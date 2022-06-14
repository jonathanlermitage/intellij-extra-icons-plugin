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

public class HelmFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(HelmFolderEnabler.class);

    /**
     * Initialization ({@link #init(Project)}) is done once at project opening to avoid
     * searching Helm folders again for every folder in the project.
     */
    private boolean initialized = false;
    private Set<String> helmFolders;

    // one icon enabler per project
    private static final Map<Project, IconEnabler> enablersCache = new ConcurrentHashMap<>();

    public static IconEnabler getInstance(@NotNull Project project) {
        if (enablersCache.containsKey(project)) {
            return enablersCache.get(project);
        }
        IconEnabler iconEnabler = new HelmFolderEnabler();
        enablersCache.put(project, iconEnabler);
        return iconEnabler;
    }

    private synchronized void init(@NotNull Project project) {
        long t1 = System.currentTimeMillis();
        helmFolders = findHelmFolders(project);
        initialized = true;
        long execDuration = System.currentTimeMillis() - t1;
        String logMsg = "Searched for Helm folders in project " + project.getName() + " in " + execDuration + " ms." +
            " Found Helm folders: " + helmFolders;
        if (execDuration > 1000) {
            LOGGER.warn(logMsg + ". Operation should complete faster. " + ProjectUtils.PLEASE_OPEN_ISSUE_MSG);
        } else {
            LOGGER.info(logMsg);
        }
    }

    /** Find folders containing Helm file. Some folders are ignored in order to preserve performance. */
    private Set<String> findHelmFolders(@NotNull Project project) {
        // In some multi-module projects, per example dotNet and Angular, loaded with Rider, the
        // project may be in a subdirectory. It could look like this:
        // ■ module
        // +--■ project (dotNet solution with SLN file)
        // +--■ module (Angular, seen as "attached" directory)
        // I have no idea why the project is not at root level, but we need to Helm files
        // in the project and in every module.
        List<String> foldersToScan = getAttachedDirectories(project);
        String projectBasePath = project.getBasePath();
        if (projectBasePath == null) {
            LOGGER.warn("Can't list project's files (Project.getBasePath() returned null), Helm support will be limited");
        } else if (!foldersToScan.contains(projectBasePath)) {
            foldersToScan.add(projectBasePath);
        }
        // will scan the smallest path first: it's very probable that the other paths are children
        // of this first path, so there is no need to scan them twice
        foldersToScan.sort(Comparator.comparingLong(String::length));
        LOGGER.info("Will scan these folders for Helm files: " + foldersToScan);

        Set<String> helmFoldersFound = new HashSet<>();
        try {
            for (String folderToScan : foldersToScan) {
                helmFoldersFound.addAll(findHelmFoldersRecursively(new File(folderToScan), ProjectUtils.EXCLUDED_FOLDERS));
            }
        } catch (Exception e) {
            LOGGER.warn("Error while looking for Helm folders, Helm support will be limited", e);
        }
        return helmFoldersFound;
    }

    private boolean allFilesExist(File folder, String... filenames) {
        for (String filename : filenames) {
            File file = new File(folder, filename);
            if (!file.exists() || !file.isFile()) {
                return false;
            }
        }
        return true;
    }

    private Set<String> findHelmFoldersRecursively(File folder, Set<String> ignoredFolderNames) {
        Set<String> helmFoldersFound = new HashSet<>();
        if (allFilesExist(folder, "Chart.yaml", "values.yaml")) {
            helmFoldersFound.add(normalizePath(normalizePath(folder.getAbsolutePath())));
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
                helmFoldersFound.addAll(findHelmFoldersRecursively(subFolder, ignoredFolderNames));
            }
        }
        return helmFoldersFound;
    }

    /** Return the list of attached projects. */
    private List<String> getAttachedDirectories(Project project) {
        List<String> attachedDirectories = new ArrayList<>();
        AttachDirectoryUtils.getAttachedDirectories(project).forEach(virtualFile -> {
            if (virtualFile.isDirectory()) {
                attachedDirectories.add(virtualFile.getPath());
            }
        });
        return attachedDirectories;
    }

    /** Should (Re)Init if initialization never occurred. */
    private boolean shouldInit() {
        return !initialized;
    }

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        if (shouldInit()) {
            init(project);
        }
        String normalizedPathToVerify = normalizePath(absolutePathToVerify);
        for (String helmFolder : helmFolders) {
            if (normalizedPathToVerify.equals(helmFolder)) {
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
        return true;
    }
}
