package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;

public enum OS {
    WIN,
    LINUX,
    MACOS;

    private static OS detectedOS;

    @NotNull
    public static OS detectOS() {
        if (detectedOS == null) {
            if (SystemInfo.isWindows) {
                detectedOS = WIN;
            } else if (SystemInfo.isMac) {
                detectedOS = MACOS;
            } else {
                detectedOS = LINUX;
            }
        }
        return detectedOS;
    }
}
