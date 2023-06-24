// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.registry.RegistryManager;
import org.jetbrains.annotations.NonNls;

public class UIUtils {

    private static final @NonNls Logger LOGGER = Logger.getInstance(UIUtils.class);

    private static Boolean newUIEnabled; // TODO use NewUI.isEnabled() when 231.8109.90 becomes the new minimal IDE version (currently 231.6890.12)

    public static synchronized boolean isNewUIEnabled() {
        if (newUIEnabled == null) {
            try {
                newUIEnabled = RegistryManager.getInstance().get("ide.experimental.ui").asBoolean();
            } catch (Exception e) {
                // This registry key is experimental, so we can assume it will disappear once the old UI is
                // definitively removed. This is why the default value is NEW_UI.
                newUIEnabled = true;
                LOGGER.warn("Failed to detect IDE's UI type (Old or New), will consider using the New UI");
            }
        }
        return newUIEnabled;
    }
}
