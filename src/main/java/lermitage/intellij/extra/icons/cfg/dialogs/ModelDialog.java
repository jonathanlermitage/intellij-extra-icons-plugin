// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.dialogs;

import com.intellij.ide.ui.laf.darcula.ui.DarculaTextBorder;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.CheckBoxList;
import com.intellij.ui.JBColor;
import com.intellij.ui.ListUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.IconUtil;
import lermitage.intellij.extra.icons.ExtraIconProvider;
import lermitage.intellij.extra.icons.Globals;
import lermitage.intellij.extra.icons.IconType;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelCondition;
import lermitage.intellij.extra.icons.ModelType;
import lermitage.intellij.extra.icons.cfg.SettingsForm;
import lermitage.intellij.extra.icons.cfg.services.SettingsIDEService;
import lermitage.intellij.extra.icons.utils.BundledIcon;
import lermitage.intellij.extra.icons.utils.ComboBoxWithImageRenderer;
import lermitage.intellij.extra.icons.utils.FileChooserUtils;
import lermitage.intellij.extra.icons.utils.I18nUtils;
import lermitage.intellij.extra.icons.utils.IconUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ComponentAdapter;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static lermitage.intellij.extra.icons.cfg.dialogs.ModelConditionDialog.FIELD_SEPARATOR;

public class ModelDialog extends DialogWrapper {

    private final SettingsForm settingsForm;
    private final Project project;

    private CheckBoxList<ModelCondition> conditionsCheckboxList;
    private JPanel pane;
    private JBTextField modelIDField;
    private JBTextField descriptionField;
    private JComboBox<String> typeComboBox;
    private JLabel iconLabel;
    private JButton chooseIconButton;
    private JPanel conditionsPanel;
    private JBLabel idLabel;
    private JComboBox<Object> chooseIconSelector;
    private JBTextField ideIconOverrideTextField;
    private JLabel ideIconOverrideLabel;
    private JBLabel ideIconOverrideTip;
    private JTextField testTextField;
    private JLabel testLabel;
    private JLabel descriptionLabel;
    private JLabel typeLabel;
    private JLabel iconLeftLabel;
    private JBTextField iconPackField;
    private JLabel iconPackLabel;

    private IconUtils.ImageWrapper customIconImage;
    private JPanel toolbarPanel;

    private Model modelToEdit;

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    public ModelDialog(SettingsForm settingsForm, Project project) {
        super(true);
        this.settingsForm = settingsForm;
        this.project = project;
        init();
        setTitle(i18n.getString("model.dialog.title"));
        initComponents();
        conditionsPanel.addComponentListener(new ComponentAdapter() {
        });
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return pane;
    }

