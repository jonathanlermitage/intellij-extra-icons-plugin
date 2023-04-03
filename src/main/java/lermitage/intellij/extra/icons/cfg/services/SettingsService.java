// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.services;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.ExtraIconProvider;
import lermitage.intellij.extra.icons.Globals;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.utils.I18nUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @SuppressWarnings("WeakerAccess")
    public Double additionalUIScale;
    @SuppressWarnings("WeakerAccess")
    public Boolean ignoreWarnings;

    private Pattern ignoredPatternObj;
    private Boolean isIgnoredPatternValid;

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    public static final double DEFAULT_ADDITIONAL_UI_SCALE = 1.0d;

    public List<String> getDisabledModelIds() {
        if (disabledModelIds == null) { // a malformed xml file could make it null
            disabledModelIds = new ArrayList<>();
        }
        return disabledModelIds;
    }

    public String getIgnoredPattern() {
        return ignoredPattern;
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

    public Double getAdditionalUIScale() {
        if (additionalUIScale == null) {
            additionalUIScale = DEFAULT_ADDITIONAL_UI_SCALE;
        }
        return additionalUIScale;
    }

    public void setAdditionalUIScale(Double additionalUIScale) {
        this.additionalUIScale = additionalUIScale;
    }

    public Boolean getIgnoreWarnings() {
        if (ignoreWarnings == null) {
            ignoreWarnings = false;
        }
        return ignoreWarnings;
    }

    public void setIgnoreWarnings(Boolean ignoreWarnings) {
        this.ignoreWarnings = ignoreWarnings;
    }

    @NotNull
    public static List<Model> getAllRegisteredModels() {
        return ExtraIconProvider.allModels();
    }

    /** Get project-level settings service, or global-level settings service if project is null. */
    @NotNull
    public static SettingsService getInstance(@Nullable Project project) {
        if (project == null) {
            return getIDEInstance();
        }
        return SettingsProjectService.getInstance(project);
    }

    /** Get global-level settings service. */
    @NotNull
    public static SettingsService getIDEInstance() {
        return SettingsIDEService.getInstance();
    }

    private void compileAndSetRegex(String regex) {
        if (regex != null && !regex.isEmpty()) {
            try {
                ignoredPatternObj = Pattern.compile(regex);
                isIgnoredPatternValid = true;
            } catch (PatternSyntaxException e) {
                NotificationGroupManager.getInstance().getNotificationGroup(Globals.PLUGIN_GROUP_DISPLAY_ID)
                    .createNotification(MessageFormat.format(i18n.getString("notification.content.cant.compile.regex"), regex,e.getMessage()),
                        NotificationType.WARNING)
                    .setTitle(MessageFormat.format(i18n.getString("notification.content.cant.compile.regex.title"), Globals.PLUGIN_NAME))
                    .setSubtitle(i18n.getString("notification.content.cant.compile.regex.subtitle"))
                    .setImportant(true)
                    .notify(null);
                ignoredPatternObj = null;
                isIgnoredPatternValid = false;
            }
        }
    }
}
