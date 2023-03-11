// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

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
            if (IDEUtils.isChineseUIEnabled()) {
                LOGGER.info("Found Chinese Language Pack - Enabling Chinese translation of Extra Icons plugin");
                pluginLocale = Locale.CHINA;
            }
        }
        return ResourceBundle.getBundle("ExtraIconsI18n", pluginLocale);
    }
}