    private void initComponents() {
        setIdComponentsVisible(false);
        ideIconOverrideTip.setText(i18n.getString("model.dialog.override.ide.tip"));
        ideIconOverrideTip.setToolTipText(i18n.getString("model.dialog.override.ide.tip.tooltip"));
        ideIconOverrideTip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ideIconOverrideTip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://jetbrains.design/intellij/resources/icons_list/"));
                } catch (Exception ex) {
                    //ignore
                }
            }
        });
        conditionsCheckboxList = new CheckBoxList<>((index, value) -> {
            //noinspection ConstantConditions
            conditionsCheckboxList.getItemAt(index).setEnabled(value);
        });

        chooseIconButton.addActionListener(al -> {
            try {
                customIconImage = loadCustomIcon();
                if (customIconImage != null) {
                    chooseIconSelector.setSelectedIndex(0);
                    iconLabel.setIcon(IconUtil.createImageIcon(customIconImage.getImage()));
                }
            } catch (IllegalArgumentException ex) {
                Messages.showErrorDialog(ex.getMessage(), i18n.getString("model.dialog.choose.icon.failed.to.load.icon"));
            }
        });

        conditionsCheckboxList.getEmptyText().setText(i18n.getString("model.dialog.choose.icon.no.conditions.added"));
        conditionsCheckboxList.addPropertyChangeListener(evt -> testModel(getModelFromInput(), testTextField));

        toolbarPanel = createConditionsListToolbar();
        conditionsPanel.add(toolbarPanel, BorderLayout.CENTER);

        typeComboBox.addItem(ModelType.FILE.getI18nFriendlyName());
        typeComboBox.addItem(ModelType.DIR.getI18nFriendlyName());
        if (!settingsForm.isProjectForm()) {
            typeComboBox.addItem(ModelType.ICON.getI18nFriendlyName());
        }

        typeComboBox.addActionListener(e -> updateUIOnTypeChange());

        chooseIconSelector.addItem(i18n.getString("model.dialog.choose.icon.first.item"));
        ExtraIconProvider.allModels().stream()
            .map(Model::getIcon)
            .sorted()
            .distinct()
            .forEach(iconPath -> chooseIconSelector.addItem(new BundledIcon(
                iconPath, MessageFormat.format(i18n.getString("model.dialog.choose.icon.bundled.icon"),
                iconPath.replace("extra-icons/", ""))))); //NON-NLS
        ComboBoxWithImageRenderer renderer = new ComboBoxWithImageRenderer();
        // customIconImage
        chooseIconSelector.setRenderer(renderer);
        chooseIconSelector.setToolTipText(i18n.getString("model.dialog.choose.icon.tooltip"));
        chooseIconSelector.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Object item = event.getItem();
                if (item instanceof BundledIcon bundledIcon) {
                    iconLabel.setIcon(IconLoader.getIcon((bundledIcon).iconPath(), IconUtils.class));
                    customIconImage = new IconUtils.ImageWrapper(bundledIcon.iconPath());
                } else if (item instanceof String) {
                    iconLabel.setIcon(new ImageIcon());
                }
            }
        });

        testLabel.setText(i18n.getString("model.dialog.model.tester"));
        testTextField.setText("");
        testTextField.setToolTipText(i18n.getString("model.dialog.model.tester.tooltip"));
        testTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                testModel(getModelFromInput(), testTextField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                testModel(getModelFromInput(), testTextField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                testModel(getModelFromInput(), testTextField);
            }
        });

        idLabel.setText(i18n.getString("model.dialog.id.label"));
        descriptionLabel.setText(i18n.getString("model.dialog.description.label"));
        typeLabel.setText(i18n.getString("model.dialog.type.label"));
        iconPackLabel.setText(i18n.getString("model.dialog.iconpack.label"));
        ideIconOverrideLabel.setText(i18n.getString("model.dialog.icons.name.label"));
        iconLeftLabel.setText(i18n.getString("model.dialog.icon.type.selector.label"));
        chooseIconButton.setText(i18n.getString("model.dialog.icon.chooser.btn"));
        testLabel.setText(i18n.getString("model.dialog.tester.label"));

        updateUIOnTypeChange();
    }

    private void testModel(Model model, JTextField testTextField) {
        if (testTextField.getText().isEmpty()) {
            testTextField.setBorder(new DarculaTextBorder());
        } else {
            if (model == null || model.getConditions().isEmpty()) {
                testTextField.setBorder(new LineBorder(JBColor.RED, 1));
            } else {
                boolean checked = model.check("", testTextField.getText(), testTextField.getText(), Collections.emptySet(), null);
                if (checked) {
                    testTextField.setBorder(new LineBorder(JBColor.GREEN, 1));
                } else {
                    testTextField.setBorder(new LineBorder(JBColor.RED, 1));
                }
            }
        }
    }

    private void updateUIOnTypeChange() {
        Object selectedItem = typeComboBox.getSelectedItem();
        testLabel.setVisible(true);
        testTextField.setVisible(true);
        if (selectedItem != null) {
            Optional<ModelType> selectedModelType = getSelectedModelType();
            boolean ideIconOverrideSelected = selectedModelType.isPresent() && selectedModelType.get() == ModelType.ICON;
            ideIconOverrideLabel.setVisible(ideIconOverrideSelected);
            ideIconOverrideTextField.setVisible(ideIconOverrideSelected);
            ideIconOverrideTip.setVisible(ideIconOverrideSelected);
            conditionsPanel.setVisible(!ideIconOverrideSelected);
            if (ideIconOverrideSelected) {
                testLabel.setVisible(false);
                testTextField.setVisible(false);
            }
        }
    }

    /**
     * Creates a new model from the user input.
     */
    public Model getModelFromInput() {
        String icon = null;
        IconType iconType = null;
        if (customIconImage != null) {
            iconType = customIconImage.getIconType();
            if (customIconImage.getIconType() == IconType.PATH) {
                icon = customIconImage.getImageAsBundledIconRef();
            } else {
                icon = IconUtils.toBase64(customIconImage);
            }
        } else if (modelToEdit != null) {
            icon = modelToEdit.getIcon();
            iconType = modelToEdit.getIconType();
        }

        Optional<ModelType> selectedModlType = getSelectedModelType();
        Model newModel = null;
        if (selectedModlType.isPresent()) {
            if (selectedModlType.get() == ModelType.ICON) {
                newModel = Model.createIdeIconModel(
                    modelIDField.isVisible() ? modelIDField.getText() : null,
                    ideIconOverrideTextField.getText(),
                    icon,
                    descriptionField.getText(),
                    selectedModlType.get(),
                    iconType,
                    iconPackField.getText()
                );
            } else {
                newModel = Model.createFileOrFolderModel(
                    modelIDField.isVisible() ? modelIDField.getText() : null,
                    icon,
                    descriptionField.getText(),
                    selectedModlType.get(),
                    iconType,
                    iconPackField.getText(),
                    IntStream.range(0, conditionsCheckboxList.getItemsCount())
                        .mapToObj(index -> conditionsCheckboxList.getItemAt(index))
                        .collect(Collectors.toList())
                );
            }
        }

        if (modelToEdit != null && newModel != null) {
            newModel.setEnabled(modelToEdit.isEnabled());
        }
        return newModel;
    }

    private Optional<ModelType> getSelectedModelType() {
        int selectedIndex = typeComboBox.getSelectedIndex();
        if (selectedIndex < 0) {
            return Optional.empty();
        }
        return Optional.of(ModelType.values()[typeComboBox.getSelectedIndex()]);
    }

    private int getModelTypeIdx(ModelType modelType) {
        return switch (modelType) {
            case FILE -> 0;
            case DIR -> 1;
            case ICON -> 2;
        };
    }

    /**
     * Sets a model that will be edited using this dialog.
     */
    public void setModelToEdit(Model model) {
        modelToEdit = model;
        setTitle(i18n.getString("model.dialog.model.editor"));
        boolean hasModelId = model.getId() != null;
        setIdComponentsVisible(hasModelId);
        if (hasModelId) {
            modelIDField.setText(model.getId());
        }
        descriptionField.setText(model.getDescription());
        ideIconOverrideTextField.setText(model.getIdeIcon());
        typeComboBox.setSelectedIndex(getModelTypeIdx(model.getModelType()));
        typeComboBox.updateUI();
        iconPackField.setText(model.getIconPack());

        SettingsIDEService settingsIDEService = SettingsIDEService.getInstance();
        Double additionalUIScale = settingsIDEService.getAdditionalUIScale2();
        SwingUtilities.invokeLater(() -> iconLabel.setIcon(IconUtils.getIcon(model, additionalUIScale, settingsIDEService.getUiTypeIconsPreference())));
        if (model.getIconType() == IconType.PATH) {
            for (int itemIdx = 0; itemIdx < chooseIconSelector.getItemCount(); itemIdx++) {
                Object item = chooseIconSelector.getItemAt(itemIdx);
                if (item instanceof BundledIcon && ((BundledIcon) item).iconPath().equals(model.getIcon())) {
                    chooseIconSelector.setSelectedIndex(itemIdx);
                    break;
                }
            }
        }
        model.getConditions().forEach(modelCondition ->
            conditionsCheckboxList.addItem(modelCondition, modelCondition.asReadableString(FIELD_SEPARATOR), modelCondition.isEnabled()));

        updateUIOnTypeChange();
    }

    /**
     * Adds a toolbar with add, edit and remove actions to the CheckboxList.
     */
    private JPanel createConditionsListToolbar() {
        return ToolbarDecorator.createDecorator(conditionsCheckboxList).setAddAction(anActionButton -> {
            ModelConditionDialog modelConditionDialog = new ModelConditionDialog();
            if (modelConditionDialog.showAndGet()) {
                ModelCondition modelCondition = modelConditionDialog.getModelConditionFromInput();
                conditionsCheckboxList.addItem(modelCondition, modelCondition.asReadableString(FIELD_SEPARATOR), modelCondition.isEnabled());
            }
            testModel(getModelFromInput(), testTextField);
        }).setEditAction(anActionButton -> {
            int selectedItem = conditionsCheckboxList.getSelectedIndex();
            ModelCondition selectedCondition = Objects.requireNonNull(conditionsCheckboxList.getItemAt(selectedItem));
            boolean isEnabled = conditionsCheckboxList.isItemSelected(selectedCondition);

            ModelConditionDialog modelConditionDialog = new ModelConditionDialog();
            modelConditionDialog.setCondition(selectedCondition);
            if (modelConditionDialog.showAndGet()) {
                ModelCondition newCondition = modelConditionDialog.getModelConditionFromInput();
                conditionsCheckboxList.updateItem(selectedCondition, newCondition, newCondition.asReadableString(FIELD_SEPARATOR));
                newCondition.setEnabled(isEnabled);
            }
            testModel(getModelFromInput(), testTextField);
        }).setRemoveAction(anActionButton -> {
                ListUtil.removeSelectedItems(conditionsCheckboxList);
                testModel(getModelFromInput(), testTextField);
            }
        ).setButtonComparator(
            i18n.getString("model.dialog.creator.condition.col.add"),
            i18n.getString("model.dialog.creator.condition.col.edit"),
            i18n.getString("model.dialog.creator.condition.col.remove")
        ).createPanel();
    }

    /**
     * Opens a file chooser dialog and loads the icon.
     */
    private IconUtils.ImageWrapper loadCustomIcon() {
        Optional<String> iconPath = FileChooserUtils.chooseFile("", this.pane,
            Globals.ALLOWED_ICON_FILE_EXTENSIONS_FILE_SELECTOR_LABEL,
            Globals.ALLOWED_ICON_FILE_EXTENSIONS);
        Project projectToLinkToModalProgress = project;
        if (projectToLinkToModalProgress == null) {
            projectToLinkToModalProgress = ProjectUtils.getFirstOpenedProject();
        }
        if (projectToLinkToModalProgress == null) {
            if (iconPath.isPresent()) {
                // TODO User wants to edit a User Icon when no project is opened. We have no workaround to
                //  avoid "Slow operations are prohibited on EDT" error log in this situation, but, this is only
                //  a log message, nothing is broken. I think we can leave it as is, and remove this code once
                //  issue #126 has a better fixed.
                VirtualFile fileByUrl = VirtualFileManager.getInstance().findFileByNioPath(Path.of(iconPath.get()));
                if (fileByUrl != null) {
                    return IconUtils.loadFromVirtualFile(fileByUrl);
                }
            }
        } else {
            if (iconPath.isPresent()) {
                // FIXME temporary workaround for "Slow operations are prohibited on EDT" issue
                //  https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/126
                //  We should be able to use VirtualFileManager.getInstance().findFileByNioPath directly
                return ActionUtil.underModalProgress(projectToLinkToModalProgress, "Loading selected icon", //NON-NLS
                    () -> {
                        VirtualFile fileByUrl = VirtualFileManager.getInstance().findFileByNioPath(Path.of(iconPath.get()));
                        if (fileByUrl != null) {
                            return IconUtils.loadFromVirtualFile(fileByUrl);
                        }
                        return null;
                    });
            }
        }
        return null;
    }

    private void setIdComponentsVisible(boolean visible) {
        idLabel.setVisible(visible);
        modelIDField.setVisible(visible);
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (modelIDField.isVisible() && modelIDField.getText().isEmpty()) {
            return new ValidationInfo(i18n.getString("model.dialog.validation.id.missing"), modelIDField);
        }
        if (descriptionField.getText().isEmpty()) {
            return new ValidationInfo(i18n.getString("model.dialog.validation.desc.missing"), descriptionField);
        }
        if (customIconImage == null && modelToEdit == null) {
            return new ValidationInfo(i18n.getString("model.dialog.validation.icon.missing"), chooseIconButton);
        }

        int selectedItemIdx = typeComboBox.getSelectedIndex();
        if (selectedItemIdx != -1 && selectedItemIdx == getModelTypeIdx(ModelType.ICON)) {
            if (ideIconOverrideTextField.getText().trim().isEmpty()) {
                return new ValidationInfo(i18n.getString("model.dialog.validation.ide.icon.name.missing"), ideIconOverrideTextField);
            } else if (!ideIconOverrideTextField.getText().endsWith(".svg")) {
                return new ValidationInfo(i18n.getString("model.dialog.validation.ide.icon.must.end.svg"), ideIconOverrideTextField);
            }
        } else {
            if (conditionsCheckboxList.isEmpty()) {
                return new ValidationInfo(i18n.getString("model.dialog.validation.condition.missing"), toolbarPanel);
            }
        }

        return super.doValidate();
    }
}
