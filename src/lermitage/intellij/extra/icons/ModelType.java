package lermitage.intellij.extra.icons;

import org.jetbrains.annotations.Contract;

public enum ModelType {
    FILE,
    DIR;
    
    /**
     * {@link ModelType} comparator: {@link ModelType#DIR} before {@link ModelType#FILE}.
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
        if (o1 == DIR && o2 == FILE) {
            return -1;
        }
        return 1;
    }
}
