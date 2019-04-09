package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lermitage.intellij.extra.icons.ExtraIconProvider;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.providers.Angular2IconProvider;
import lermitage.intellij.extra.icons.providers.JavascriptIconProvider;
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
        if (disabledModelIds == null) { // a malformed xml file could make it null
            disabledModelIds = new ArrayList<>();
        }
        return disabledModelIds;
    }
    
    static void setDisabledModelIds(List<String> disabledModelIds) {
        ServiceManager.getService(SettingsService.class).disabledModelIds = disabledModelIds;
    }
    
    @NotNull
    static List<Model> getAllRegisteredModels() {
        List<Model> allModels = new ArrayList<>();
        allModels.addAll(ExtraIconProvider.allModels());
        allModels.addAll(Angular2IconProvider.allModels());
        allModels.addAll(JavascriptIconProvider.allModels());
        return allModels;
    }
    
    @SuppressWarnings("WeakerAccess") // the implementation of PersistentStateComponent works by serializing public fields, so keep it public
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
