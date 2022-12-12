// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers.services;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.enablers.AbstractInFolderEnabler;
import lermitage.intellij.extra.icons.enablers.IconEnabler;
import org.jetbrains.annotations.NotNull;

@Service
public final class InAngularFolderEnblerService extends AbstractInFolderEnabler implements IconEnabler {

    public InAngularFolderEnblerService(@NotNull Project project) {
        init(project);
    }

    public static InAngularFolderEnblerService getInstance(@NotNull Project project) {
        return project.getService(InAngularFolderEnblerService.class);
    }

    @NotNull
    @Override
    protected String[] getFilenamesToSearch() {
        return new String[]{"angular.json"};
    }

    @Override
    public String getName() {
        return "Angular icons";
    }
}
