// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.dialogs;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CheckBoxList;
import com.intellij.ui.ListUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.IconUtil;
import lermitage.intellij.extra.icons.ExtraIconProvider;
import lermitage.intellij.extra.icons.IconType;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelCondition;
import lermitage.intellij.extra.icons.ModelType;
import lermitage.intellij.extra.icons.cfg.SettingsForm;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import lermitage.intellij.extra.icons.providers.Angular2IconProvider;
import lermitage.intellij.extra.icons.utils.AsyncUtils;
import lermitage.intellij.extra.icons.utils.IconUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static lermitage.intellij.extra.icons.cfg.dialogs.ModelConditionDialog.FIELD_SEPARATOR;

public class ModelDialog extends DialogWrapper {

    // Icons can be SVG or PNG only. Never allow user to pick GIF, JPEG, etc, otherwise
    // we should convert these files to PNG in IconUtils:toBase64 method.
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
    private JComboBox<Object> chooseIconSelector;
    private JBTextField ideIconOverrideTextField;
    private JLabel ideIconOverrideLabel;
    private JBLabel ideIconOverrideTip;

    private IconUtils.ImageWrapper customIconImage;
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
        ideIconOverrideTip.setText("<html><b>Find IDE icons <a href=\"#\">here</a></b>, open the ZIP file then use " +
            "an icon file name (with <i>.svg</i> extension).</html>");
        ideIconOverrideTip.setToolTipText("<html>Open <i>https://jetbrains.design/intellij/resources/icons_list/</i> in default browser.</html>");
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

        chooseIconButton.addActionListener(e -> AsyncUtils.invokeReadActionAndWait(() -> {
            try {
                customIconImage = loadCustomIcon();
                if (customIconImage != null) {
                    chooseIconSelector.setSelectedIndex(0);
                    iconLabel.setIcon(IconUtil.createImageIcon(customIconImage.getImage()));
                }
            } catch (IllegalArgumentException ex) {
                Messages.showErrorDialog(ex.getMessage(), "Could Not Load Icon.");
            }
        }));

        conditionsCheckboxList.getEmptyText().setText("No conditions added.");
        toolbarPanel = createConditionsListToolbar();
        conditionsPanel.add(toolbarPanel, BorderLayout.CENTER);

        typeComboBox.addItem(ModelType.FILE.getFriendlyName());
        typeComboBox.addItem(ModelType.DIR.getFriendlyName());
        if (!settingsForm.isProjectForm()) {
            typeComboBox.addItem(ModelType.ICON.getFriendlyName());
        }

        typeComboBox.addActionListener(e -> updateUIOnTypeChange());

