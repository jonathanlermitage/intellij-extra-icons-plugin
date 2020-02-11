package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.SeparatorWithText;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import lermitage.intellij.extra.icons.InMemoryIconLoader;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelType;
import lermitage.intellij.extra.icons.cfg.settings.SettingsProjectService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SettingsForm implements Configurable {

    private final JBTable table = new JBTable();
    private JPanel pane;
    private JBLabel title;
    private JButton buttonEnableAll;
    private JButton buttonDisableAll;
    private JCheckBox overrideSettingsCheckbox;
    private JBTextField ignoredPatternTextField;
    private JBLabel ignoredPatternTitle;
    private JPanel tablePanel;

    private SettingsTableModel settingsTableModel;
    private Project project;
    private boolean isProjectForm = false;
    private boolean modified = false;

    public SettingsForm() {
        buttonEnableAll.addActionListener(e -> enableAll());
        buttonDisableAll.addActionListener(e -> disableAll());
    }

    @SuppressWarnings("UnusedDeclaration")
    public SettingsForm(Project project) {
        this();
        this.project = project;
        this.isProjectForm = true;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Extra Icons";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        initComponents();
        return pane;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void apply() {
        if (isProjectForm) {
            boolean selected = overrideSettingsCheckbox.isSelected();
            SettingsProjectService.getInstance(project).setOverrideIDESettings(selected);
        }
        List<String> disabledModelIds = new ArrayList<>();
        for (int settingsTableRow = 0; settingsTableRow < settingsTableModel.getRowCount(); settingsTableRow++) {
            boolean iconEnabled = (boolean) settingsTableModel.getValueAt(settingsTableRow, SettingsTableModel.ICON_ENABLED_ROW_NUMBER);
            if (!iconEnabled) {
                disabledModelIds.add((String) settingsTableModel.getValueAt(settingsTableRow, SettingsTableModel.ICON_ID_ROW_NUMBER));
            }
        }
        SettingsService.getInstance(project).setDisabledModelIds(disabledModelIds);
        SettingsService.getInstance(project).setIgnoredPattern(ignoredPatternTextField.getText());
        modified = false;
    }

    private void initComponents() {
        title.setText("Select extra icons to activate, then hit OK or Apply button:");
        buttonEnableAll.setText("Enable all");
        buttonDisableAll.setText("Disable all");
        ignoredPatternTitle.setText("Regex to ignore paths:");
        ignoredPatternTextField.getEmptyText().setText("Lowercased parent/fileOrFolder");
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setFocusable(false);
        table.setEnabled(true);
        table.setRowSelectionAllowed(true);
        initCheckbox();
        loadTable();
        loadIgnoredPattern();
    }

    private void initCheckbox() {
        if (!isProjectForm) {
            overrideSettingsCheckbox.setVisible(false);
            return;
        }
        overrideSettingsCheckbox.setText("Override IDE settings");
        boolean shouldOverride = SettingsProjectService.getInstance(project).isOverrideIDESettings();
        overrideSettingsCheckbox.setSelected(shouldOverride);
        setComponentState(shouldOverride);
        overrideSettingsCheckbox.setToolTipText("Set icons on a project-level basis");
        overrideSettingsCheckbox.addItemListener(item -> {
            boolean enabled = item.getStateChange() == ItemEvent.SELECTED;
            setComponentState(enabled);
            modified = true;
        });
    }

    private void setComponentState(boolean enabled) {
        buttonEnableAll.setEnabled(enabled);
        buttonDisableAll.setEnabled(enabled);
        table.setEnabled(enabled);
        ignoredPatternTitle.setEnabled(enabled);
        ignoredPatternTextField.setEnabled(enabled);
        title.setEnabled(enabled);
    }

    @Override
    public void reset() {
        initCheckbox();
        setTableModel();
        loadIgnoredPattern();
        modified = false;
    }

    private void enableAll() {
        for (int settingsTableRow = 0; settingsTableRow < settingsTableModel.getRowCount(); settingsTableRow++) {
            settingsTableModel.setValueAt(true, settingsTableRow, SettingsTableModel.ICON_ENABLED_ROW_NUMBER);
        }
    }

    private void disableAll() {
        for (int settingsTableRow = 0; settingsTableRow < settingsTableModel.getRowCount(); settingsTableRow++) {
            settingsTableModel.setValueAt(false, settingsTableRow, SettingsTableModel.ICON_ENABLED_ROW_NUMBER);
        }
    }

    private void loadTable() {
        setTableModel();
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(table).setEditAction(anActionButton -> {
            int selectedRow = table.getSelectedRow();
            ModelEditor editor = new ModelEditor();
            List<Model> modelList;
            if ((boolean) settingsTableModel.getValueAt(selectedRow, SettingsTableModel.ICON_EDITED_ROW_NUMBER)) {
                modelList = SettingsService.getInstance(project).getEditedModels();
            }
            else {
                modelList = SettingsService.getAllRegisteredModels();
            }
            editor.setData(
                modelList.stream()
                    .filter(it -> it.getId().equals(settingsTableModel.getValueAt(selectedRow, SettingsTableModel.ICON_ID_ROW_NUMBER)))
                    .findFirst()
                    .get()
            );
            editor.show();
            int exitCode = editor.getExitCode();
            Model newModel = editor.getEditedModel();
            if (exitCode == 0 || exitCode == 10) {
                SettingsService.getInstance(project).getEditedModels().removeIf(model -> newModel.getId().equals(model.getId()));
            }
            if (exitCode == 0) {
                SettingsService.getInstance(project).getEditedModels().add(newModel);
            }
            setTableModel();
        }).setButtonComparator("Edit");
        tablePanel.add(decorator.createPanel(), BorderLayout.CENTER);
    }

    private void setTableModel() {
        int currentSelected = settingsTableModel != null ? table.getSelectedRow() : -1;
        settingsTableModel = new SettingsTableModel();
        List<Model> allRegisteredModels = SettingsService.getAllRegisteredModels();
        allRegisteredModels.sort((o1, o2) -> {
            // folders first, then compare descriptions
            int typeComparison = ModelType.compare(o1.getModelType(), o2.getModelType());
            if (typeComparison == 0) {
                return o1.getDescription().compareToIgnoreCase(o2.getDescription());
            }
            return typeComparison;
        });
        List<String> disabledModelIds = SettingsService.getInstance(project).getDisabledModelIds();
        allRegisteredModels.forEach(m -> {
                Optional<Model> editedModelOptional = SettingsService.getInstance(project)
                    .getEditedModels()
                    .stream()
                    .filter(model -> model.getId().equals(m.getId()))
                    .findFirst();
                if (editedModelOptional.isPresent()) {
                    Model editedModel = editedModelOptional.get();
                    settingsTableModel.addRow(new Object[]{
                        InMemoryIconLoader.getIcon(editedModel),
                        !disabledModelIds.contains(m.getId()),
                        editedModel.getDescription(),
                        true,
                        m.getId()});
                } else {
                    settingsTableModel.addRow(new Object[]{
                        InMemoryIconLoader.getIcon(m),
                        !disabledModelIds.contains(m.getId()),
                        m.getDescription(),
                        false,
                        m.getId()});
                }
            }
        );
        table.setModel(settingsTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (currentSelected != -1) {
            table.setRowSelectionInterval(currentSelected, currentSelected);
        }
        table.setRowHeight(28);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_ROW_NUMBER).setMaxWidth(28);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_ROW_NUMBER).setWidth(28);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_ENABLED_ROW_NUMBER).setWidth(28);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_ENABLED_ROW_NUMBER).setMaxWidth(28);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_LABEL_ROW_NUMBER).sizeWidthToFit();
        table.getColumnModel().getColumn(SettingsTableModel.ICON_EDITED_ROW_NUMBER).setWidth(56);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_EDITED_ROW_NUMBER).setMaxWidth(56);
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(SettingsTableModel.ICON_ID_ROW_NUMBER)); // set invisible but keep data
        settingsTableModel.addTableModelListener(e -> modified = true);
    }


    private void loadIgnoredPattern() {
        ignoredPatternTextField.setText(SettingsService.getInstance(project).getIgnoredPattern());
        ignoredPatternTextField.getDocument().addDocumentListener((SimpleDocumentListener) e -> modified = true);
    }
}
