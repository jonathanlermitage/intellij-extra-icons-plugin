package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// see http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html
@State(
        name = "ExtraIconsSettings",
        storages = @Storage("lermitage-extra-icons.xml")
)
public class SettingsService implements PersistentStateComponent<SettingsService> {
    
    public static List<String> getDisabledModelIds() {
        List<String> disabledModelIds = ServiceManager.getService(SettingsService.class).disabledModelIds;
        if (disabledModelIds == null) { // a malformed xml file could make disabledModelIds null
            disabledModelIds = new ArrayList<>();
        }
        return disabledModelIds;
    }
    
    @SuppressWarnings("WeakerAccess") // the implementation of PersistentStateComponent works by serializing public fields
    public List<String> disabledModelIds = new ArrayList<>();
    
    @Override
    public SettingsService getState() {
        return this;
    }
    
    @Override
    public void loadState(@NotNull SettingsService state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
