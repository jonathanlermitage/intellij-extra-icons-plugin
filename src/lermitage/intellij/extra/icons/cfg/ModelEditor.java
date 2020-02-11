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
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class ModelEditor extends DialogWrapper {

    private final JBTable conditionsTable = new JBTable();
    private JPanel pane;
    private JBTextField modelIDField;
    private JBTextField descriptionField;
    private JComboBox<String> typeComboBox;
    private JLabel iconLabel;
    private JButton chooseIconButton;
    private JPanel conditionsPanel;

    private final List<String> extensions = Arrays.asList("svg", "png");
    private Model underlyingModel;

    private InMemoryIconLoader.ImageWrapper customIconImage;

    protected ModelEditor() {
        super(true);
        init();
        setTitle("Edit Model");
        chooseIconButton.addActionListener(e -> {
            try {
                loadAndScaleCustomIcon();
                if (customIconImage != null) {
                    iconLabel.setIcon(IconUtil.createImageIcon(customIconImage.getImage()));
                }
            }
            catch (IllegalArgumentException ex) {
                Messages.showErrorDialog(ex.getMessage(), "Error While Loading Icon.");
            }
        });
        conditionsTable.getEmptyText().setText("No conditions set.");
        conditionsPanel.add(ToolbarDecorator.createDecorator(conditionsTable).createPanel(), BorderLayout.CENTER);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return pane;
    }

    public void setData(@NotNull Model model) {
        this.underlyingModel = model;
        modelIDField.setText(model.getId());
        descriptionField.setText(model.getDescription());
        typeComboBox.setSelectedItem(model.getModelType().name());
        iconLabel.setIcon(InMemoryIconLoader.getIcon(model));
    }

    public Model getEditedModel() {
        String icon = underlyingModel.getIcon();
        IconType iconType = IconType.PATH;
        if (customIconImage != null) {
            icon = InMemoryIconLoader.toBase64(customIconImage);
            iconType = customIconImage.getIconType();
        }
        return new Model(modelIDField.getText(),
            icon,
            descriptionField.getText(),
            ModelType.valueOf(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString()),
            iconType,
            underlyingModel.getConditions());
    }

    private void loadAndScaleCustomIcon() throws IllegalArgumentException {
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(
            new FileChooserDescriptor(true, false, false, false, false, false)
                .withFileFilter(file -> extensions.contains(file.getExtension())),
            null,
            null);
        if (virtualFiles.length > 0) {
            customIconImage = InMemoryIconLoader.loadFromVirtualFile(virtualFiles[0]);
        }
    }

    @NotNull
    @Override
    protected Action[] createLeftSideActions() {
        return new Action[] { new ResetToDefaultAction() };
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (descriptionField.getText().isEmpty()) {
            return new ValidationInfo("Description cannot be empty!", descriptionField);
        }
        return super.doValidate();
    }

    private class ResetToDefaultAction extends DialogWrapperExitAction {

        public ResetToDefaultAction() {
            super("Reset to defaults", 10);
        }
    }
}
