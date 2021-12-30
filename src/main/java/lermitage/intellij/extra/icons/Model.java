// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.annotations.OptionTag;
import com.intellij.util.xmlb.annotations.XCollection;
import lermitage.intellij.extra.icons.enablers.IconEnablerType;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType"})
public class Model {

    @OptionTag
    private String id;
    @OptionTag
    private String ideIcon;
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
    @XCollection
    private List<ModelCondition> conditions = new ArrayList<>(Collections.singletonList(new ModelCondition()));

    @NotNull
    @Contract("_, _, _ -> new")
    public static Model ofFile(String id, String icon, String description) {
        return new Model(id, icon, description, ModelType.FILE, IconType.PATH);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static Model ofDir(String id, String icon, String description) {
        return new Model(id, icon, description, ModelType.DIR, IconType.PATH);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static Model ofIcon(String id, String ideIcon, String icon, String description) {
        return new Model(id, ideIcon, icon, description, ModelType.ICON, IconType.PATH);
    }

    // For XML deserializer
    @SuppressWarnings("unused")
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

    public Model(String id, String ideIcon, String icon, String description, ModelType modelType, IconType iconType) {
        this(id, icon, description, modelType, iconType);
        this.ideIcon = ideIcon;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getIdeIcon() {
        return ideIcon;
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
        getCurrentCondition().setFacets(Arrays.stream(facets).map(String::toLowerCase).toArray(String[]::new));
        return this;
    }

    public Model iconEnabler(IconEnablerType type) {
        getCurrentCondition().setIconEnablerType(type);
        return this;
    }

    public Model or() {
        this.conditions.add(new ModelCondition());
        return this;
    }

    private ModelCondition getCurrentCondition() {
        return conditions.get(conditions.size() - 1);
    }

    public boolean check(String parentName, String fileName, Optional<String> fullPath, Set<String> facets, Project project) {
        for (ModelCondition condition : conditions) {
            if (condition.check(parentName, fileName, fullPath, facets, project)) {
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
            Objects.equals(ideIcon, model.ideIcon) &&
            Objects.equals(icon, model.icon) &&
            description.equals(model.description) &&
            modelType == model.modelType &&
            iconType == model.iconType &&
            conditions.equals(model.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ideIcon, icon, description, modelType, iconType, enabled, conditions);
    }
}
