// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.IconPathPatcher;
import lermitage.intellij.extra.icons.cfg.services.impl.SettingsIDEService;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static lermitage.intellij.extra.icons.utils.Base64Utils.B64_DECODER;

public class ExtraIconPatcher extends IconPathPatcher {

    private static final Logger LOGGER = Logger.getInstance(ExtraIconPatcher.class);

    private Map<String, String> icons;


    @NotNull
    public static Map<String, String> getEnabledIcons() {
        Map<String, String> enabledIcons = new LinkedHashMap<>();
        List<String> disabledModelIds = SettingsIDEService.getIDEInstance().getDisabledModelIds();
        Stream.of(ExtraIconProvider.allModels(), SettingsIDEService.getInstance().getCustomModels()).flatMap(Collection::stream)
            .collect(Collectors.toList()).stream()
            .filter(model -> model.getModelType() == ModelType.ICON)
            .forEach(model -> {
                if (model.isEnabled() && !enabledIcons.containsKey(model.getIdeIcon()) && !disabledModelIds.contains(model.getId())) {
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
        if (icons == null) {
            loadConfig();
        }

        if (this.icons.containsKey(path)) {
            return this.icons.get(path);
        }
        if (path.startsWith("/") && path.length() > 2) {
            String simplifiedPath = path.substring(1);
            if (this.icons.containsKey(simplifiedPath)) {
                return this.icons.get(simplifiedPath);
            }
        }

        String fileName = (new File(path)).getName();
        if (this.icons.containsKey(fileName)) {
            return this.icons.get(fileName);
        }

        return null;
    }

    private void loadConfig() {
        icons = getEnabledIcons();
        try {
            icons = convertB64IconsToLocalFiles(icons);
        } catch (IOException e) {
            LOGGER.warn("Cannot create temporary directory to store user IDE icons, this feature won't work", e);
        }
        //icons.put("add.svg", "file://C:\\Projects\\ij-extra-icons\\src\\main\\resources\\extra-icons\\angular2.svg");
        LOGGER.info("config loaded with success, enabled " + icons.size() + " items");
    }

    /**
     * Convert Base64 user icons to local files. This conversion is needed because IconPatcher can't work with
     * in-memory byte arrays; we have to use bundled icons (from /resources) or local files. This method
     * stores in-memory Base64 icons provided by user as temporary local files.
     */
    private Map<String, String> convertB64IconsToLocalFiles(Map<String, String> icons) throws IOException {
        Map<String, String> morphedIcons = new LinkedHashMap<>();
        for (String iconKey : icons.keySet()) {
            String iconStr = icons.get(iconKey);
            if (iconStr.startsWith("/extra-icons/")) {
                // bundled icon, nothing to do
                morphedIcons.put(iconKey, iconStr);
            } else {
                // base64 icon provided by user: store as local file
                Path iconFile = Files.createTempFile("extra-icons-ide-user-icon", ".svg");
                FileUtils.forceDeleteOnExit(iconFile.toFile());
                Files.write(iconFile, B64_DECODER.decode(iconStr));
                String decodedIconPath = iconFile.toAbsolutePath().toString();
                morphedIcons.put(iconKey, "file://" + decodedIconPath);
            }
        }
        return morphedIcons;
    }
}
