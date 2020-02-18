package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.table.JBTable;
import lermitage.intellij.extra.icons.CustomIconLoader;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelType;
import lermitage.intellij.extra.icons.cfg.settings.SettingsProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.intellij.lang.regexp.RegExpFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public class SettingsForm implements Configurable, Configurable.NoScroll {

    private final JBTable userIconsTable = new JBTable();
    private JPanel pane;
    private JBLabel title;
    private JButton buttonEnableAll;
    private JButton buttonDisableAll;
    private JCheckBox overrideSettingsCheckbox;
    private EditorTextField ignoredPatternTextField;
    private JBLabel ignoredPatternTitle;
    private JTabbedPane iconsTabbedPane;
    private JPanel pluginIconsTablePanel;
    private JBTable pluginIconsTable;
    private JPanel userIconsTablePanel;
    private JPanel overrideSettingsPanel;

    private SettingsTableModel settingsTableModel;
    private Project project;
    private boolean isProjectForm = false;
    private List<Model> addedModels = new ArrayList<>();

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
        SettingsService service = SettingsService.getInstance(project);
        if (isProjectForm) {
            boolean selected = overrideSettingsCheckbox.isSelected();
            if (((SettingsProjectService) service).isOverrideIDESettings() != selected) {
                return true;
            }
        }
        if (!CollectionUtils.isEqualCollection(collectDisabledModelIds(), service.getDisabledModelIds())) {
            return true;
        }
        if (service.getIgnoredPattern() == null && ignoredPatternTextField.getText().isEmpty()) {
            return false;
        }
        return !ignoredPatternTextField.getText().equals(service.getIgnoredPattern());
    }

    private List<String> collectDisabledModelIds() {
        List<String> disabledModelIds = new ArrayList<>();
        for (int settingsTableRow = 0; settingsTableRow < settingsTableModel.getRowCount(); settingsTableRow++) {
            boolean iconEnabled = (boolean) settingsTableModel.getValueAt(settingsTableRow, SettingsTableModel.ICON_ENABLED_ROW_NUMBER);
            if (!iconEnabled) {
                disabledModelIds.add((String) settingsTableModel.getValueAt(settingsTableRow, SettingsTableModel.ICON_ID_ROW_NUMBER));
            }
        }
        return disabledModelIds;
    }

    @Override
    public void apply() {
        SettingsService service = SettingsService.getInstance(project);
        if (isProjectForm) {
            boolean selected = overrideSettingsCheckbox.isSelected();
            ((SettingsProjectService) service).setOverrideIDESettings(selected);
        }
        service.setDisabledModelIds(collectDisabledModelIds());
        service.setIgnoredPattern(ignoredPatternTextField.getText());
    }

    @Nullable
    public Project getProject() {
        return this.project;
    }

    private void initComponents() {
        title.setText("Select extra icons to activate, then hit OK or Apply button:");
        buttonEnableAll.setText("Enable all");
        buttonDisableAll.setText("Disable all");
        ignoredPatternTitle.setText("Regex to ignore relative paths:");
        pluginIconsTable.setShowHorizontalLines(false);
        pluginIconsTable.setShowVerticalLines(false);
        pluginIconsTable.setFocusable(false);
        pluginIconsTable.setEnabled(true);
        pluginIconsTable.setRowSelectionAllowed(true);
        initCheckbox();
        loadPluginIconsTable();
        loadIgnoredPattern();
    }

    private void initCheckbox() {
        if (!isProjectForm) {
            overrideSettingsPanel.setVisible(false);
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
        });
    }

    private void setComponentState(boolean enabled) {
        buttonEnableAll.setEnabled(enabled);
        buttonDisableAll.setEnabled(enabled);
        pluginIconsTable.setEnabled(enabled);
        ignoredPatternTitle.setEnabled(enabled);
        ignoredPatternTextField.setEnabled(enabled);
        title.setEnabled(enabled);
        iconsTabbedPane.setEnabled(enabled);
    }

    @Override
    public void reset() {
        initCheckbox();
        loadPluginIconsTable();
        loadIgnoredPattern();
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

    private void loadPluginIconsTable() {
        int currentSelected = settingsTableModel != null ? pluginIconsTable.getSelectedRow() : -1;
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
        allRegisteredModels.forEach(m -> settingsTableModel.addRow(new Object[] {
                CustomIconLoader.getIcon(m),
                !disabledModelIds.contains(m.getId()),
                m.getDescription(),
                m.getId()
            })
        );
        pluginIconsTable.setModel(settingsTableModel);
        pluginIconsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (currentSelected != -1) {
            pluginIconsTable.setRowSelectionInterval(currentSelected, currentSelected);
        }
        pluginIconsTable.setRowHeight(28);
        pluginIconsTable.getColumnModel().getColumn(SettingsTableModel.ICON_ROW_NUMBER).setMaxWidth(28);
        pluginIconsTable.getColumnModel().getColumn(SettingsTableModel.ICON_ROW_NUMBER).setWidth(28);
        pluginIconsTable.getColumnModel().getColumn(SettingsTableModel.ICON_ENABLED_ROW_NUMBER).setWidth(28);
        pluginIconsTable.getColumnModel().getColumn(SettingsTableModel.ICON_ENABLED_ROW_NUMBER).setMaxWidth(28);
        pluginIconsTable.getColumnModel().getColumn(SettingsTableModel.ICON_LABEL_ROW_NUMBER).sizeWidthToFit();
        pluginIconsTable.getColumnModel().removeColumn(pluginIconsTable.getColumnModel().getColumn(SettingsTableModel.ICON_ID_ROW_NUMBER)); // set invisible but keep data
    }

    protected List<Model> getAddedModels() {
        return addedModels;
    }

    private void loadIgnoredPattern() {
        ignoredPatternTextField.setText(SettingsService.getInstance(project).getIgnoredPattern());
    }

    private JComponent createToolbarDecorator() {
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(pluginIconsTable).setAddAction(anActionButton -> {
            ModelDialog modelDialog = new ModelDialog(this);
            boolean wasOk = modelDialog.showAndGet();
            if (wasOk) {
                Model newModel = modelDialog.getModelFromInput();
                addedModels.add(newModel);
                loadPluginIconsTable();
            }
            // TODO Add and save new model
        }).setButtonComparator("Add");
        return decorator.createPanel();
    }

    private void createUIComponents() {
        // Use default project here because project is not available yet
        ignoredPatternTextField = new EditorTextField("", ProjectManager.getInstance().getDefaultProject(), RegExpFileType.INSTANCE);
        ignoredPatternTextField.setFontInheritedFromLAF(false);
    }
}
