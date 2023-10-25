// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.Nullable;

public class ProjectUtils {

    private static final Logger LOGGER = Logger.getInstance(ProjectUtils.class);

    public static @Nullable Project getFirstOpenedProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        return projects.length == 0 ? null : projects[0];
    }

    /**
     * Return true if the project can be manipulated. Project is not null, not disposed, etc.
     * Developed to fix <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/39">issue #39</a>.
     */
    public static boolean isProjectAlive(@Nullable Project project) {
        if (project != null && !project.isDisposed()) {
            return true;
        } else {
            if (project == null) {
                LOGGER.warn("Project is null"); //NON-NLS
            } else {
                LOGGER.warn("Project '" + project.getName() + "' is not alive - Project is disposed: " + project.isDisposed()); //NON-NLS
            }
            return false;
        }
    }
}
