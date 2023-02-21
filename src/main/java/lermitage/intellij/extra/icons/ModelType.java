// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import lermitage.intellij.extra.icons.utils.I18nUtils;
import org.jetbrains.annotations.Contract;

import java.util.ResourceBundle;

public enum ModelType {
    FILE("File"), //NON-NLS
    DIR("Directory"), //NON-NLS
    ICON("Icon (IDE restart is required to see changes)"); //NON-NLS

    private final String friendlyName;

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    ModelType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

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

    public String getFriendlyName() {
        if (friendlyName.equals(FILE.friendlyName)) {
            return i18n.getString("model.type.file");
        }
        if (friendlyName.equals(DIR.friendlyName)) {
            return i18n.getString("model.type.directory");
        }
        if (friendlyName.equals(ICON.friendlyName)) {
            return i18n.getString("model.type.icon");
        }
        return friendlyName;
    }

    public static ModelType getByFriendlyName(String friendlyName) {
        for (ModelType modelType : ModelType.values()) {
            if (modelType.friendlyName.equals(friendlyName)) return modelType;
        }
        return null;
    }
}
