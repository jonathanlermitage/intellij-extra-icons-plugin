// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.messaging;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.Nullable;

@Service
public final class RefreshIconsNotifierService {

    private final Logger LOGGER = Logger.getInstance(RefreshIconsNotifierService.class);

    public void triggerProjectIconsRefresh(@Nullable Project project) {
        ApplicationManager.getApplication().runReadAction(() -> {
            if (ProjectUtils.isProjectAlive(project)) {
                assert project != null;
                RefreshIconsNotifier refreshIconsNotifier = project.getMessageBus()
                    .syncPublisher(RefreshIconsNotifier.EXTRA_ICONS_REFRESH_ICONS_NOTIFIER_TOPIC);
                refreshIconsNotifier.refreshProjectIcons(project);
            } else {
                LOGGER.warn("Project is not alive, can't refresh icons"); //NON-NLS
            }
        });
    }

    public void triggerProjectIconEnablersReinit(@Nullable Project project) {
        ApplicationManager.getApplication().runReadAction(() -> {
            if (ProjectUtils.isProjectAlive(project)) {
                assert project != null;
                RefreshIconsNotifier refreshIconsNotifier = project.getMessageBus()
                    .syncPublisher(RefreshIconsNotifier.EXTRA_ICONS_REFRESH_ICONS_NOTIFIER_TOPIC);
                refreshIconsNotifier.reinitProjectIconEnablers(project);
            } else {
                LOGGER.warn("Project is not alive, can't reinit icon enablers"); //NON-NLS
            }
        });
    }

    public void triggerAllIconsRefreshAndIconEnablersReinit() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            triggerProjectIconEnablersReinit(project);
            triggerProjectIconsRefresh(project);
        }
    }

    public static RefreshIconsNotifierService getInstance() {
        return ApplicationManager.getApplication().getService(RefreshIconsNotifierService.class);
    }
}
