// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers.services;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.enablers.AbstractInFolderEnabler;
import lermitage.intellij.extra.icons.enablers.IconEnabler;
import org.jetbrains.annotations.NotNull;

@Service(Service.Level.PROJECT)
public final class InWritersideFolderEnablerService extends AbstractInFolderEnabler implements IconEnabler {

    public static InWritersideFolderEnablerService getInstance(@NotNull Project project) {
        return project.getService(InWritersideFolderEnablerService.class);
    }

    @NotNull
    @Override
    protected String[] getFilenamesToSearch() {
        return new String[]{"writerside.cfg"};
    }

    @Override
    public String getName() {
        return "Writerside icons"; //NON-NLS
    }
}
