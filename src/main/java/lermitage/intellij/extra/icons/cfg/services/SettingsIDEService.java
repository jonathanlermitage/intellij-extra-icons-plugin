// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lermitage.intellij.extra.icons.UITypeIconsPreference;
import org.jetbrains.annotations.NotNull;

// see http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html
@State(
    name = "ExtraIconsSettings",
    storages = @Storage("lermitage-extra-icons.xml")
)
public class SettingsIDEService extends SettingsService implements PersistentStateComponent<SettingsIDEService> {

    @SuppressWarnings("WeakerAccess")
    public Double additionalUIScale2; // renamed to 2 because fixed default user interface scale calculation, so a reset is needed
    @SuppressWarnings("WeakerAccess")
    public UITypeIconsPreference uiTypeIconsPreference;
    @SuppressWarnings("WeakerAccess")
    public Boolean useIDEFilenameIndex;
    @SuppressWarnings("WeakerAccess")
    public Boolean pluginIsConfigurableHintNotifDisplayed;

    public static SettingsIDEService getInstance() {
        return ApplicationManager.getApplication().getService(SettingsIDEService.class);
    }

    @Override
    public SettingsIDEService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsIDEService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public Double getAdditionalUIScale2() {
        if (additionalUIScale2 == null) {
            additionalUIScale2 = DEFAULT_ADDITIONAL_UI_SCALE;
        }
        return additionalUIScale2;
    }

    public void setAdditionalUIScale2(Double additionalUIScale2) {
        this.additionalUIScale2 = additionalUIScale2;
    }

    public UITypeIconsPreference getUiTypeIconsPreference() {
        if (uiTypeIconsPreference == null) {
            uiTypeIconsPreference = UITypeIconsPreference.BASED_ON_ACTIVE_UI_TYPE;
        }
        return uiTypeIconsPreference;
    }

    public void setUiTypeIconsPreference(UITypeIconsPreference uiTypeIconsPreference) {
        this.uiTypeIconsPreference = uiTypeIconsPreference;
    }

    public Boolean getUseIDEFilenameIndex() {
        if (useIDEFilenameIndex == null) {
            useIDEFilenameIndex = true;
        }
        return useIDEFilenameIndex;
    }

    public void setUseIDEFilenameIndex(Boolean useIDEFilenameIndex) {
        this.useIDEFilenameIndex = useIDEFilenameIndex;
    }

    public Boolean getPluginIsConfigurableHintNotifDisplayed() {
        if (pluginIsConfigurableHintNotifDisplayed == null) {
            pluginIsConfigurableHintNotifDisplayed = false;
        }
        return pluginIsConfigurableHintNotifDisplayed;
    }

    public void setPluginIsConfigurableHintNotifDisplayed(Boolean pluginIsConfigurableHintNotifDisplayed) {
        this.pluginIsConfigurableHintNotifDisplayed = pluginIsConfigurableHintNotifDisplayed;
    }
}
