// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import lermitage.intellij.extra.icons.utils.ProjectUtils;

import java.util.Optional;

public class EnablerUtils {

    public static void forceInitAllEnablers() throws Exception {
        forceInitAllEnablers(true);
    }

    public static void forceInitAllEnablers(boolean silentErrors) throws Exception {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            forceInitAllEnablers(project, silentErrors);
        }
    }

    public static void forceInitAllEnablers(Project project) throws Exception {
        forceInitAllEnablers(project, true);
    }

    public static void forceInitAllEnablers(Project project, boolean silentErrors) throws Exception {
        for (IconEnablerType iconEnablerType : IconEnablerType.values()) {
            if (ProjectUtils.isAlive(project)) {
                Optional<IconEnabler> iconEnabler = IconEnablerProvider.getIconEnabler(project, iconEnablerType);
                if (iconEnabler.isPresent()) {
                    iconEnabler.get().init(project, silentErrors);
                }
            }
        }
    }
}
