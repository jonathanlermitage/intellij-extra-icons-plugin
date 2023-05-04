// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import lermitage.intellij.extra.icons.utils.I18nUtils;

import java.util.ResourceBundle;

public class Globals {

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    public static final String PLUGIN_GROUP_DISPLAY_ID = "ExtraIcons";
    public static final String PLUGIN_NAME = i18n.getString("extra.icons.plugin");

    // Icons can be SVG or PNG only. Never allow user to pick GIF, JPEG, etc., otherwise
    // we should convert these files to PNG in IconUtils:toBase64 method.
    public static final String[] ALLOWED_ICON_FILE_EXTENSIONS = new String[]{"svg", "png"}; //NON-NLS
    public static final String ALLOWED_ICON_FILE_EXTENSIONS_FILE_SELECTOR_LABEL = "*.svg, *.png"; //NON-NLS
}
