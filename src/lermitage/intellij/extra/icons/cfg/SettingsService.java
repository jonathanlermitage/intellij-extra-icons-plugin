package lermitage.intellij.extra.icons.cfg;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.ExtraIconProvider;
import lermitage.intellij.extra.icons.Globals;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.cfg.settings.SettingsIDEService;
import lermitage.intellij.extra.icons.cfg.settings.SettingsProjectService;
import lermitage.intellij.extra.icons.providers.Angular2IconProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class SettingsService {

    private static final Logger LOGGER = Logger.getInstance(SettingsService.class);

    // the implementation of PersistentStateComponent works by serializing public fields, so keep them public
    @SuppressWarnings("WeakerAccess")
    public List<String> disabledModelIds = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    public String ignoredPattern = "";

    private Pattern ignoredPatternObj;

    public List<String> getDisabledModelIds() {
        if (disabledModelIds == null) { // a malformed xml file could make it null
            disabledModelIds = new ArrayList<>();
        }
        return disabledModelIds;
    }

    public String getIgnoredPattern() {
        return ignoredPattern == null ? "" : ignoredPattern;
    }

    public Pattern getIgnoredPatternObj() {
        if (ignoredPatternObj != null) {
            return ignoredPatternObj;
        }
        if (ignoredPattern == null || ignoredPattern.isEmpty()) {
            return null;
        }
        return ignoredPatternObj = compileRegex(ignoredPattern);
    }

    public void setDisabledModelIds(List<String> disabledModelIds) {
        this.disabledModelIds = disabledModelIds;
    }

    public void setIgnoredPattern(String ignoredPattern) {
        this.ignoredPattern = ignoredPattern;
        this.ignoredPatternObj = compileRegex(ignoredPattern);
    }

    @NotNull
    public static List<Model> getAllRegisteredModels() {
        List<Model> allModels = new ArrayList<>();
        allModels.addAll(ExtraIconProvider.allModels());
        allModels.addAll(Angular2IconProvider.allModels());
        return allModels;
    }

    @NotNull
    public static SettingsService getInstance(@Nullable Project project) {
        if (project == null) {
            return ServiceManager.getService(SettingsIDEService.class);
        }
        return ServiceManager.getService(project, SettingsProjectService.class);
    }

    private Pattern compileRegex(String regex) {
        try {
            return Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            Notification notification = new Notification(
                Globals.PLUGIN_GROUP_DISPLAY_ID,
                Globals.PLUGIN_NAME + " settings",
                "Can't compile regex: '" + regex + "' (" + e.getMessage() + ")",
                NotificationType.WARNING
            );
            notification.setImportant(true);
            Notifications.Bus.notify(notification);
            LOGGER.warn("Can't compile regex '" + regex + "'", e);
            return null;
        }
    }
}
