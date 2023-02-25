// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

/** An icon that comes with Extra Icons plugin. */
public class BundledIcon {

    private final String iconPath;
    private final String description;

    public BundledIcon(String iconPath, String description) {
        this.iconPath = iconPath;
        this.description = description;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getDescription() {
        return description;
    }
}
