// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.ide.actions.AttachDirectoryUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AngularFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(AngularFolderEnabler.class);

    /**
     * Initialization ({@link #init(Project)}) is done once at project opening to avoid
     * searching angular.json files again for every folder in the project.
     */
    private boolean initialized = false;
    private Set<String> angularFolders;
    private static final Set<String> EXCLUDED_FOLDERS = Set.of(
        "build",
        "cache",
        "idea-sandbox",
        "gradle",
        "node_modules",
        "out",
        "output",
        "private");

    // one icon enabler per project
    private static final Map<Project, IconEnabler> enablersCache = new ConcurrentHashMap<>();

    public static IconEnabler getInstance(@NotNull Project project) {
        if (enablersCache.containsKey(project)) {
            return enablersCache.get(project);
        }
        IconEnabler iconEnabler = new AngularFolderEnabler();
        enablersCache.put(project, iconEnabler);
        return iconEnabler;
    }

    private synchronized void init(@NotNull Project project) {
        long t1 = System.currentTimeMillis();
        angularFolders = findAngularFolders(project);
        initialized = true;
        long execDuration = System.currentTimeMillis() - t1;
        String logMsg = "Searched for angular.json files in project " + project.getName() + " in " + execDuration + " ms." +
            " Found angular folders: " + angularFolders;
        if (execDuration > 1000) {
            LOGGER.warn(logMsg + ". Operation should complete faster. " + ProjectUtils.PLEASE_OPEN_ISSUE_MSG);
        } else {
            LOGGER.info(logMsg);
        }
    }

    /** Find folders containing an angular.json file. Some folders are ignored in order to preserve performance. */
    private Set<String> findAngularFolders(@NotNull Project project) {
        // In some multi-module projects, per example dotNet and Angular, loaded with Rider, the
        // project may be in a subdirectory. It could look like this:
        // ■ module
        // +--■ project (dotNet solution with SLN file)
        // +--■ module (Angular, seen as "attached" directory)
        // I have no idea why the project is not at root level, but we need to find angular.json files
        // in the project and in every module.
        List<File> foldersToScan = getAttachedDirectories(project);
        String projectBasePath = project.getBasePath();
        if (projectBasePath == null) {
            LOGGER.warn("Can't list project's files (Project.getBasePath() returned null), Angular support will be limited");
        } else {
            foldersToScan.add(new File(projectBasePath));
        }
        // will scan the smallest path first: it's very probable that the other paths are children
        // of this first path, so there is no need to scan them twice
        foldersToScan.sort(Comparator.comparingLong(File::length));
        LOGGER.info("Will scan these folders for angular.json files: " + foldersToScan);

        Set<String> angularFoldersFound = new HashSet<>();
        try {
            for (File folderToScan : foldersToScan) {
                boolean folderAlreadyScannedByParent = false;
                for (String angularFolderFound : angularFoldersFound) {
                    if (normalizePath(folderToScan.getAbsolutePath()).startsWith(angularFolderFound)) {
                        folderAlreadyScannedByParent = true;
                        break;
                    }
                }
                if (!folderAlreadyScannedByParent) {
                    angularFoldersFound.addAll(findAngularFoldersRecursively(folderToScan, EXCLUDED_FOLDERS));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Error while looking for angular folders, Angular support will be limited", e);
        }
        return angularFoldersFound;
    }

    private Set<String> findAngularFoldersRecursively(File folder, Set<String> ignoredFolderNames) {
        Set<String> angularFoldersFound = new HashSet<>();
        if (new File(folder, "angular.json").exists()) {
            angularFoldersFound.add(normalizePath(normalizePath(folder.getAbsolutePath())));
            return angularFoldersFound;
        }
        File[] baseFolders = folder.listFiles(file -> {
                if (!file.isDirectory()) {
                    return false;
                }
                String fileName = file.getName();
                return !fileName.startsWith(".") && !ignoredFolderNames.contains(fileName);
            }
        );
        if (baseFolders != null) {
            for (File subFolder : baseFolders) {
                angularFoldersFound.addAll(findAngularFoldersRecursively(subFolder, ignoredFolderNames));
            }
        }
        return angularFoldersFound;
    }

    /** Return the list of attached projects by reading the optional {@code .idea/indexLayout.xml} file. */
    private List<File> getAttachedDirectories(Project project) {
        List<File> attachedDirectories = new ArrayList<>();
        AttachDirectoryUtils.getAttachedDirectories(project).forEach(virtualFile -> {
            if (virtualFile.isDirectory()) {
                attachedDirectories.add(new File(virtualFile.getPath()));
            }
        });
        return attachedDirectories;
    }

    /** Should (Re)Init if initialization never occurred or if latest initialization is too old. */
    private boolean shouldInit() {
        return !initialized;
    }

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        if (shouldInit()) {
            init(project);
        }
        String normalizedPathToVerify = normalizePath(absolutePathToVerify);
        for (String angularFolder : angularFolders) {
            if (normalizedPathToVerify.startsWith(angularFolder)) {
                return true;
            }
        }
        return false;
    }

    private String normalizePath(@NotNull String path) {
        return path.toLowerCase().replaceAll("\\\\", "/");
    }

    @Override
    public boolean terminatesConditionEvaluation() {
        return false;
    }
}
