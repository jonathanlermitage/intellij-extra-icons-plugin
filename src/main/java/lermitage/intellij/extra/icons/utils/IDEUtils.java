// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NonNls;

public class IDEUtils {

    private static final @NonNls Logger LOGGER = Logger.getInstance(IDEUtils.class);

    private static Boolean isChineseUIEnabled;

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
