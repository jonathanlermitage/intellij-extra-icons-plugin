package lermitage.intellij.extra.icons.cfg.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import org.jetbrains.annotations.NotNull;

// see http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html
@State(
    name = "ExtraIconsSettingsProject"
)
public class SettingsProjectService extends SettingsService implements PersistentStateComponent<SettingsProjectService> {

    public boolean overrideIDESettings = false;

    public void setOverrideIDESettings(boolean overrideIDESettings) {
        this.overrideIDESettings = overrideIDESettings;
    }

    public boolean isOverrideIDESettings() {
        return overrideIDESettings;
    }

    @Override
    public SettingsProjectService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsProjectService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @NotNull
    public static SettingsProjectService getInstance(Project project) {
        return ServiceManager.getService(project, SettingsProjectService.class);
    }
}
