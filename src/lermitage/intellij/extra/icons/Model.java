package lermitage.intellij.extra.icons;

import com.intellij.util.xmlb.annotations.OptionTag;
import com.intellij.util.xmlb.annotations.XCollection;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        getCurrentCondition().setExtensions(extensions);
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

    public Model or() {
        this.conditions.add(new ModelCondition());
        return this;
    }

    private ModelCondition getCurrentCondition() {
        return conditions.get(conditions.size() - 1);
    }

    public boolean check(String parentName, String fileName, Optional<String> fullPath) {
        for (ModelCondition condition : conditions) {
            if (condition.check(parentName, fileName, fullPath)) {
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
}
