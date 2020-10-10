// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.dialogs;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CheckBoxList;
import com.intellij.ui.ListUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.IconUtil;
import lermitage.intellij.extra.icons.*;
import lermitage.intellij.extra.icons.cfg.SettingsForm;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static lermitage.intellij.extra.icons.cfg.dialogs.ModelConditionDialog.FIELD_SEPARATOR;

public class ModelDialog extends DialogWrapper {

    private final List<String> extensions = Arrays.asList("svg", "png");

    private final SettingsForm settingsForm;

    private CheckBoxList<ModelCondition> conditionsCheckboxList;
    private JPanel pane;
    private JBTextField modelIDField;
    private JBTextField descriptionField;
    private JComboBox<String> typeComboBox;
    private JLabel iconLabel;
    private JButton chooseIconButton;
    private JPanel conditionsPanel;
    private JBLabel idLabel;

    private CustomIconLoader.ImageWrapper customIconImage;
    private JPanel toolbarPanel;

    private Model modelToEdit;

    public ModelDialog(SettingsForm settingsForm) {
        super(true);
        this.settingsForm = settingsForm;
        init();
        setTitle("Add New Model");
        initComponents();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return pane;
    }

    private void initComponents() {
        setIdComponentsVisible(false);
        conditionsCheckboxList = new CheckBoxList<>((index, value) -> {
            //noinspection ConstantConditions
            conditionsCheckboxList.getItemAt(index).setEnabled(value);
        });
        chooseIconButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            try {
                customIconImage = loadCustomIcon();
                if (customIconImage != null) {
                    iconLabel.setIcon(IconUtil.createImageIcon(customIconImage.getImage()));
                }
            }
            catch (IllegalArgumentException ex) {
                Messages.showErrorDialog(ex.getMessage(), "Could Not Load Icon.");
            }
        }));
        conditionsCheckboxList.getEmptyText().setText("No conditions added.");
        toolbarPanel = createConditionsListToolbar();
        conditionsPanel.add(toolbarPanel, BorderLayout.CENTER);

        for (ModelType value : ModelType.values()) {
            typeComboBox.addItem(value.getFriendlyName());
        }
    }

    /**
     * Creates a new model from the user input.
     */
    public Model getModelFromInput() {
        String icon = null;
        IconType iconType = null;
        if (customIconImage != null) {
            icon = CustomIconLoader.toBase64(customIconImage);
            iconType = customIconImage.getIconType();
        }
        else if (modelToEdit != null) {
            icon = modelToEdit.getIcon();
            iconType = modelToEdit.getIconType();
        }
        Model newModel = new Model(modelIDField.isVisible() ? modelIDField.getText() : null,
            icon,
            descriptionField.getText(),
            ModelType.getByFriendlyName(typeComboBox.getSelectedItem().toString()),
            iconType,
            IntStream.range(0, conditionsCheckboxList.getItemsCount())
                .mapToObj(index -> conditionsCheckboxList.getItemAt(index))
                .collect(Collectors.toList())
        );
        if (modelToEdit != null) {
            newModel.setEnabled(modelToEdit.isEnabled());
        }
        return newModel;
    }

    /**
     * Sets a model that will be edited using this dialog.
     */
    public void setModelToEdit(Model model) {
        modelToEdit = model;
        setTitle("Edit Model");
        boolean hasModelId = model.getId() != null;
        setIdComponentsVisible(hasModelId);
        if (hasModelId) {
            modelIDField.setText(model.getId());
        }
        descriptionField.setText(model.getDescription());
        typeComboBox.setSelectedItem(model.getModelType().getFriendlyName());
        typeComboBox.updateUI();
        SwingUtilities.invokeLater(() -> iconLabel.setIcon(CustomIconLoader.getIcon(model)));
        model.getConditions().forEach(modelCondition -> {
            conditionsCheckboxList.addItem(modelCondition, modelCondition.asReadableString(FIELD_SEPARATOR), modelCondition.isEnabled());
        });
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
        }).setRemoveAction(anActionButton -> {
            ListUtil.removeSelectedItems(conditionsCheckboxList);
        }).setButtonComparator("Add", "Edit", "Remove").createPanel();
    }

    /**
     * Opens a file chooser dialog and loads the icon.
     */
    private CustomIconLoader.ImageWrapper loadCustomIcon() {
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(
            new FileChooserDescriptor(true, false, false, false, false, false)
                .withFileFilter(file -> extensions.contains(file.getExtension())),
            settingsForm.getProject(),
            null);
        if (virtualFiles.length > 0) {
            return CustomIconLoader.loadFromVirtualFile(virtualFiles[0]);
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
            return new ValidationInfo("ID cannot be empty!", modelIDField);
        }
        if (descriptionField.getText().isEmpty()) {
            return new ValidationInfo("Description cannot be empty!", descriptionField);
        }
        if (customIconImage == null && modelToEdit == null) {
            return new ValidationInfo("Please add an icon!", chooseIconButton);
        }
        if (conditionsCheckboxList.isEmpty()) {
            return new ValidationInfo("Please add a condition to your model!", toolbarPanel);
        }
        return super.doValidate();
    }
}
