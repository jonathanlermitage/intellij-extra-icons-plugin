// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.util.ui.EDT;
import org.jetbrains.annotations.NonNls;

public class IJUtils {

    private static final @NonNls Logger LOGGER = Logger.getInstance(IJUtils.class);

    /**
     * Indicate if plugin <a href="https://github.com/jonathanlermitage/IconViewer">lermitage.intellij.iconviewer</a>
     * is installed and enabled.
     */
    public static boolean isIconViewer2Loaded() {
        try {
            PluginId id = PluginId.findId("lermitage.intellij.iconviewer");
            if (id == null) {
                return false;
            }
            IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(id);
            if (plugin == null) {
                return false;
            }
            return plugin.isEnabled();
        } catch (Exception e) {
            LOGGER.warn("Can't determine if plugin 'lermitage.intellij.iconviewer' is installed and enabled", e);
            return false;
        }
    }

    /**
     * Run given Runnable in EDT.
     * @param description description of what to run.
     * @param r what to run in EDT.
     */
    public static void runInEDT(String description, Runnable r) {
        if (EDT.isCurrentThreadEdt()) {
            r.run();
        } else {
            LOGGER.info("Enter temporarily in EDT in order to run: '" + description + "'");
            ApplicationManager.getApplication().invokeLater(r, ModalityState.defaultModalityState());
        }
    }

    /**
     * Run given Runnable in BGT (i.e. outside EDT).
     * @param description description of what to run.
     * @param r what to run in BGT.
     * @param isReadAction is explicitly a Read Action.
     */
    public static void runInBGT(String description, Runnable r, boolean isReadAction) {
        if (EDT.isCurrentThreadEdt()) {
            LOGGER.info("Enter temporarily in BGT in order to run: '" + description + "'");
            ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
                @Override
                public void run() {
                    if (isReadAction) {
                        ApplicationManager.getApplication().runReadAction(r);
                    } else {
                        ApplicationManager.getApplication().invokeLater(r);
                    }
                }
            });
        } else {
            r.run();
        }
    }
}
