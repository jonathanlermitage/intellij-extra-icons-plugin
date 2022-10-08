// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.indexing.UnindexedFilesUpdaterListener;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import lermitage.intellij.extra.icons.enablers.EnablerUtils;
import lermitage.intellij.extra.icons.utils.AsyncUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO deprecated external plugins in most cases should use com.intellij.openapi.project.DumbService.DumbModeListener
//  https://github.com/JetBrains/intellij-community/blob/master/platform/platform-impl/src/com/intellij/openapi/project/DumbServiceImpl.java
public class IndexListener implements UnindexedFilesUpdaterListener {

    private static final Logger LOGGER = Logger.getInstance(IndexListener.class);

    private static final Map<String, Boolean> reloadDoneByProject = new ConcurrentHashMap<>();

    @Override
    public void updateStarted(@NotNull Project project) {
    }

    @Override
    public void updateFinished(@NotNull Project project) {
        if (!reloadDoneByProject.containsKey(project.getLocationHash()) || !reloadDoneByProject.get(project.getLocationHash())) {
            AsyncUtils.invokeReadActionAndWait(() -> {
                try {
                    EnablerUtils.forceInitAllEnablers(project, false);
                    ProjectUtils.refresh(project);
                    LOGGER.info("Index Listener reloaded icons for project " + project.getName());
                    reloadDoneByProject.put(project.getLocationHash(), true);
                } catch (Exception e) {
                    LOGGER.warn("Index Listener failed to reload icons for project " + project.getName(), e);
                    if (!SettingsService.getIDEInstance().getIgnoreWarnings()) {
                        Notification notification = NotificationGroupManager.getInstance()
                            .getNotificationGroup(Globals.PLUGIN_GROUP_DISPLAY_ID)
                            .createNotification("Index Listener failed to reload icons after indexing.", NotificationType.WARNING)
                            .setTitle(Globals.PLUGIN_NAME)
                            .setImportant(false);
                        notification.hideBalloon();
                        notification.notify(project);
                    }
                }
            });
        }
    }
}
