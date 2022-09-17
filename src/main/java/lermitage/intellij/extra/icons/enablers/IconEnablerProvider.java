// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IconEnablerProvider {

    private static final Logger LOGGER = Logger.getInstance(IconEnablerProvider.class);

    public static Optional<IconEnabler> getIconEnabler(@Nullable Project project, @Nullable IconEnablerType type) {
        if (project == null || type == null) {
            return Optional.empty();
        }
        switch (type) {
            case IS_GIT_SUBMODULE_FOLDER:
                return Optional.of(GitSubmoduleFolderEnabler.getInstance(project));
            case IS_HELM_FOLDER:
                return Optional.of(HelmFolderEnabler.getInstance(project));
            case IS_IN_ANGULAR_FOLDER:
                return Optional.of(InAngularFolderEnabler.getInstance(project));
            case IS_IN_GRAPHQL_FOLDER:
                return Optional.of(InGraphQLFolderEnabler.getInstance(project));
            case IS_IN_HELM_FOLDER:
                return Optional.of(InHelmFolderEnabler.getInstance(project));
            default:
                LOGGER.warn("Invalid IconEnablerType: " + type);
                return Optional.empty();
        }
    }
}
