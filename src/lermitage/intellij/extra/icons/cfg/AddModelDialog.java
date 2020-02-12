package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import com.intellij.util.IconUtil;
import com.intellij.util.ImageLoader;
import com.intellij.util.SVGLoader;
import com.intellij.util.ui.ImageUtil;
import com.intellij.util.ui.JBImageIcon;
import lermitage.intellij.extra.icons.IconType;
import lermitage.intellij.extra.icons.InMemoryIconLoader;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelType;
import lermitage.intellij.extra.icons.cfg.settings.SettingsIDEService;
import lermitage.intellij.extra.icons.cfg.settings.SettingsProjectService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddModelDialog extends DialogWrapper {

    private final JBTable conditionsTable = new JBTable();
    private final SettingsForm settingsForm;
    private JPanel pane;
    private JBTextField modelIDField;
    private JBTextField descriptionField;
    private JComboBox<String> typeComboBox;
    private JLabel iconLabel;
    private JButton chooseIconButton;
    private JPanel conditionsPanel;

    private final List<String> extensions = Arrays.asList("svg", "png");

    private InMemoryIconLoader.ImageWrapper customIconImage;

    protected AddModelDialog(SettingsForm settingsForm) {
        super(true);
        this.settingsForm = settingsForm;
        init();
        setTitle("Add New Model");
        chooseIconButton.addActionListener(e -> {
            try {
                loadAndScaleCustomIcon();
                if (customIconImage != null) {
                    iconLabel.setIcon(IconUtil.createImageIcon(customIconImage.getImage()));
                }
            }
            catch (IllegalArgumentException ex) {
                Messages.showErrorDialog(ex.getMessage(), "Could Not Load Icon.");
            }
        });
        conditionsTable.getEmptyText().setText("No conditions set.");
        conditionsPanel.add(ToolbarDecorator.createDecorator(conditionsTable).setAddAction(anActionButton -> {

        }).setEditAction(anActionButton -> {}).setButtonComparator("Add", "Edit").createPanel(), BorderLayout.CENTER);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return pane;
    }

    public Model getNewModel() {
        String icon = null;
        IconType iconType = null;
        if (customIconImage != null) {
            icon = InMemoryIconLoader.toBase64(customIconImage);
            iconType = customIconImage.getIconType();
        }
        return new Model(modelIDField.getText(),
            icon,
            descriptionField.getText(),
            ModelType.valueOf(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString()),
            iconType,
            new ArrayList<>()/*underlyingModel.getConditions()*/); // TODO
    }

    private void loadAndScaleCustomIcon() throws IllegalArgumentException {
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(
            new FileChooserDescriptor(true, false, false, false, false, false)
                .withFileFilter(file -> extensions.contains(file.getExtension())),
            settingsForm.getProject(),
            null);
        if (virtualFiles.length > 0) {
            customIconImage = InMemoryIconLoader.loadFromVirtualFile(virtualFiles[0]);
        }
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
        return super.doValidate();
    }

    private boolean doesIdExist(String id) {
        return Stream.concat(
            Stream.concat(
                Stream.concat(SettingsService.getAllRegisteredModels().stream(),
                    SettingsProjectService.getInstance(settingsForm.getProject()).getCustomModels().stream()),
                SettingsIDEService.getInstance().getCustomModels().stream()),
            settingsForm.getAddedModels().stream())
            .anyMatch(model -> model.getId().equals(id));
    }
}
