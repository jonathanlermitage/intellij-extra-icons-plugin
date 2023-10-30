// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.activity;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import lermitage.intellij.extra.icons.enablers.IconEnabler;
import lermitage.intellij.extra.icons.enablers.IconEnablerProvider;
import lermitage.intellij.extra.icons.enablers.IconEnablerType;
import lermitage.intellij.extra.icons.messaging.RefreshIconsNotifier;
import lermitage.intellij.extra.icons.utils.IJUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RefreshIconsListenerProjectActivity implements ProjectActivity {

    private static final Logger LOGGER = Logger.getInstance(RefreshIconsListenerProjectActivity.class);

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        String projectName = project.getName();
        project.getMessageBus().connect().subscribe(RefreshIconsNotifier.EXTRA_ICONS_REFRESH_ICONS_NOTIFIER_TOPIC,
            new RefreshIconsNotifier() {

                @Override
                public void refreshProjectIcons(@Nullable Project project) {
                    if (LOGGER.isDebugEnabled()) {
                        // activate with Help > Diagnostic Tools > Debug Log Settings > #lermitage.intellij.extra.icons.RefreshIconsListenerProjectActivity
                        LOGGER.debug("refreshProjectIcons on project: " + projectName); //NON-NLS
                    }
                    refreshIcons(project);
                }

                @Override
                public void reinitProjectIconEnablers(@Nullable Project project) {
                    if (LOGGER.isDebugEnabled()) {
                        // activate with Help > Diagnostic Tools > Debug Log Settings > #lermitage.intellij.extra.icons.RefreshIconsListenerProjectActivity
                        LOGGER.debug("reinitProjectIconEnablers on project: " + projectName); //NON-NLS
                    }
                    reinitIconEnablers(project);
                }
            });
        return null;
    }

    private void reinitIconEnablers(@Nullable Project project) {
        IJUtils.runInBGT("reinit icon enablers", () -> { //NON-NLS
            if (ProjectUtils.isProjectAlive(project)) {
                assert project != null;
                DumbService.getInstance(project).runReadActionInSmartMode(() -> {
                    for (IconEnablerType iconEnablerType : IconEnablerType.values()) {
                        if (ProjectUtils.isProjectAlive(project)) {
                            Optional<IconEnabler> iconEnabler = IconEnablerProvider.getIconEnabler(project, iconEnablerType);
                            iconEnabler.ifPresent(enabler -> enabler.init(project));
                        }
                    }
                });
            }
        }, true);
    }

    private void refreshIcons(@Nullable Project project) {
        ApplicationManager.getApplication().runReadAction(() -> {
            if (ProjectUtils.isProjectAlive(project)) {
                assert project != null;

                ProjectView view = ProjectView.getInstance(project);
                if (view != null) {

                    view.refresh();
                    //IJUtils.runInBGT("refresh ProjectView", view::refresh, true); //NON-NLS

                    AbstractProjectViewPane currentProjectViewPane = view.getCurrentProjectViewPane();
                    if (currentProjectViewPane != null) {

                        currentProjectViewPane.updateFromRoot(true);
                        //IJUtils.runInBGT("update AbstractProjectViewPane", () -> currentProjectViewPane.updateFromRoot(true), true); //NON-NLS

                    } else {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.warn("Project view pane is null"); //NON-NLS
                        }
                    }
                    try {
                        EditorWindow[] editorWindows = FileEditorManagerEx.getInstanceEx(project).getWindows();
                        for (EditorWindow editorWindow : editorWindows) {
                            try {

                                editorWindow.getManager().refreshIcons();
                                //IJUtils.runInBGT("refresh EditorWindow icons", () -> editorWindow.getManager().refreshIcons(), true); //NON-NLS

                            } catch (Exception e) {
                                LOGGER.warn("Failed to refresh editor tabs icon (EditorWindow manager failed to refresh icons)", e); //NON-NLS
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.warn("Failed to refresh editor tabs icon (can't get FileEditorManagerEx instance or project's windows)", e); //NON-NLS
                    }
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.warn("Project view is null"); //NON-NLS
                    }
                }
            }
        });
    }
}
