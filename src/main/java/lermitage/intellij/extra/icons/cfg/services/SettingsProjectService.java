// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.services;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

// see http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html
@State(
    name = "ExtraIconsSettingsProject"
)
public class SettingsProjectService extends SettingsService implements PersistentStateComponent<SettingsProjectService> {

    @SuppressWarnings("WeakerAccess")
    public boolean overrideIDESettings = false;
    @SuppressWarnings("WeakerAccess")
    public boolean addToIDEUserIcons = true;

    @NotNull
    public static SettingsProjectService getInstance(Project project) {
        return project.getService(SettingsProjectService.class);
    }

    public boolean isOverrideIDESettings() {
        return overrideIDESettings;
    }

    public void setOverrideIDESettings(boolean overrideIDESettings) {
        this.overrideIDESettings = overrideIDESettings;
    }

    public boolean isAddToIDEUserIcons() {
        return addToIDEUserIcons;
    }

    public void setAddToIDEUserIcons(boolean addToIDEUserIcons) {
        this.addToIDEUserIcons = addToIDEUserIcons;
    }

    @Override
    public SettingsProjectService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsProjectService state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
