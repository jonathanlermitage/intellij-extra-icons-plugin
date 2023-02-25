// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NonNls;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nUtils {

    private static final @NonNls Logger LOGGER = Logger.getInstance(I18nUtils.class);

    private static Locale pluginLocale = null;

    public static synchronized ResourceBundle getResourceBundle() {
        if (pluginLocale == null) {
            pluginLocale = Locale.ROOT;
            try {
                for (IdeaPluginDescriptor plugin : PluginManager.getLoadedPlugins()) {
                    if (plugin.getPluginId().getIdString().equals("com.intellij.zh")) {
                        LOGGER.info("Found Chinese Language Pack - Enabling Chinese translation of Extra Icons plugin");
                        pluginLocale = Locale.CHINA;
                        break;
                    }
                }
            } catch (Exception e) {
                LOGGER.warn(e);
            }
        }
        return ResourceBundle.getBundle("ExtraIconsI18n", pluginLocale);
    }
}
