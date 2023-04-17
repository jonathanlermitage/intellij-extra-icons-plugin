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

    private static Boolean isChineseUIEnabled;
    private static Locale pluginLocale = null;

    public static synchronized ResourceBundle getResourceBundle() {
        if (pluginLocale == null) {
            pluginLocale = Locale.ROOT;
            if (isChineseUIEnabled()) {
                LOGGER.info("Found Chinese Language Pack - Enabling Chinese translation of Extra Icons plugin");
                pluginLocale = Locale.CHINA;
            } else {
                try {
                    if (System.getProperty("extra-icons.enable.chinese.ui", "false").equalsIgnoreCase("true")) { //NON-NLS
                        LOGGER.info("Found extra-icons.enable.chinese.ui=true - Force usage of Chinese translation of Extra Icons plugin");
                        pluginLocale = Locale.CHINA;
                    }
                } catch (Exception e) {
                    LOGGER.info(e);
                }
            }
        }
        return ResourceBundle.getBundle("ExtraIconsI18n", pluginLocale);
    }

    public static synchronized boolean isChineseUIEnabled() {
        if (isChineseUIEnabled != null) {
            return isChineseUIEnabled;
        }
        try {
            for (IdeaPluginDescriptor plugin : PluginManager.getLoadedPlugins()) {
                if (plugin.getPluginId().getIdString().equals("com.intellij.zh")) {
                    isChineseUIEnabled = true;
                    return true;
                }
            }
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e);
            }
        }
        isChineseUIEnabled = false;
        return false;
    }
}
