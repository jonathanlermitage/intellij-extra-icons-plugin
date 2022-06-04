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
        switch (type) {
            case IS_GIT_SUBMODULE_FOLDER:
                return Optional.of(GitSubmoduleFolderEnabler.getInstance(project));
            case IS_IN_ANGULAR_FOLDER:
                return Optional.of(AngularFolderEnabler.getInstance(project));
            default:
                throw new IllegalArgumentException("Invalid IconEnablerType: " + type);
        }
    }
}
