// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers.services;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.enablers.AbstractFolderEnabler;
import lermitage.intellij.extra.icons.enablers.IconEnabler;
import org.jetbrains.annotations.NotNull;

@Service(Service.Level.PROJECT)
public final class HelmFolderEnablerService extends AbstractFolderEnabler implements IconEnabler {

    public static HelmFolderEnablerService getInstance(@NotNull Project project) {
        return project.getService(HelmFolderEnablerService.class);
    }

    @NotNull
    @Override
    protected String[] getFilenamesToSearch() {
        return new String[]{"Chart.yaml", "values.yaml"};
    }

    @Override
    public String getName() {
        return "Helm folder icon"; //NON-NLS
    }
}
