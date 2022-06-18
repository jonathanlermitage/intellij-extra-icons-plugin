// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;
import com.intellij.ide.actions.AttachDirectoryUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

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

    @SuppressWarnings("UnstableApiUsage")
    private synchronized void init(@NotNull Project project) {
        long t1 = System.currentTimeMillis();

        TimeLimiter timeLimiter = SimpleTimeLimiter.create(Executors.newSingleThreadExecutor());
        Duration timeout = Duration.ofMillis(500);
        // TODO this is a temporary and urgent fix for issue #99, I need to investigate and improve 'init' and 'findAngularFolders' code
        try {
            angularFolders = timeLimiter.callWithTimeout(() -> findAngularFolders(project), timeout);
        } catch (UncheckedTimeoutException | TimeoutException | InterruptedException | ExecutionException e) {
            LOGGER.warn("Already spent 500 ms searching for angular.json files, task cancelled to avoid potential IDE freeze. " +
                "Angular support will be limited. " + ProjectUtils.PLEASE_OPEN_ISSUE_MSG);
            angularFolders = new HashSet<>();
        }

        String projectBasePath = project.getBasePath();
        if (projectBasePath == null) {
            LOGGER.warn("Can't list project's files (Project.getBasePath() returned null), Angular support will be limited");
        } else if (new File(project.getBasePath(), "angular.json").exists()) {
            angularFolders.add(normalizePath(projectBasePath));
        }

        initialized = true;
        long execDuration = System.currentTimeMillis() - t1;
        LOGGER.info("Searched for angular.json files in project " + project.getName() + " in " + execDuration + " ms." +
            " Found angular folders: " + angularFolders);
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

    @Override
    public boolean terminatesConditionEvaluation() {
        return false;
    }

    private static String normalizePath(@NotNull String path) {
        return path.toLowerCase().replaceAll("\\\\", "/");
    }

    /** Find folders containing an angular.json file. Some folders are ignored in order to preserve performance. */
    private static Set<String> findAngularFolders(@NotNull Project project) {
        // In some multi-module projects, per example dotNet and Angular, loaded with Rider, the
        // project may be in a subdirectory. It could look like this:
        // ■ module
        // +--■ project (dotNet solution with SLN file)
        // +--■ module (Angular, seen as "attached" directory)
        // I have no idea why the project is not at root level, but we need to find angular.json files
        // in the project and in every module.
        List<File> foldersToScan = getAttachedDirectories(project);

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

    private static List<File> getAttachedDirectories(Project project) {
        List<File> attachedDirectories = new ArrayList<>();
        AttachDirectoryUtils.getAttachedDirectories(project).forEach(virtualFile -> {
            if (virtualFile.isDirectory()) {
                attachedDirectories.add(new File(virtualFile.getPath()));
            }
        });
        return attachedDirectories;
    }

    private static Set<String> findAngularFoldersRecursively(File folder, Set<String> ignoredFolderNames) {
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
}
