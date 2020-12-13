// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.utils.GitSubmoduleUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GitSubmoduleFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(GitSubmoduleFolderEnabler.class);

    private static final long INIT_TTL_MS = 300_000L;
    private final Map<Project, Boolean> initialized = new HashMap<>();
    private final Map<Project, Long> lastInit = new HashMap<>();
    private Set<String> submoduleFolders;

    @Override
    public synchronized void init(Project project) {
        long t1 = System.currentTimeMillis();
        submoduleFolders = findGitmodulesFiles(project);
        initialized.put(project, true);
        long t2 = System.currentTimeMillis();
        lastInit.put(project, t2);
        LOGGER.info("Initialized " + this.getClass().getSimpleName() + " for project " + project.getBasePath() + " in " + (t2 - t1) + " ms");
    }

    private Set<String> findGitmodulesFiles(Project project) {
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

    private Set<String> findNestedGitmodulesFiles(Set<String> parentModules) { // TODO should do some refactoring
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
    private boolean shouldInit(Project project) {
        return !initialized.containsKey(project)
            || !initialized.get(project)
            || !lastInit.containsKey(project)
            || (System.currentTimeMillis() - lastInit.get(project)) > INIT_TTL_MS;
    }

    @Override
    public boolean verify(Project project, String absolutePathToVerify) {
        if (shouldInit(project)) {
            init(project);
        }
        return submoduleFolders.contains(absolutePathToVerify.toLowerCase());
    }
}
