package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CheckBoxList;
import com.intellij.ui.ListUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.IconUtil;
import lermitage.intellij.extra.icons.*;
import lermitage.intellij.extra.icons.cfg.settings.SettingsIDEService;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

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

    private InMemoryIconLoader.ImageWrapper customIconImage;
    private JPanel toolbarPanel;

    private final List<ModelCondition> conditions = new ArrayList<>();

    protected ModelDialog(SettingsForm settingsForm) {
        super(true);
        this.settingsForm = settingsForm;
        init();
        setTitle("Add New Model");
        initComponents();
    }

    private void initComponents() {
        conditionsCheckboxList = new CheckBoxList<>((index, value) -> {
            conditions.get(index).setEnabled(value);
        });
        chooseIconButton.addActionListener(e -> {
            try {
                customIconImage = loadCustomIcon();
                if (customIconImage != null) {
                    iconLabel.setIcon(IconUtil.createImageIcon(customIconImage.getImage()));
                }
            }
            catch (IllegalArgumentException ex) {
                Messages.showErrorDialog(ex.getMessage(), "Could Not Load Icon.");
            }
        });
        conditionsCheckboxList.getEmptyText().setText("No conditions added.");

        toolbarPanel = ToolbarDecorator.createDecorator(conditionsCheckboxList).setAddAction(anActionButton -> {
            ModelConditionDialog modelConditionDialog = new ModelConditionDialog();
            boolean wasOk = modelConditionDialog.showAndGet();
            if (wasOk) {
                ModelCondition modelCondition = modelConditionDialog.getModelConditionFromInput();
                conditionsCheckboxList.addItem(modelCondition, modelCondition.asReadableString(), modelCondition.isEnabled());
                conditions.add(modelCondition);
            }
        }).setEditAction(anActionButton -> {
            int selectedItem = conditionsCheckboxList.getSelectedIndex();
            ModelConditionDialog modelConditionDialog = new ModelConditionDialog();
            modelConditionDialog.setCondition(conditionsCheckboxList.getItemAt(selectedItem));
            boolean wasOk = modelConditionDialog.showAndGet();
            if (wasOk) {
                ModelCondition modelCondition = modelConditionDialog.getModelConditionFromInput();
                conditions.set(selectedItem, modelCondition);
                conditionsCheckboxList.setItems(conditions, ModelCondition::asReadableString);
                conditions.forEach(condition -> conditionsCheckboxList.setItemSelected(condition, condition.isEnabled()));
            }
        }).setRemoveAction(anActionButton -> {
            int selectedItem = conditionsCheckboxList.getSelectedIndex();
            conditions.remove(selectedItem);
            ListUtil.removeSelectedItems(conditionsCheckboxList);
        }).setButtonComparator("Add", "Edit", "Remove").createPanel();
        conditionsPanel.add(toolbarPanel, BorderLayout.CENTER);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return pane;
    }

    public Model getModelFromInput() {
        String icon = null;
        IconType iconType = null;
        if (customIconImage != null) {
            icon = InMemoryIconLoader.toBase64(customIconImage);
            iconType = customIconImage.getIconType();
        }
        return new Model(modelIDField.getText(),
            icon,
            descriptionField.getText(),
            ModelType.valueOf(typeComboBox.getSelectedItem().toString()),
            iconType,
            conditions);
    }

    private InMemoryIconLoader.ImageWrapper loadCustomIcon() {
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(
            new FileChooserDescriptor(true, false, false, false, false, false)
                .withFileFilter(file -> extensions.contains(file.getExtension())),
            settingsForm.getProject() == null ? ProjectManager.getInstance().getDefaultProject() : settingsForm.getProject(),
            null);
        if (virtualFiles.length > 0) {
            return InMemoryIconLoader.loadFromVirtualFile(virtualFiles[0]);
        }
        return null;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (modelIDField.getText().isEmpty()) {
            return new ValidationInfo("ID cannot be empty!", modelIDField);
        }
        String id = modelIDField.getText();
        if (doesIdExist(id)) {
            return new ValidationInfo("ID already exists!", modelIDField);
        }
        if (descriptionField.getText().isEmpty()) {
            return new ValidationInfo("Description cannot be empty!", descriptionField);
        }
        if (customIconImage == null) {
            return new ValidationInfo("Please add an icon!", chooseIconButton);
        }
        if (conditions.isEmpty()) {
            return new ValidationInfo("Please add a condition to your model!", toolbarPanel);
        }
        return super.doValidate();
    }

    private boolean doesIdExist(String id) {
        return Stream.concat(
            Stream.concat(SettingsService.getAllRegisteredModels().stream(),
                SettingsIDEService.getInstance().getCustomModels().stream()),
            settingsForm.getAddedModels().stream())
            .anyMatch(model -> model.getId().equals(id));
    }
}
