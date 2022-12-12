// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

// see http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html
@State(
    name = "ExtraIconsSettings",
    storages = @Storage("lermitage-extra-icons.xml")
)
public class SettingsIDEService extends SettingsService implements PersistentStateComponent<SettingsIDEService> {

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
}
