// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import lermitage.intellij.extra.icons.utils.I18nUtils;
import org.jetbrains.annotations.Contract;

import java.util.ResourceBundle;

public enum ModelType {
    FILE,
    DIR,
    ICON;

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    /**
     * {@link ModelType} comparator: {@link ModelType#DIR} &gt; {@link ModelType#FILE} > {@link ModelType#ICON}.
     */
    @Contract(pure = true)
    public static int compare(ModelType o1, ModelType o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return 1;
        }
        if (o2 == null) {
            return -1;
        }
        if (o1 == DIR && (o2 == FILE || o2 == ICON)) {
            return -1;
        }
        if (o1 == FILE && o2 == ICON) {
            return -1;
        }
        return 1;
    }

    public String getI18nFriendlyName() {
        switch (this) {
            case FILE:
                return i18n.getString("model.type.file");
            case DIR:
                return i18n.getString("model.type.directory");
            case ICON:
                return i18n.getString("model.type.icon");
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
