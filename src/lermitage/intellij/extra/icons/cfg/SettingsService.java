package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.ExtraIconProvider;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.cfg.settings.SettingsIDEService;
import lermitage.intellij.extra.icons.cfg.settings.SettingsProjectService;
import lermitage.intellij.extra.icons.providers.Angular2IconProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class SettingsService {

    @SuppressWarnings("WeakerAccess") // the implementation of PersistentStateComponent works by serializing public fields, so keep it public
    public List<String> disabledModelIds = new ArrayList<>();

    public List<String> getDisabledModelIds() {
        if (disabledModelIds == null) { // a malformed xml file could make it null
            disabledModelIds = new ArrayList<>();
        }
        return disabledModelIds;
    }

    public void setDisabledModelIds(List<String> disabledModelIds) {
        this.disabledModelIds = disabledModelIds;
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
}
