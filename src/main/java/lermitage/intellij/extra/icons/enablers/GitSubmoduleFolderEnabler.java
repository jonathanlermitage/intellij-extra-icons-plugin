// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.utils.GitSubmoduleUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GitSubmoduleFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(GitSubmoduleFolderEnabler.class);

    private static final long INIT_TTL_MS = 300_000L;
    private boolean initialized = false;
    private long lastInit = -1;
    private Set<String> submoduleFolders;

    // one icon enabler per project
    private static final Map<Project, IconEnabler> iconEnablersCache = new ConcurrentHashMap<>();

    public static IconEnabler getIntance(@NotNull Project project) {
        if (iconEnablersCache.containsKey(project)) {
            return iconEnablersCache.get(project);
        }
        GitSubmoduleFolderEnabler iconEnabler = new GitSubmoduleFolderEnabler();
        iconEnablersCache.put(project, iconEnabler);
        return iconEnabler;
    }

    private synchronized void init(@NotNull Project project) {
        long t1 = System.currentTimeMillis();
        submoduleFolders = findGitmodulesFiles(project);
        initialized = true;
        long t2 = System.currentTimeMillis();
        lastInit = t2;
        LOGGER.info("Initialized " + this.getClass().getSimpleName() + " for project " + project.getBasePath() + " in " + (t2 - t1) + " ms");
    }

    private Set<String> findGitmodulesFiles(@NotNull Project project) {
        Set<String> submoduleFoldersFound = new HashSet<>();
        String basePath = project.getBasePath();
        // find .gitmodules at root, then find every nested .gitmodules for every module (don't have to explore the whole project files)
        try {
            submoduleFoldersFound = GitSubmoduleUtils
                .findGitSubmodules(basePath)
                .stream()
                .map(gitSubmodule -> gitSubmodule.getPath().toLowerCase())
                .collect(Collectors.toSet());
            submoduleFoldersFound.addAll(findNestedGitmodulesFiles(submoduleFoldersFound));
        } catch (FileNotFoundException e) {
            LOGGER.warn("Error while looking for git submodules", e);
        }
        LOGGER.info("Found git submodules: " + submoduleFoldersFound);
        return submoduleFoldersFound;
    }

    private Set<String> findNestedGitmodulesFiles(@NotNull Set<String> parentModules) { // TODO should do some refactoring
        Set<String> nestedModules = new HashSet<>();
        for (String parentModule : parentModules) {
            try {
                Set<String> submoduleFoldersFound = GitSubmoduleUtils
                    .findGitSubmodules(parentModule)
                    .stream()
                    .map(gitSubmodule -> gitSubmodule.getPath().toLowerCase())
                    .collect(Collectors.toSet());
                if (!submoduleFoldersFound.isEmpty()) {
                    nestedModules.addAll(submoduleFoldersFound);
                    nestedModules.addAll(findNestedGitmodulesFiles(submoduleFoldersFound));
                }
            } catch (FileNotFoundException e) {
                LOGGER.warn("Error while looking for nested git submodules", e);
            }
        }
        return nestedModules;
    }

    /** Should (Re)Init if initialization never occurred or if latest initialization is too old. */
    private boolean shouldInit(@NotNull Project project) {
        return !initialized || (System.currentTimeMillis() - lastInit) > INIT_TTL_MS;
    }

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        if (shouldInit(project)) {
            init(project);
        }
        return submoduleFolders.contains(absolutePathToVerify.toLowerCase());
    }
}
