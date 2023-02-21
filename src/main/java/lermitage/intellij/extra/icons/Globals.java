// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import lermitage.intellij.extra.icons.utils.I18nUtils;

import java.util.ResourceBundle;

public class Globals {

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    public static final String PLUGIN_GROUP_DISPLAY_ID = "ExtraIcons";
    public static final String PLUGIN_NAME = i18n.getString("extra.icons.plugin");
}
