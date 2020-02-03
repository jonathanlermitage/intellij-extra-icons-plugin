package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.table.JBTable;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelType;
import lermitage.intellij.extra.icons.cfg.settings.SettingsProjectService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public class SettingsForm implements Configurable {

    private JPanel pane;
    private JLabel title;
    private JBTable table;
    private JButton buttonEnableAll;
    private JButton buttonDisableAll;
    private JCheckBox overrideSettingsCheckbox;

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
        modified = false;
    }

    private void initComponents() {
        title.setText("Select extra icons to activate, then hit OK or Apply button:");
        buttonEnableAll.setText("Enable all");
        buttonDisableAll.setText("Disable all");
        initCheckbox();
        loadTable();
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
    }

    @Override
    public void reset() {
        initCheckbox();
        loadTable();
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
        allRegisteredModels.forEach(m -> settingsTableModel.addRow(new Object[]{
            IconLoader.getIcon(m.getIcon()),
            !disabledModelIds.contains(m.getId()),
            m.getDescription(),
            m.getId()})
        );
        table.setModel(settingsTableModel);
        table.setRowHeight(28);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_ROW_NUMBER).setMaxWidth(28);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_ROW_NUMBER).setWidth(28);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_ENABLED_ROW_NUMBER).setWidth(28);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_ENABLED_ROW_NUMBER).setMaxWidth(28);
        table.getColumnModel().getColumn(SettingsTableModel.ICON_LABEL_ROW_NUMBER).sizeWidthToFit();
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(SettingsTableModel.ICON_ID_ROW_NUMBER)); // set invisible but keep data
        settingsTableModel.addTableModelListener(e -> modified = true);
        modified = false;
    }
}
