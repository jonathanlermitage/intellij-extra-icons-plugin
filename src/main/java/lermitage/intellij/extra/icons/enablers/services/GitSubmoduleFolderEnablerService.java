// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers.services;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.enablers.IconEnabler;
import lermitage.intellij.extra.icons.utils.LogUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public final class GitSubmoduleFolderEnablerService implements IconEnabler {

    public GitSubmoduleFolderEnablerService(@NotNull Project project) {
        init(project);
    }

    public static GitSubmoduleFolderEnablerService getInstance(@NotNull Project project) {
        return project.getService(GitSubmoduleFolderEnablerService.class);
    }

    private static final Logger LOGGER = Logger.getInstance(GitSubmoduleFolderEnablerService.class);
    private static final String GIT_MODULES_FILENAME = ".gitmodules";
    private static final Pattern GIT_MODULES_PATH_PATTERN = Pattern.compile("\\s*path\\s*=\\s*([^\\s]+)\\s*");

    /**
     * Initialization ({@link #init(Project)}) is done once at project opening and at least every {@value}ms (if needed) to avoid
     * parsing {@code .gitmodule} files for every folder in the project.
     * Useful if you add a new git submodule: user will wait up to {@value}ms to see the corresponding git submodule icon.
     */
    private static final long INIT_TTL_MS = 300_000L; // 5min
    private boolean initialized = false;
    private long lastInit = -1;
    private Set<String> submoduleFolders = Collections.emptySet();

    @Override
    public synchronized void init(@NotNull Project project) {
        long t1 = System.currentTimeMillis();
        try {
            submoduleFolders = findAllGitModulesFilesRecursively(project);
        } catch (Exception e) {
            LogUtils.showErrorIfAllowedByUser(LOGGER, "Failed to init Git submodule Enabler", e);
        }

        initialized = true;
        long t2 = System.currentTimeMillis();
        lastInit = t2;
        long execDuration = t2 - t1;
        String logMsg = "Searched for git submodules in project " + project.getName() + " in " + execDuration + " ms." +
            " Found git submodule folders: " + submoduleFolders;
        if (execDuration > 4000) {
            LOGGER.warn(logMsg + ". Operation should complete faster. " + ProjectUtils.PLEASE_OPEN_ISSUE_MSG);
        } else {
            LOGGER.info(logMsg);
        }
    }

    /** Find .gitmodules at root, then find every nested .gitmodules for every module (don't have to explore the whole project files). */
    private Set<String> findAllGitModulesFilesRecursively(@NotNull Project project) {
        Set<String> submoduleFoldersFound = new HashSet<>();
        String basePath = project.getBasePath();
        if (basePath == null) {
            return submoduleFoldersFound;
        }
        try {
            submoduleFoldersFound = findGitModulesFilesInFolder(basePath)
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
            submoduleFoldersFound.addAll(findNestedGitModulesFilesRecursively(submoduleFoldersFound));
        } catch (FileNotFoundException e) {
            LogUtils.showErrorIfAllowedByUser(LOGGER, "Error while looking for git submodules", e);
        }
        return submoduleFoldersFound;
    }

    private Set<String> findNestedGitModulesFilesRecursively(@NotNull Set<String> parentModules) {
        Set<String> nestedModules = new HashSet<>();
        for (String parentModule : parentModules) {
            try {
                Set<String> submoduleFoldersFound = findGitModulesFilesInFolder(parentModule)
                    .stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
                if (!submoduleFoldersFound.isEmpty()) {
                    nestedModules.addAll(submoduleFoldersFound);
                    nestedModules.addAll(findNestedGitModulesFilesRecursively(submoduleFoldersFound));
                }
            } catch (FileNotFoundException e) {
                LogUtils.showErrorIfAllowedByUser(LOGGER,
                    "Error while looking for nested git submodules (parent git module: '" + parentModule + "')",
                    e);
            } catch (StackOverflowError e) {
                LogUtils.showErrorIfAllowedByUser(LOGGER,
                    "Error while looking for nested git submodules (parent git module: '" + parentModule + "')" +
                        ", the git submodules tree is too deep",
                    e);
            }
        }
        return nestedModules;
    }

    /**
     * Find Git submodules in given folder.
     * @param folderPath folder's path.
     * @return submodule paths relative to folderPath.
     * @throws FileNotFoundException if folderPath doesn't exist.
     */
    private Set<String> findGitModulesFilesInFolder(@NotNull String folderPath) throws FileNotFoundException {
        File rootGitModules = new File(folderPath, GIT_MODULES_FILENAME);
        if (!rootGitModules.exists()) {
            return Collections.emptySet();
        }
        Set<String> submodules = new HashSet<>();
        Scanner scanner = new Scanner(rootGitModules);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = GIT_MODULES_PATH_PATTERN.matcher(line);
            if (matcher.find()) {
                String path = matcher.group(1);
                submodules.add(folderPath + "/" + path);
            }
        }
        return submodules;
    }

    /** Should (Re)Init if initialization never occurred or if latest initialization is too old. */
    private boolean shouldInit() {
        return !initialized || (System.currentTimeMillis() - lastInit) > INIT_TTL_MS;
    }

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        if (shouldInit()) {
            init(project);
        }
        return submoduleFolders.contains(absolutePathToVerify.toLowerCase());
    }

    @Override
    public boolean terminatesConditionEvaluation() {
        return true;
    }
}
