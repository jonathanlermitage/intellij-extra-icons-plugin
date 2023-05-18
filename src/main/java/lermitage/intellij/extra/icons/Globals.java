// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

public class Globals {

    /** Reflects <code>notificationGroup id</code> in <code>META-INF/settings.xml</code>. */
    public static final String PLUGIN_GROUP_DISPLAY_ID = "ExtraIcons";

    // Icons can be SVG or PNG only. Never allow user to pick GIF, JPEG, etc., otherwise
    // we should convert these files to PNG in IconUtils:toBase64 method.
    public static final String[] ALLOWED_ICON_FILE_EXTENSIONS = new String[]{"svg", "png"}; //NON-NLS
    public static final String ALLOWED_ICON_FILE_EXTENSIONS_FILE_SELECTOR_LABEL = "*.svg, *.png"; //NON-NLS
}
