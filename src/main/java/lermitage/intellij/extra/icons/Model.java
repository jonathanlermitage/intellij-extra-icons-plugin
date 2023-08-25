// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.annotations.OptionTag;
import com.intellij.util.xmlb.annotations.XCollection;
import lermitage.intellij.extra.icons.cfg.services.SettingsService;
import lermitage.intellij.extra.icons.enablers.IconEnablerType;
import lermitage.intellij.extra.icons.enablers.services.GitSubmoduleFolderEnablerService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    private String iconPack;
    @OptionTag
    private ModelType modelType;
    @OptionTag
    private IconType iconType;
    /** Indicates if this model is offered for the old or the new UI. If null, both UIs are supported. */
    @OptionTag
    private UIType uiType;
    /**
     * Indicates if a <b>user</b> model is enabled. To know if a built-in model
     * is enabled, see {@link SettingsService#getDisabledModelIds()}.
     */
    @OptionTag
    private boolean enabled = true;
    @XCollection
    private List<ModelCondition> conditions = new ArrayList<>(Collections.singletonList(new ModelCondition()));

    // transient fields are excluded from IconPack items

    /** Alternative icons. Extra Icons will automatically generate alt icons (and ids, names, etc.) based on on this list. */
    private transient String[] altIcons; // transient because computed dynamically

    /** Tags associated to this model. Used to easily (de)activate models linked to specific tags. */
    private transient List<ModelTag> tags; // transient because not exposed to user models

    /** For a model representing an alternative icon, the ID of the base model, otherwise null. */
    private transient Object parentId = null; // transient because computed dynamically

    private boolean autoLoadNewUIIconVariant = false;

    private boolean inFreemium = false;

    // For XML deserializer (IntelliJ internals)
    @SuppressWarnings("unused")
    private Model() {
    }

    /**
     * Create a rule (Model) to apply given icon to a file. Once created, you need to
     * apply condition(s) to rule, like {@link #start(String...)}, {@link #eq(String...)}, etc.
     * @param id a unique id.
     * @param icon the path of the icon to apply, located in plugin resources folder.
     * @param description human readable description.
     */
    @NotNull
    @Contract("_, _, _ -> new")
    public static Model ofFile(String id, String icon, String description) {
        return createFileOrFolderModel(id, icon, description, ModelType.FILE, IconType.PATH, null);
    }

    /**
     * Create a rule (Model) to apply given icon to a folder. Once created, you need to
     * apply condition(s) to rule, like {@link #start(String...)}, {@link #eq(String...)}, etc.
     * @param id a unique id.
     * @param icon the path of the icon to apply, located in plugin resources folder.
     * @param description human readable description.
     */
    @NotNull
    @Contract("_, _, _ -> new")
    public static Model ofDir(String id, String icon, String description) {
        return createFileOrFolderModel(id, icon, description, ModelType.DIR, IconType.PATH, null);
    }

    /**
     * Create a rule (Model) to override given icon (ideIcon) with new icon (icon). Once created,
     * you must NOT apply conditions (like {@link #start(String...)}, {@link #eq(String...)}, etc.) to this rule,
     * they will be ignored.
     * @param id a unique id.
     * @param ideIcon the name of the icon to override. You can find icon
     *     names <a href="https://jetbrains.design/intellij/resources/icons_list/">here</a>: pick
     *     an icon and open the ZIP file; you can use the non-dark SVG file name.
     * @param icon the path of the icon to apply, located in plugin resources folder.
     * @param description human readable description.
     */
    @NotNull
    @Contract("_, _, _, _ -> new")
    public static Model ofIcon(String id, String ideIcon, String icon, String description) {
        return createIdeIconModel(id, ideIcon, icon, description, ModelType.ICON, IconType.PATH, null);
    }

    @NotNull
    @Contract("_, _, _, _, _, _ -> new")
    public static Model createFileOrFolderModel(String id, String icon, String description,
                                                ModelType modelType, IconType iconType, String iconPack) {
        Model model = new Model();
        model.id = id;
        model.icon = icon;
        model.description = description;
        model.modelType = modelType;
        model.iconType = iconType;
        model.iconPack = iconPack;
        return model;
    }

    @NotNull
    @Contract("_, _, _, _, _, _, _ -> new")
    public static Model createFileOrFolderModel(String id, String icon, String description,
                                                ModelType modelType, IconType iconType, String iconPack,
                                                List<ModelCondition> conditions) {
        Model model = createFileOrFolderModel(id, icon, description, modelType, iconType, iconPack);
        model.conditions = conditions;
        return model;
    }

    @NotNull
    @Contract("_, _, _, _, _, _, _ -> new")
    public static Model createIdeIconModel(String id, String ideIcon, String icon, String description,
                                           ModelType modelType, IconType iconType, String iconPack) {
        Model model = new Model();
        model.id = id;
        model.ideIcon = ideIcon;
        model.icon = icon;
        model.description = description;
        model.modelType = modelType;
        model.iconType = iconType;
        model.iconPack = iconPack;
        return model;
    }

    @NotNull
    @Contract("_, _, _, _, _ -> new")
    public static Model createAltModel(Model baseModel, String altId, String altIdeIcon,
                                       String altIcon, String altDescription) {
        Model altModel = new Model();
        altModel.id = altId;
        altModel.ideIcon = altIdeIcon;
        altModel.icon = altIcon;
        altModel.description = altDescription;
        altModel.modelType = baseModel.getModelType();
        altModel.iconType = baseModel.getIconType();
        altModel.enabled = baseModel.isEnabled();
        altModel.conditions = baseModel.getConditions();
        altModel.tags = baseModel.getTags();
        altModel.parentId = baseModel.getId();
        altModel.uiType = baseModel.getUiType();
        altModel.autoLoadNewUIIconVariant = baseModel.isAutoLoadNewUIIconVariant();
        altModel.inFreemium = baseModel.isInFreemium();
        return altModel;
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

    @Nullable
    public Object getParentId() {
        return parentId;
    }

    @Nullable
    public String getIconPack() {
        return iconPack;
    }

    /**
     * Condition: has given parent directory(s).
     * @param parents one or multiple possible directories, lowercase.
     */
    public Model parents(@NotNull String... parents) {
        getCurrentCondition().setParents(parents);
        return this;
    }

    /**
     * Condition: file/folder name starts with given string(s).
     * @param start strings, lowercase.
     */
    public Model start(@NotNull String... start) {
        getCurrentCondition().setStart(start);
        return this;
    }

    /**
     * Condition: file/folder name is equal to given string(s).
     * @param name strings, lowercase.
     */
    public Model eq(@NotNull String... name) {
        getCurrentCondition().setEq(name);
        return this;
    }

    /**
     * Condition: file/folder name may end with given string(s). This condition is optional.
     * Example: to catch README and README.md files, you will use {@code model.eq("readme").mayEnd(".md")}
     * @param end strings, lowercase.
     */
    public Model mayEnd(@NotNull String... end) {
        getCurrentCondition().setMayEnd(end);
        return this;
    }

    /**
     * Condition: file/folder name ends with given string(s).
     * @param end strings, lowercase.
     */
    public Model end(@NotNull String... end) {
        getCurrentCondition().setEnd(end);
        return this;
    }

    /**
     * Condition: file/folder name must not contain a dot.
     */
    public Model noDot() {
        getCurrentCondition().setNoDot();
        return this;
    }

    /**
     * Condition: file/folder <b>absolute path</b> satisfies given regular expression.
     * @param regex regular expression.
     */
    public Model regex(@NotNull String regex) {
        getCurrentCondition().setRegex(regex);
        return this;
    }

    /**
     * Condition: project has given facet, like 'andoid', 'kotlin', 'python', 'spring' etc. You
     * can see and add facets in Project Structure / Project Settings / Facets.
     * @param facets facet(s), lowercase.
     */
    public Model facets(@NotNull String... facets) {
        getCurrentCondition().setFacets(Arrays.stream(facets).map(String::toLowerCase).toArray(String[]::new));
        return this;
    }

    /**
     * Condition: use an {@link lermitage.intellij.extra.icons.enablers.IconEnablerType},
     * like {@link GitSubmoduleFolderEnablerService}.
     * @param type IconEnablerType.
     */
    public Model iconEnabler(@NotNull IconEnablerType type) {
        getCurrentCondition().setIconEnablerType(type);
        return this;
    }

    /**
     * Register alternate icons.
     * @param altIcons an array of icons.
     */
    public Model altIcons(@NotNull String... altIcons) {
        this.altIcons = altIcons;
        return this;
    }

    /** This model is offered for the old UI only. */
    public Model oldUIOnly() {
        this.uiType = UIType.OLD_UI;
        return this;
    }

    /** This model is offered for the new UI only. */
    public Model newUIOnly() {
        this.uiType = UIType.NEW_UI;
        return this;
    }

    /** Model has a new UI icon in "./newui/": plugin will use it if the new UI is active. */
    public Model autoLoadNewUIIconVariant() {
        this.autoLoadNewUIIconVariant = true;
        return this;
    }

    /** Indicates if model has a new UI icon in "./newui/". */
    public boolean isAutoLoadNewUIIconVariant() {
        return autoLoadNewUIIconVariant;
    }

    /** This model is available in freemium (free) mode. If not, it requires a paid licence. */
    public Model inFreemium() {
        this.inFreemium = true;
        return this;
    }

    public boolean isInFreemium() {
        return inFreemium;
    }

    /**
     * Associate this model to one or multiple tags. User will be able to enable and disable multiple models by tag.
     * @param tags an array of tags.
     */
    public Model tags(@NotNull ModelTag... tags) {
        this.tags = Arrays.stream(tags).collect(Collectors.toList());
        return this;
    }

    /** Add a possible condition to current model: file/folder will have to satisfy one of configured conditions. */
    public Model or() {
        this.conditions.add(new ModelCondition());
        return this;
    }

    private ModelCondition getCurrentCondition() {
        return conditions.get(conditions.size() - 1);
    }

    public boolean check(String parentName, String fileName, @Nullable String fullPath, Set<String> facets, Project project) {
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

    public UIType getUiType() {
        return uiType;
    }

    public List<ModelCondition> getConditions() {
        return conditions;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String[] getAltIcons() {
        return altIcons;
    }

    public List<ModelTag> getTags() {
        return tags == null ? Collections.emptyList() : tags;
    }

    public void setIconPack(String iconPack) {
        this.iconPack = iconPack;
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
            Objects.equals(iconPack, model.iconPack) &&
            description.equals(model.description) &&
            modelType == model.modelType &&
            uiType == model.uiType &&
            iconType == model.iconType &&
            conditions.equals(model.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ideIcon, icon, iconPack, description, modelType, uiType, iconType, enabled, conditions);
    }
}
