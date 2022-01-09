// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.IconPathPatcher;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExtraIconPatcher extends IconPathPatcher {

    private final Logger LOG = Logger.getInstance(getClass().getName());

    private Map<String, String> icons;

    @NotNull
    public static Map<String, String> getEnabledIcons() {
        Map<String, String> enabledIcons = new LinkedHashMap<>();
        List<String> disabledModelIds = SettingsService.getIDEInstance().getDisabledModelIds();
        ExtraIconProvider.allModels()
            .stream()
            .filter(model -> model.getModelType() == ModelType.ICON)
            .forEach(model -> {
                if (!enabledIcons.containsKey(model.getIdeIcon()) && !disabledModelIds.contains(model.getId())) {
                    enabledIcons.put(model.getIdeIcon(), model.getIcon());
                }
            });
        return enabledIcons;
    }

    public ExtraIconPatcher() {
        super();
        loadConfig();
        IconLoader.installPathPatcher(this);
    }

    @Override
    public @Nullable ClassLoader getContextClassLoader(@NotNull String path, @Nullable ClassLoader originalClassLoader) {
        return ExtraIconPatcher.class.getClassLoader();
    }

    @Override
    public @Nullable String patchPath(@NotNull String path, @Nullable ClassLoader classLoader) {
        String iconOriginalPath = (new File(path)).getName();
        if (icons == null) {
            loadConfig();
        }
        return this.icons.get(iconOriginalPath);
    }

    private void loadConfig() {
        icons = getEnabledIcons();
        LOG.info("config loaded with success, enabled " + icons.size() + " items");
    }
}