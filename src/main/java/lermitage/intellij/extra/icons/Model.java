// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.util.xmlb.annotations.OptionTag;
import com.intellij.util.xmlb.annotations.XCollection;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.*;

@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType"})
public class Model {

    @OptionTag
    private String id;
    @OptionTag
    private String icon;
    @OptionTag
    private String description;
    @OptionTag
    private ModelType modelType;
    @OptionTag
    private IconType iconType;
    @OptionTag
    private boolean enabled = true;
    private Icon intelliJIcon;
    @XCollection
    private List<ModelCondition> conditions = new ArrayList<>(Collections.singletonList(new ModelCondition()));

    @NotNull
    @Contract("_, _, _ -> new")
    public static Model ofFile(String id, String icon, String description) {
        return new Model(id, icon, description, ModelType.FILE, IconType.PATH);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static Model ofFile(String id, Icon icon, String description) {
        return new Model(id, icon, description, ModelType.FILE);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static Model ofDir(String id, String icon, String description) {
        return new Model(id, icon, description, ModelType.DIR, IconType.PATH);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static Model ofDir(String id, Icon icon, String description) {
        return new Model(id, icon, description, ModelType.DIR);
    }

    // For XML deserializer
    private Model() {

    }

    public Model(String id, String icon, String description, ModelType modelType, IconType iconType) {
        this.id = id;
        this.icon = icon;
        this.description = description;
        this.modelType = modelType;
        this.iconType = iconType;
    }

    public Model(String id, String icon, String description, ModelType modelType, IconType iconType, List<ModelCondition> conditions) {
        this(id, icon, description, modelType, iconType);
        this.conditions = conditions;
    }

    public Model(String id, Icon icon, String description, ModelType modelType) {
        this(id, null, description, modelType, IconType.ICON);
        this.intelliJIcon = icon;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Model parents(String... parents) {
        getCurrentCondition().setParents(parents);
        return this;
    }

    public Model start(String... base) {
        getCurrentCondition().setStart(base);
        return this;
    }

    public Model eq(String... base) {
        getCurrentCondition().setEq(base);
        return this;
    }

    public Model mayEnd(String... extensions) {
        getCurrentCondition().setMayEnd(extensions);
        return this;
    }

    public Model end(String... extensions) {
        getCurrentCondition().setEnd(extensions);
        return this;
    }

    public Model noDot() {
        getCurrentCondition().setNoDot();
        return this;
    }

    public Model regex(@Language("RegExp") String regex) {
        getCurrentCondition().setRegex(regex);
        return this;
    }

    public Model facets(String... facets) {
        getCurrentCondition().setFacets(facets);
        return this;
    }

    public Model or() {
        this.conditions.add(new ModelCondition());
        return this;
    }

    private ModelCondition getCurrentCondition() {
        return conditions.get(conditions.size() - 1);
    }

    public boolean check(String parentName, String fileName, Optional<String> fullPath, Set<String> facets) {
        for (ModelCondition condition : conditions) {
            if (condition.check(parentName, fileName, fullPath, facets)) {
                return true;
            }
        }
        return false;
    }

    public IconType getIconType() {
        return iconType;
    }

    public List<ModelCondition> getConditions() {
        return conditions;
    }

    public Icon getIntelliJIcon() {
        return intelliJIcon;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return enabled == model.enabled &&
            Objects.equals(id, model.id) &&
            Objects.equals(icon, model.icon) &&
            description.equals(model.description) &&
            modelType == model.modelType &&
            iconType == model.iconType &&
            Objects.equals(intelliJIcon, model.intelliJIcon) &&
            conditions.equals(model.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, icon, description, modelType, iconType, enabled, intelliJIcon, conditions);
    }
}
