// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.activity;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import lermitage.intellij.extra.icons.messaging.RefreshIconsNotifier;
import lermitage.intellij.extra.icons.utils.IJUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RefreshIconsListenerActivity implements ProjectActivity {

    private static final Logger LOGGER = Logger.getInstance(RefreshIconsListenerActivity.class);

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        project.getMessageBus().connect().subscribe(RefreshIconsNotifier.EXTRA_ICONS_REFRESH_ICONS_NOTIFIER_TOPIC,
            new RefreshIconsNotifier() {

                @Override
                public void refreshProjectIcons(@Nullable Project project) {
                    refreshProject(project);
                }
            });
        return null;
    }

    private void refreshProject(Project project) {
        ApplicationManager.getApplication().runReadAction(() -> {
            if (ProjectUtils.isProjectAlive(project)) {
                ProjectView view = ProjectView.getInstance(project);
                if (view != null) {
                    IJUtils.runInBGT("refresh ProjectView", view::refresh, true); //NON-NLS
                    AbstractProjectViewPane currentProjectViewPane = view.getCurrentProjectViewPane();
                    if (currentProjectViewPane != null) {
                        IJUtils.runInBGT("update AbstractProjectViewPane", () -> currentProjectViewPane.updateFromRoot((true)), true); //NON-NLS
                    }
                    try {
                        EditorWindow[] editorWindows = FileEditorManagerEx.getInstanceEx(project).getWindows();
                        for (EditorWindow editorWindow : editorWindows) {
                            try {
                                IJUtils.runInBGT("refresh EditorWindow icons", () -> editorWindow.getManager().refreshIcons(), true); //NON-NLS
                            } catch (Exception e) {
                                LOGGER.warn("Failed to refresh editor tabs icon (EditorWindow manager failed to refresh icons)", e); //NON-NLS
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.warn("Failed to refresh editor tabs icon (can't get FileEditorManagerEx instance or project's windows)", e); //NON-NLS
                    }
                }
            }
        });
    }

    private void refreshAllOpenedProjects() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            refreshProject(project);
        }
    }
}
