// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum IconEnablers {

    GIT_SUBMODULE_FOLDER;

    // one icon enabler per project
    private final Map<Project, GitSubmoduleFolderEnabler> gitSubmoduleFolderIconEnablersCache = new HashMap<>();

    public Optional<IconEnabler> getIconEnabler(Project project) {
        if (project == null) {
            return Optional.empty();
        }
        //noinspection SwitchStatementWithTooFewBranches
        switch (this) {
            case GIT_SUBMODULE_FOLDER:
                if (gitSubmoduleFolderIconEnablersCache.containsKey(project)) {
                    return Optional.of(gitSubmoduleFolderIconEnablersCache.get(project));
                }
                GitSubmoduleFolderEnabler iconEnabler = new GitSubmoduleFolderEnabler();
                gitSubmoduleFolderIconEnablersCache.put(project, iconEnabler);
                return Optional.of(iconEnabler);
            default:
                return Optional.empty();
        }
    }
}
