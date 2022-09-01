// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.utils.GitSubmoduleUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GitSubmoduleFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(GitSubmoduleFolderEnabler.class);

    /**
     * Initialization ({@link #init(Project)}) is done once at project opening and at least every {@value}ms (if needed) to avoid
     * parsing {@code .gitmodule} files for every folder in the project.
     * Useful if you add a new git submodule: user will wait up to {@value}ms to see the corresponding git submodule icon.
     */
    private static final long INIT_TTL_MS = 300_000L; // 5min
    private boolean initialized = false;
    private long lastInit = -1;
    private Set<String> submoduleFolders;

    // one icon enabler per project
    private static final Map<Project, IconEnabler> enablersCache = new ConcurrentHashMap<>();

    public static IconEnabler getInstance(@NotNull Project project) {
        if (enablersCache.containsKey(project)) {
            return enablersCache.get(project);
        }
        IconEnabler iconEnabler = new GitSubmoduleFolderEnabler();
        enablersCache.put(project, iconEnabler);
        return iconEnabler;
    }

    @Override
    public synchronized void init(@NotNull Project project) {
        long t1 = System.currentTimeMillis();
        submoduleFolders = findGitModulesFiles(project);
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
    private Set<String> findGitModulesFiles(@NotNull Project project) {
        Set<String> submoduleFoldersFound = new HashSet<>();
        String basePath = project.getBasePath();
        try {
            submoduleFoldersFound = GitSubmoduleUtils
                .findGitSubmodules(basePath)
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
            submoduleFoldersFound.addAll(findNestedGitModulesFiles(submoduleFoldersFound));
        } catch (FileNotFoundException e) {
            LOGGER.warn("Error while looking for git submodules", e);
        }
        return submoduleFoldersFound;
    }

    private Set<String> findNestedGitModulesFiles(@NotNull Set<String> parentModules) { // TODO should do some refactoring
        Set<String> nestedModules = new HashSet<>();
        for (String parentModule : parentModules) {
            try {
                Set<String> submoduleFoldersFound = GitSubmoduleUtils
                    .findGitSubmodules(parentModule)
                    .stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
                if (!submoduleFoldersFound.isEmpty()) {
                    nestedModules.addAll(submoduleFoldersFound);
                    nestedModules.addAll(findNestedGitModulesFiles(submoduleFoldersFound));
                }
            } catch (FileNotFoundException e) {
                LOGGER.warn("Error while looking for nested git submodules", e);
            }
        }
        return nestedModules;
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
