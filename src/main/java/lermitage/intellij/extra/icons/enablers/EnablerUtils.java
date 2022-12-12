// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import lermitage.intellij.extra.icons.utils.ProjectUtils;

import java.util.Optional;

public class EnablerUtils {

    public static void forceInitAllEnablers() throws Exception {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            forceInitAllEnablers(project);
        }
    }

    public static void forceInitAllEnablers(Project project) throws Exception {
        for (IconEnablerType iconEnablerType : IconEnablerType.values()) {
            if (ProjectUtils.isProjectAlive(project)) {
                Optional<IconEnabler> iconEnabler = IconEnablerProvider.getIconEnabler(project, iconEnablerType);
                if (iconEnabler.isPresent()) {
                    iconEnabler.get().init(project);
                }
            }
        }
    }
}
