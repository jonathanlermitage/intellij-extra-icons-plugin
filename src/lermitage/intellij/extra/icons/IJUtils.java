// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

public class IJUtils {

    /**
     * Refresh project view.
     */
    public static void refresh(Project project) {
        if (project != null) {
            ProjectView view = ProjectView.getInstance(project);
            view.refresh();
            view.getCurrentProjectViewPane().updateFromRoot(true);
        }
    }

    /**
     * Refresh all opened project views.
     */
    public static void refreshOpenedProjects() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            refresh(project);
        }
    }
}
