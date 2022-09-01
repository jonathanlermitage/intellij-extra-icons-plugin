// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import lermitage.intellij.extra.icons.utils.ProjectUtils;

import java.util.Optional;

public class EnablerUtils {

    public static void forceInitAllEnablers() {
        for (IconEnablerType iconEnablerType : IconEnablerType.values()) {
            Project[] projects = ProjectManager.getInstance().getOpenProjects();
            for (Project project : projects) {
                if (ProjectUtils.isAlive(project)) {
                    Optional<IconEnabler> iconEnabler = IconEnablerProvider.getIconEnabler(project, iconEnablerType);
                    iconEnabler.ifPresent(enabler -> enabler.init(project));
                }
            }
        }
    }
}
