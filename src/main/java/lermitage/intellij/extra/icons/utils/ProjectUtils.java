// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

public class ProjectUtils {

    public static final @NonNls String PLEASE_OPEN_ISSUE_MSG = "You could open an issue: " +
        "https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues. Thank you!";

    public static @Nullable Project getFirstOpenedProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        return projects.length == 0 ? null : projects[0];
    }

    /**
     * Return true if the project can be manipulated. Project is not null, not disposed, etc.
     * Developed to fix <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/39">issue #39</a>.
     */
    public static boolean isProjectAlive(@Nullable Project project) {
        return project != null && !project.isDisposed();
    }
}
