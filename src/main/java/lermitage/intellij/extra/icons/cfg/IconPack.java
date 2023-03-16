// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg;

import lermitage.intellij.extra.icons.Model;

import java.util.List;

/**
 * A downloadable and exportable list of {@link Model} objects (icons and conditions).
 */
public class IconPack {

    /**
     * Icon Pack's name, used to identify the icon pack in user icons table.
     */
    private String name;

    /**
     * List of models.
     */
    private List<Model> models;

    public IconPack(String name, List<Model> models) {
        this.name = name;
        this.models = models;
    }

    @SuppressWarnings("unused")
    public IconPack() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }
}
