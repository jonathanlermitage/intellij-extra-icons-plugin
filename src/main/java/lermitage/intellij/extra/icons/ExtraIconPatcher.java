// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.IconPathPatcher;
import lermitage.intellij.extra.icons.cfg.services.SettingsIDEService;
import lermitage.intellij.extra.icons.utils.OS;
import org.jetbrains.annotations.NonNls;
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
import java.util.stream.Stream;

import static lermitage.intellij.extra.icons.utils.Base64Utils.B64_DECODER;

public class ExtraIconPatcher extends IconPathPatcher {

    private static final @NonNls Logger LOGGER = Logger.getInstance(ExtraIconPatcher.class);
    private static final OS detectedOS = OS.detectOS();

    private Map<String, String> icons;

    /** ClassLoader#getResource needs a leading "/" in old IDEs, and it should be absent in modern IDEs (2022+). */
    private final boolean useOldResourceLoaderBehavior;

    @NotNull
    public static Map<String, String> getEnabledIcons() {
        Map<String, String> enabledIcons = new LinkedHashMap<>();
        List<String> disabledModelIds = SettingsIDEService.getInstance().getDisabledModelIds();
        Stream.of(ExtraIconProvider.allModels(), SettingsIDEService.getInstance().getCustomModels()).flatMap(Collection::stream)
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

        // Starting with IJ 2022 (build 221+), resource path must not start with a leading "/". On older IJ versions, resource
        // path has to start with a leading "/". A solution would be to support IJ 2022+ only, but we can easily
        // detect IDE build version and adapt icons path when needed.
        String buildVersion = ApplicationInfo.getInstance().getBuild().asStringWithoutProductCode();
        useOldResourceLoaderBehavior = buildVersion.startsWith("21");
        if (useOldResourceLoaderBehavior) {
            LOGGER.info("Detected IDE build version " + buildVersion + ". This is an old build (<221). " +
                "Will adapt code in order to customize IDE icons correctly.");
        }

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
            icons = convertUserB64IconsToLocalFilesAndKeepBundledIcons(icons);
        } catch (IOException e) {
            LOGGER.warn("Cannot create temporary directory to store user IDE icons, this feature won't work", e);
        }
        LOGGER.info("Config loaded with success, enabled " + icons.size() + " items");
    }

    /**
     * Convert Base64 user icons to local files. This conversion is needed because IconPatcher can't work with
     * in-memory byte arrays; we have to use bundled icons (from /resources) or local files. This method
     * stores in-memory Base64 icons provided by user as temporary local files.
     */
    private Map<String, String> convertUserB64IconsToLocalFilesAndKeepBundledIcons(Map<String, String> icons) throws IOException {
        Map<String, String> morphedIcons = new LinkedHashMap<>();
        for (String iconKey : icons.keySet()) {
            String iconStr = icons.get(iconKey);
            if (iconStr.startsWith("extra-icons/")) { //NON-NLS
                // bundled icon, no icon transformation needed
                if (useOldResourceLoaderBehavior) {
                    morphedIcons.put(iconKey, "/" + iconStr);
                } else {
                    morphedIcons.put(iconKey, iconStr);
                }
            } else {
                // base64 icon provided by user: store as local file
                Path iconFile = Files.createTempFile("extra-icons-ide-user-icon", ".svg"); //NON-NLS
                iconFile.toFile().deleteOnExit();
                Files.write(iconFile, B64_DECODER.decode(iconStr));
                String decodedIconPath = iconFile.toAbsolutePath().toString();
                if (detectedOS == OS.WIN) {
                    morphedIcons.put(iconKey, "file:/" + decodedIconPath); //NON-NLS
                } else {
                    morphedIcons.put(iconKey, "file://" + decodedIconPath); //NON-NLS
                }
            }
        }
        return morphedIcons;
    }
}
