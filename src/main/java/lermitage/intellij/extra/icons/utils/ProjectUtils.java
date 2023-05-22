// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

public class ProjectUtils {

    private static final Logger LOGGER = Logger.getInstance(ProjectUtils.class);

    public static final @NonNls String PLEASE_OPEN_ISSUE_MSG = "You could open an issue: " +
        "https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues. Thank you!";

    /**
     * Refresh project view.
     */
    public static void refreshProject(Project project) {
        if (ProjectUtils.isProjectAlive(project)) {
            ProjectView view = ProjectView.getInstance(project);
            if (view != null) {
                view.refresh();
                AbstractProjectViewPane currentProjectViewPane = view.getCurrentProjectViewPane();
                if (currentProjectViewPane != null) {
                    currentProjectViewPane.updateFromRoot(true);
                }
                try {
                    EditorWindow[] editorWindows = FileEditorManagerEx.getInstanceEx(project).getWindows();
                    for (EditorWindow editorWindow : editorWindows) {
                        editorWindow.getManager().refreshIcons();
                    }
                } catch (Exception e) {
                    LOGGER.warn("Failed to refresh editor tabs icon", e); //NON-NLS
                }
            }
        }
    }

    /**
     * Refresh all opened project views.
     */
    public static void refreshAllOpenedProjects() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            refreshProject(project);
        }
    }

    /**
     * Return true if the project can be manipulated. Project is not null, not disposed, etc.
     * Developed to fix <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/39">issue #39</a>.
     */
    public static boolean isProjectAlive(@Nullable Project project) {
        return project != null && !project.isDisposed();
    }

    public static @Nullable Project getFirstOpenedProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        return projects.length == 0 ? null : projects[0];
    }
}