        chooseIconSelector.addItem("choose custom or bundled icon");
        List<Model> bundledModels = new ArrayList<>();
        bundledModels.addAll(Angular2IconProvider.allModels());
        bundledModels.addAll(ExtraIconProvider.allModels());
        bundledModels.stream()
            .map(Model::getIcon)
            .sorted()
            .distinct()
            .forEach(iconPath -> chooseIconSelector.addItem(new BundledIcon(
                iconPath, "bundled: " + iconPath.replace("extra-icons/", ""))));
        ComboBoxRenderer renderer = new ComboBoxRenderer();
        // customIconImage
        chooseIconSelector.setRenderer(renderer);
        chooseIconSelector.setToolTipText("<html>Choose a custom icon with the button on the right,<br>otherwise select a bundled icon in the list.</html>");
        chooseIconSelector.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Object item = event.getItem();
                if (item instanceof BundledIcon) {
                    BundledIcon bundledIcon = (BundledIcon) item;
                    iconLabel.setIcon(IconLoader.getIcon((bundledIcon).getIconPath(), IconUtils.class));
                    customIconImage = new IconUtils.ImageWrapper(bundledIcon.getIconPath());
                } else if (item instanceof String) {
                    iconLabel.setIcon(new ImageIcon());
                }
            }
        });

        updateUIOnTypeChange();
    }

    private void updateUIOnTypeChange() {
        Object selectedItem = typeComboBox.getSelectedItem();
        if (selectedItem != null) {
            boolean ideIconOverrideSelected = selectedItem.equals(ModelType.ICON.getFriendlyName());
            ideIconOverrideLabel.setVisible(ideIconOverrideSelected);
            ideIconOverrideTextField.setVisible(ideIconOverrideSelected);
            ideIconOverrideTip.setVisible(ideIconOverrideSelected);
            conditionsPanel.setVisible(!ideIconOverrideSelected);
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

        Object selectedItem = typeComboBox.getSelectedItem();
        Model newModel;
        if (selectedItem != null && selectedItem.equals(ModelType.ICON.getFriendlyName())) {
            newModel = Model.createIdeIconModel(
                modelIDField.isVisible() ? modelIDField.getText() : null,
                ideIconOverrideTextField.getText(),
                icon,
                descriptionField.getText(),
                ModelType.getByFriendlyName(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString()),
                iconType
            );
        } else {
            newModel = Model.createFileOrFolderModel(
                modelIDField.isVisible() ? modelIDField.getText() : null,
                icon,
                descriptionField.getText(),
                ModelType.getByFriendlyName(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString()),
                iconType,
                IntStream.range(0, conditionsCheckboxList.getItemsCount())
                    .mapToObj(index -> conditionsCheckboxList.getItemAt(index))
                    .collect(Collectors.toList())
            );
        }

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
        ideIconOverrideTextField.setText(model.getIdeIcon());
        typeComboBox.setSelectedItem(model.getModelType().getFriendlyName());
        typeComboBox.updateUI();
        Double additionalUIScale = SettingsService.getIDEInstance().getAdditionalUIScale();
        SwingUtilities.invokeLater(() -> iconLabel.setIcon(IconUtils.getIcon(model, additionalUIScale)));
        if (model.getIconType() == IconType.PATH) {
            for (int itemIdx = 0; itemIdx < chooseIconSelector.getItemCount(); itemIdx++) {
                Object item = chooseIconSelector.getItemAt(itemIdx);
                if (item instanceof BundledIcon && ((BundledIcon) item).iconPath.equals(model.getIcon())) {
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
        }).setRemoveAction(anActionButton ->
            ListUtil.removeSelectedItems(conditionsCheckboxList)
        ).setButtonComparator("Add", "Edit", "Remove").createPanel();
    }

    /**
     * Opens a file chooser dialog and loads the icon.
     */
    private IconUtils.ImageWrapper loadCustomIcon() {
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(
            new FileChooserDescriptor(true, false, false, false, false, false)
                .withFileFilter(file -> extensions.contains(file.getExtension())),
            settingsForm.getProject(),
            null);
        if (virtualFiles.length > 0) {
            return IconUtils.loadFromVirtualFile(virtualFiles[0]);
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

        Object selectedItem = typeComboBox.getSelectedItem();
        if (selectedItem != null && selectedItem.equals(ModelType.ICON.getFriendlyName())) {
            if (ideIconOverrideTextField.getText().trim().isEmpty()) {
                return new ValidationInfo("IDE icon's name cannot be empty!", ideIconOverrideTextField);
            } else if (!ideIconOverrideTextField.getText().endsWith(".svg")) {
                return new ValidationInfo("IDE icon's name must end with .svg!", ideIconOverrideTextField);
            }
        } else {
            if (conditionsCheckboxList.isEmpty()) {
                return new ValidationInfo("Please add a condition to your model!", toolbarPanel);
            }
        }

        return super.doValidate();
    }

    private static class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {

        @SuppressWarnings("OverridableMethodCallInConstructor")
        ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String text = null;
            Icon icon = null;
            if (value instanceof BundledIcon) {
                text = ((BundledIcon) value).getDescription();
                icon = IconLoader.getIcon(((BundledIcon) value).getIconPath(), IconUtils.class);
            } else if (value instanceof String) {
                text = (String) value;
                icon = new ImageIcon();
            }
            setText(text);
            setIcon(icon);

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }

    private static class BundledIcon {
        private final String iconPath;
        private final String description;

        public BundledIcon(String iconPath, String description) {
            this.iconPath = iconPath;
            this.description = description;
        }

        public String getIconPath() {
            return iconPath;
        }

        public String getDescription() {
            return description;
        }
    }
}
