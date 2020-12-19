// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IconEnablerProvider {

    public static Optional<IconEnabler> getIconEnabler(@Nullable Project project, @Nullable IconEnablerType type) {
        if (project == null || type == null) {
            return Optional.empty();
        }
        //noinspection SwitchStatementWithTooFewBranches
        switch (type) {
            case GIT_SUBMODULE_FOLDER:
                return Optional.of(GitSubmoduleFolderEnabler.getIntance(project));
            default:
                throw new IllegalArgumentException("Invalid IconEnablerType: " + type);
        }
    }
}
