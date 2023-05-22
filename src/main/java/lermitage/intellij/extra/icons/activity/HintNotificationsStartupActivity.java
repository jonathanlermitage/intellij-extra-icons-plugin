// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.activity;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import lermitage.intellij.extra.icons.Globals;
import lermitage.intellij.extra.icons.cfg.SettingsForm;
import lermitage.intellij.extra.icons.cfg.services.SettingsService;
import lermitage.intellij.extra.icons.utils.I18nUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

/**
 * Display some useful hints in notifications on startup, a single time only.
 */
public class HintNotificationsStartupActivity implements ProjectActivity {

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        SettingsService ideSettings = SettingsService.getIDEInstance();
        try {
            if (!ideSettings.getPluginIsConfigurableHintNotifDisplayed()) {
                Notification notif = new Notification(Globals.PLUGIN_GROUP_DISPLAY_ID,
                    i18n.getString("notif.tips.plugin.config.title"),
                    i18n.getString("notif.tips.plugin.config.content"),
                    NotificationType.INFORMATION);
                notif.addAction(new NotificationAction(i18n.getString("notif.tips.plugin.config.btn")) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
                        ShowSettingsUtil.getInstance().showSettingsDialog(project, SettingsForm.class);
                    }
                });
                Notifications.Bus.notify(notif);
            }
        } finally {
            ideSettings.setPluginIsConfigurableHintNotifDisplayed(true);
        }
        return null;
    }
}
