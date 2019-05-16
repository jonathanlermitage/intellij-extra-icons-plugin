package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.IconLoader;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.util.ArrayList;
import java.util.List;

public class SettingsForm implements Configurable {
    
    private JPanel pane;
    private JLabel title;
    private JTable table;
    private JButton buttonEnableAll;
    private JButton buttonDisableAll;
    
    private SettingsTableModel settingsTableModel;
    private boolean modified = false;
    
    public SettingsForm() {
        buttonEnableAll.addActionListener(e -> enableAll());
        buttonDisableAll.addActionListener(e -> disableAll());
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
        List<String> disabledModelIds = new ArrayList<>();
        for (int settingsTableRow = 0; settingsTableRow < settingsTableModel.getRowCount(); settingsTableRow++) {
            boolean iconEnabled = (boolean) settingsTableModel.getValueAt(settingsTableRow, SettingsTableModel.ICON_ENABLED_ROW_NUMBER);
            if (!iconEnabled) {
                disabledModelIds.add((String) settingsTableModel.getValueAt(settingsTableRow, SettingsTableModel.ICON_ID_ROW_NUMBER));
            }
        }
        SettingsService.setDisabledModelIds(disabledModelIds);
        modified = false;
    }
    
    private void initComponents() {
        title.setText("Select extra icons to activate, then hit OK or Apply button (changes take effect after restart):");
        buttonEnableAll.setText("Enable all");
        buttonDisableAll.setText("Disable all");
        loadTable();
    }
    
    @Override
    public void reset() {
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
        List<String> disabledModelIds = SettingsService.getDisabledModelIds();
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
