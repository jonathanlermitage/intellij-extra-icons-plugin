// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.enablers.services.GitSubmoduleFolderEnablerService;
import lermitage.intellij.extra.icons.enablers.services.HelmFolderEnablerService;
import lermitage.intellij.extra.icons.enablers.services.InAngularFolderEnblerService;
import lermitage.intellij.extra.icons.enablers.services.InGraphQLFolderEnablerService;
import lermitage.intellij.extra.icons.enablers.services.InHelmFolderEnablerService;
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
                return Optional.of(GitSubmoduleFolderEnablerService.getInstance(project));
            case IS_HELM_FOLDER:
                return Optional.of(HelmFolderEnablerService.getInstance(project));
            case IS_IN_ANGULAR_FOLDER:
                return Optional.of(InAngularFolderEnblerService.getInstance(project));
            case IS_IN_GRAPHQL_FOLDER:
                return Optional.of(InGraphQLFolderEnablerService.getInstance(project));
            case IS_IN_HELM_FOLDER:
                return Optional.of(InHelmFolderEnablerService.getInstance(project));
            default:
                LOGGER.warn("Invalid IconEnablerType: " + type);
                return Optional.empty();
        }
    }
}
