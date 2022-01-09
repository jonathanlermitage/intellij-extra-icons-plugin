// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import org.jetbrains.annotations.Contract;

public enum ModelType {
    FILE("File"),
    DIR("Directory"),
    ICON("Icon (IDE restart is required to see changes)");


    private final String friendlyName;

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
        return friendlyName;
    }

    public static ModelType getByFriendlyName(String friendlyName) {
        for (ModelType modelType : ModelType.values()) {
            if (modelType.friendlyName.equals(friendlyName)) return modelType;
        }
        return null;
    }
}
