// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IconEnablerProvider {

    // one icon enabler per project
    private static final Map<Project, GitSubmoduleFolderEnabler> gitSubmoduleFolderIconEnablersCache = new HashMap<>();

    public static Optional<IconEnabler> getIconEnabler(Project project, IconEnablerType type) {
        if (project == null) {
            return Optional.empty();
        }
        //noinspection SwitchStatementWithTooFewBranches
        switch (type) {
            case GIT_SUBMODULE_FOLDER:
                if (gitSubmoduleFolderIconEnablersCache.containsKey(project)) {
                    return Optional.of(gitSubmoduleFolderIconEnablersCache.get(project));
                }
                GitSubmoduleFolderEnabler iconEnabler = new GitSubmoduleFolderEnabler();
                gitSubmoduleFolderIconEnablersCache.put(project, iconEnabler);
                return Optional.of(iconEnabler);
            default:
                throw new IllegalArgumentException("Invalid IconEnablerType: " + type);
        }
    }
}
