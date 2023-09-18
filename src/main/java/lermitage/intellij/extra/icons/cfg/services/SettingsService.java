// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.services;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.scale.JBUIScale;
import lermitage.intellij.extra.icons.ExtraIconProvider;
import lermitage.intellij.extra.icons.Globals;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.utils.I18nUtils;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class SettingsService {

    // the implementation of PersistentStateComponent works by serializing public fields, so keep them public
    @SuppressWarnings("WeakerAccess")
    public List<String> disabledModelIds = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    public String ignoredPattern;
    @SuppressWarnings("WeakerAccess")
    public List<Model> customModels = new ArrayList<>();

    private Pattern ignoredPatternObj;
    private Boolean isIgnoredPatternValid;

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    public static final double DEFAULT_ADDITIONAL_UI_SCALE = JBUIScale.sysScale();

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
        if (isIgnoredPatternValid == null) {
            compileAndSetRegex(ignoredPattern);
        }
        if (isIgnoredPatternValid == Boolean.TRUE) {
            return ignoredPatternObj;
        }
        return null;
    }

    public void setDisabledModelIds(List<String> disabledModelIds) {
        this.disabledModelIds = disabledModelIds;
    }

    public void setIgnoredPattern(String ignoredPattern) {
        this.ignoredPattern = ignoredPattern;
        compileAndSetRegex(ignoredPattern);
    }

    public List<Model> getCustomModels() {
        if (customModels == null) { // a malformed xml file could make it null
            customModels = new ArrayList<>();
        }
        return customModels;
    }

    public void setCustomModels(List<Model> customModels) {
        this.customModels = customModels;
    }

    @NotNull
    public static List<Model> getAllRegisteredModels() {
        return ExtraIconProvider.allModels();
    }

    /**
     * Returns the Project settings service if the project is not null and, optionally, if the checkbox in the project settings was checked,
     * otherwise returns the IDE settings service.
     */
    @NotNull
    public static SettingsService getBestSettingsService(Project project, boolean isOverrideIDESettings) {
        if (project != null) {
            SettingsProjectService settingsProjectService = SettingsProjectService.getInstance(project);
            if (settingsProjectService.isOverrideIDESettings() || !isOverrideIDESettings) {
                return settingsProjectService;
            }
        }
        return SettingsIDEService.getInstance();
    }

    private void compileAndSetRegex(String regex) {
        if (regex != null && !regex.isEmpty()) {
            try {
                ignoredPatternObj = Pattern.compile(regex);
                isIgnoredPatternValid = true;
            } catch (PatternSyntaxException e) {
                NotificationGroupManager.getInstance().getNotificationGroup(Globals.PLUGIN_GROUP_DISPLAY_ID)
                    .createNotification(
                        MessageFormat.format(
                            i18n.getString("notification.content.cant.compile.regex"),
                            regex,
                            e.getMessage()),
                        NotificationType.WARNING)
                    .setTitle(
                        MessageFormat.format(
                            i18n.getString("notification.content.cant.compile.regex.title"),
                            i18n.getString("extra.icons.plugin")))
                    .setSubtitle(i18n.getString("notification.content.cant.compile.regex.subtitle"))
                    .setImportant(true)
                    .notify(null);
                ignoredPatternObj = null;
                isIgnoredPatternValid = false;
            }
        }
    }
}
