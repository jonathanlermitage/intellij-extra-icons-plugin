// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.diagnostic.Logger;
import lermitage.intellij.extra.icons.cfg.services.SettingsIDEService;

public class LogUtils {

    public static void showErrorIfAllowedByUser(Logger logger, String msg, Throwable e) {
        if (SettingsIDEService.getIDEInstance().getIgnoreWarnings()) {
            logger.warn(msg, e);
        } else {
            logger.error(msg, e);
        }
    }

    public static void throwErrorIfAllowedByUser(Logger logger, String msg, Throwable e) throws Throwable {
        if (SettingsIDEService.getIDEInstance().getIgnoreWarnings()) {
            logger.warn(msg, e);
        } else {
            throw e;
        }
    }
}
