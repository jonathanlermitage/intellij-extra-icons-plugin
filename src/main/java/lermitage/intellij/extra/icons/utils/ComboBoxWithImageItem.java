// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import lermitage.intellij.extra.icons.ModelTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@link ComboBoxWithImageRenderer} item: PNG or SVG image + text.
 */
public class ComboBoxWithImageItem {

    private final String title;
    private final String imagePath;

    public ComboBoxWithImageItem(String title) {
        this.title = title;
        this.imagePath = null;
    }

    public ComboBoxWithImageItem(@NotNull ModelTag modelTag, String title) {
        this.title = title;
        this.imagePath = modelTag.getIcon();
    }

    public ComboBoxWithImageItem(@NotNull String imagePath, String title) {
        this.title = title;
        this.imagePath = imagePath;
    }

    /** item's text */
    public String getTitle() {
        return title;
    }

    /** item's image path relative to 'resources' folder, without leading {@code /}. Example: {@code extra-icons/image.svg} */
    public @Nullable String getImagePath() {
        return imagePath;
    }
}
