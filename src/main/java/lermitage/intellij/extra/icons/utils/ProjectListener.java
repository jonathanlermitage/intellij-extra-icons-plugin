// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import lermitage.intellij.extra.icons.enablers.GitSubmoduleFolderEnabler;
import lermitage.intellij.extra.icons.enablers.HelmFolderEnabler;
import lermitage.intellij.extra.icons.enablers.InAngularFolderEnabler;
import lermitage.intellij.extra.icons.enablers.InGraphQLFolderEnabler;
import lermitage.intellij.extra.icons.enablers.InHelmFolderEnabler;
import org.jetbrains.annotations.NotNull;

public class ProjectListener implements ProjectManagerListener {

    @Override
    public void projectClosed(@NotNull Project project) {
        // clear caches associated to given closed project
        ProjectUtils.onProjectClose(project);
        GitSubmoduleFolderEnabler.onProjectClose(project);
        HelmFolderEnabler.onProjectClose(project);
        InAngularFolderEnabler.onProjectClose(project);
        InGraphQLFolderEnabler.onProjectClose(project);
        InHelmFolderEnabler.onProjectClose(project);
    }
}
