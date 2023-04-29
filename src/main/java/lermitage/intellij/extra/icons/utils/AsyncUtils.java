// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;

public class AsyncUtils {

    /**
     * Execute given runnable via {@code invokeAndWait} + {@code runReadAction} to
     * fix <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/40">#40</a>.
     */
    public static void invokeReadActionAndWait(Runnable runnable) {
        final Application application = ApplicationManager.getApplication();
        application.invokeAndWait(() -> application.runReadAction(runnable), ModalityState.defaultModalityState());
    }
}
