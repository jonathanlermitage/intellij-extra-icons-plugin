// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.messaging;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

@Service
public final class RefreshIconsNotifierService {

    private final Logger LOGGER = Logger.getInstance(RefreshIconsNotifierService.class);

    public void triggerProjectIconsRefresh(@Nullable Project project) {
        if (project != null) {
            RefreshIconsNotifier refreshIconsNotifier = ApplicationManager.getApplication()
                .getMessageBus()
                .syncPublisher(RefreshIconsNotifier.EXTRA_ICONS_REFRESH_ICONS_NOTIFIER_TOPIC);
            refreshIconsNotifier.refreshProjectIcons(project);
        } else {
            LOGGER.warn("Project is null, can't refresh icons"); //NON-NLS
        }
    }

    public void triggerAllIconsRefresh() {
        RefreshIconsNotifier refreshIconsNotifier = ApplicationManager.getApplication()
            .getMessageBus()
            .syncPublisher(RefreshIconsNotifier.EXTRA_ICONS_REFRESH_ICONS_NOTIFIER_TOPIC);
        refreshIconsNotifier.refreshAllProjectsIcons();
    }

    public static RefreshIconsNotifierService getInstance() {
        return ApplicationManager.getApplication().getService(RefreshIconsNotifierService.class);
    }
}
