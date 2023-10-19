// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.enablers.services.GitSubmoduleFolderEnablerService;
import lermitage.intellij.extra.icons.enablers.services.HelmFolderEnablerService;
import lermitage.intellij.extra.icons.enablers.services.InAngularFolderEnablerService;
import lermitage.intellij.extra.icons.enablers.services.InGraphQLFolderEnablerService;
import lermitage.intellij.extra.icons.enablers.services.InHelmFolderEnablerService;
import lermitage.intellij.extra.icons.enablers.services.InWritersideFolderEnablerService;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IconEnablerProvider {

    public static Optional<IconEnabler> getIconEnabler(@Nullable Project project, @Nullable IconEnablerType type) {
        if (project == null || type == null) {
            return Optional.empty();
        }
        return switch (type) {
            case IS_GIT_SUBMODULE_FOLDER -> Optional.of(GitSubmoduleFolderEnablerService.getInstance(project));
            case IS_HELM_FOLDER -> Optional.of(HelmFolderEnablerService.getInstance(project));
            case IS_IN_ANGULAR_FOLDER -> Optional.of(InAngularFolderEnablerService.getInstance(project));
            case IS_IN_GRAPHQL_FOLDER -> Optional.of(InGraphQLFolderEnablerService.getInstance(project));
            case IS_IN_HELM_FOLDER -> Optional.of(InHelmFolderEnablerService.getInstance(project));
            case IS_IN_WRITERSIDE_FOLDER -> Optional.of(InWritersideFolderEnablerService.getInstance(project));
        };
    }
}
