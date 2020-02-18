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
import lermitage.intellij.extra.icons.cfg.dialogs.ModelDialog;
import lermitage.intellij.extra.icons.cfg.models.PluginIconsSettingsTableModel;
import lermitage.intellij.extra.icons.cfg.models.UserIconsSettingsTableModel;
import lermitage.intellij.extra.icons.cfg.services.impl.SettingsProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.intellij.lang.regexp.RegExpFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SettingsForm implements Configurable, Configurable.NoScroll {

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
    private JBTable userIconsTable = new JBTable();
    private JPanel overrideSettingsPanel;
    private JCheckBox addToIDEUserIconsCheckbox;

    private PluginIconsSettingsTableModel pluginIconsSettingsTableModel;
    private UserIconsSettingsTableModel userIconsSettingsTableModel;
    private Project project;
    private boolean isProjectForm = false;
    private List<Model> customModels = new ArrayList<>();

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
            SettingsProjectService projectService = (SettingsProjectService) service;
            if (projectService.isOverrideIDESettings() != overrideSettingsCheckbox.isSelected()) {
                return true;
            }
            if (projectService.isAddToIDEUserIcons() != addToIDEUserIconsCheckbox.isSelected()) {
                return true;
            }
        }
        if (!CollectionUtils.isEqualCollection(collectDisabledModelIds(), service.getDisabledModelIds())) {
            return true;
        }
        if (!CollectionUtils.isEqualCollection(customModels, service.getCustomModels())) {
            return true;
        }
        if (!CollectionUtils.isEqualCollection(customModels.stream().map(Model::isEnabled).collect(Collectors.toList()), collectUserIconEnabledStates())) {
            return true;
        }
        if (service.getIgnoredPattern() == null && ignoredPatternTextField.getText().isEmpty()) {
            return false;
        }
        return !ignoredPatternTextField.getText().equals(service.getIgnoredPattern());
    }

    private List<String> collectDisabledModelIds() {
        List<String> disabledModelIds = new ArrayList<>();
        for (int settingsTableRow = 0; settingsTableRow < pluginIconsSettingsTableModel.getRowCount(); settingsTableRow++) {
            boolean iconEnabled = (boolean) pluginIconsSettingsTableModel.getValueAt(settingsTableRow, PluginIconsSettingsTableModel.ICON_ENABLED_ROW_NUMBER);
            if (!iconEnabled) {
                disabledModelIds.add((String) pluginIconsSettingsTableModel.getValueAt(settingsTableRow, PluginIconsSettingsTableModel.ICON_ID_ROW_NUMBER));
            }
        }
        return disabledModelIds;
    }

    private List<Boolean> collectUserIconEnabledStates() {
        return IntStream.range(0, userIconsSettingsTableModel.getRowCount()).mapToObj(
            index -> ((boolean) userIconsSettingsTableModel.getValueAt(index, UserIconsSettingsTableModel.ICON_ENABLED_ROW_NUMBER))
        ).collect(Collectors.toList());
    }

    @Override
    public void apply() {
        SettingsService service = SettingsService.getInstance(project);
        if (isProjectForm) {
            SettingsProjectService projectService = (SettingsProjectService) service;
            projectService.setOverrideIDESettings(overrideSettingsCheckbox.isSelected());
            projectService.setAddToIDEUserIcons(addToIDEUserIconsCheckbox.isSelected());
        }
        service.setDisabledModelIds(collectDisabledModelIds());
        service.setIgnoredPattern(ignoredPatternTextField.getText());
        List<Boolean> enabledStates = collectUserIconEnabledStates();
        for (int i = 0; i < customModels.size(); i++) {
            Model model = customModels.get(i);
            model.setEnabled(enabledStates.get(i));
        }
        service.setCustomModels(customModels);
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
        initCheckbox();
        loadPluginIconsTable();
        userIconsTable.setShowHorizontalLines(false);
        userIconsTable.setShowVerticalLines(false);
        userIconsTable.setFocusable(false);
        userIconsTable.setRowSelectionAllowed(true);
        userIconsTablePanel.add(createToolbarDecorator());
        loadUserIconsTable();
        loadIgnoredPattern();
    }

    private void createUIComponents() {
        // Use default project here because project is not available yet
        ignoredPatternTextField = new EditorTextField("", ProjectManager.getInstance().getDefaultProject(), RegExpFileType.INSTANCE);
        ignoredPatternTextField.setFontInheritedFromLAF(false);
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
        addToIDEUserIconsCheckbox.setText("Add project user icons to IDE user icons");
        addToIDEUserIconsCheckbox.setToolTipText("If unchecked, project user icons will overwrite IDE user icons");
        boolean shouldAdd = SettingsProjectService.getInstance(project).isAddToIDEUserIcons();
        addToIDEUserIconsCheckbox.setSelected(shouldAdd);
    }

    private void setComponentState(boolean enabled) {
        buttonEnableAll.setEnabled(enabled);
        buttonDisableAll.setEnabled(enabled);
        pluginIconsTable.setEnabled(enabled);
        ignoredPatternTitle.setEnabled(enabled);
        ignoredPatternTextField.setEnabled(enabled);
        title.setEnabled(enabled);
        iconsTabbedPane.setEnabled(enabled);
        addToIDEUserIconsCheckbox.setEnabled(enabled);
    }

    @Override
    public void reset() {
        initCheckbox();
        loadPluginIconsTable();
        loadUserIconsTable();
        loadIgnoredPattern();
    }

    private void loadUserIconsTable() {
        this.customModels = new ArrayList<>(SettingsService.getInstance(project).getCustomModels());
        setUserIconsTableModel();
    }

    private void setUserIconsTableModel() {
        int currentSelected = userIconsSettingsTableModel != null ? userIconsTable.getSelectedRow() : -1;
        userIconsSettingsTableModel = new UserIconsSettingsTableModel();
        customModels.forEach(m -> userIconsSettingsTableModel.addRow(new Object[] {
                CustomIconLoader.getIcon(m),
                m.isEnabled(),
                m.getDescription()
            })
        );
        userIconsTable.setModel(userIconsSettingsTableModel);
        userIconsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userIconsTable.setRowHeight(28);
        userIconsTable.getColumnModel().getColumn(UserIconsSettingsTableModel.ICON_ROW_NUMBER).setMaxWidth(28);
        userIconsTable.getColumnModel().getColumn(UserIconsSettingsTableModel.ICON_ROW_NUMBER).setWidth(28);
        userIconsTable.getColumnModel().getColumn(UserIconsSettingsTableModel.ICON_ENABLED_ROW_NUMBER).setWidth(28);
        userIconsTable.getColumnModel().getColumn(UserIconsSettingsTableModel.ICON_ENABLED_ROW_NUMBER).setMaxWidth(28);
        userIconsTable.getColumnModel().getColumn(UserIconsSettingsTableModel.ICON_LABEL_ROW_NUMBER).sizeWidthToFit();
        if (currentSelected != -1 && currentSelected < userIconsTable.getRowCount()) {
            userIconsTable.setRowSelectionInterval(currentSelected, currentSelected);
        }
    }

    private void enableAll() {
        DefaultTableModel tableModel = iconsTabbedPane.getSelectedIndex() == 0 ? pluginIconsSettingsTableModel : userIconsSettingsTableModel;
        for (int settingsTableRow = 0; settingsTableRow < tableModel.getRowCount(); settingsTableRow++) {
            tableModel.setValueAt(true, settingsTableRow, PluginIconsSettingsTableModel.ICON_ENABLED_ROW_NUMBER); // Enabled row number is the same for both models
        }
    }

    private void disableAll() {
        DefaultTableModel tableModel = iconsTabbedPane.getSelectedIndex() == 0 ? pluginIconsSettingsTableModel : userIconsSettingsTableModel;
        for (int settingsTableRow = 0; settingsTableRow < tableModel.getRowCount(); settingsTableRow++) {
            tableModel.setValueAt(false, settingsTableRow, PluginIconsSettingsTableModel.ICON_ENABLED_ROW_NUMBER); // Enabled row number is the same for both models
        }
    }

    private void loadPluginIconsTable() {
        int currentSelected = pluginIconsSettingsTableModel != null ? pluginIconsTable.getSelectedRow() : -1;
        pluginIconsSettingsTableModel = new PluginIconsSettingsTableModel();
        List<Model> allRegisteredModels = SettingsService.getAllRegisteredModels();
        sortModels(allRegisteredModels);
        List<String> disabledModelIds = SettingsService.getInstance(project).getDisabledModelIds();
        allRegisteredModels.forEach(m -> pluginIconsSettingsTableModel.addRow(new Object[] {
                CustomIconLoader.getIcon(m),
                !disabledModelIds.contains(m.getId()),
                m.getDescription(),
                m.getId()
            })
        );
        pluginIconsTable.setModel(pluginIconsSettingsTableModel);
        pluginIconsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pluginIconsTable.setRowHeight(28);
        pluginIconsTable.getColumnModel().getColumn(PluginIconsSettingsTableModel.ICON_ROW_NUMBER).setMaxWidth(28);
        pluginIconsTable.getColumnModel().getColumn(PluginIconsSettingsTableModel.ICON_ROW_NUMBER).setWidth(28);
        pluginIconsTable.getColumnModel().getColumn(PluginIconsSettingsTableModel.ICON_ENABLED_ROW_NUMBER).setWidth(28);
        pluginIconsTable.getColumnModel().getColumn(PluginIconsSettingsTableModel.ICON_ENABLED_ROW_NUMBER).setMaxWidth(28);
        pluginIconsTable.getColumnModel().getColumn(PluginIconsSettingsTableModel.ICON_LABEL_ROW_NUMBER).sizeWidthToFit();
        pluginIconsTable.getColumnModel().removeColumn(pluginIconsTable.getColumnModel().getColumn(PluginIconsSettingsTableModel.ICON_ID_ROW_NUMBER)); // set invisible but keep data
        if (currentSelected != -1) {
            pluginIconsTable.setRowSelectionInterval(currentSelected, currentSelected);
        }
    }

    private void loadIgnoredPattern() {
        ignoredPatternTextField.setText(SettingsService.getInstance(project).getIgnoredPattern());
    }

    private JComponent createToolbarDecorator() {
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(userIconsTable)
            .setAddAction(anActionButton -> {
                ModelDialog modelDialog = new ModelDialog(this);
                if (modelDialog.showAndGet()) {
                    Model newModel = modelDialog.getModelFromInput();
                    customModels.add(newModel);
                    sortModels(customModels);
                    setUserIconsTableModel();
                }
            }).setEditAction(anActionButton -> {
                int currentSelected = userIconsTable.getSelectedRow();
                ModelDialog modelDialog = new ModelDialog(this);
                modelDialog.setModelToEdit(customModels.get(currentSelected));
                if (modelDialog.showAndGet()) {
                    Model newModel = modelDialog.getModelFromInput();
                    customModels.set(currentSelected, newModel);
                    setUserIconsTableModel();
                }
            }).setRemoveAction(anActionButton -> {
                customModels.remove(userIconsTable.getSelectedRow());
                setUserIconsTableModel();
            }).setButtonComparator("Add", "Edit", "Remove");
        return decorator.createPanel();
    }

    private void sortModels(List<Model> models) {
        models.sort((o1, o2) -> {
            // folders first, then compare descriptions
            int typeComparison = ModelType.compare(o1.getModelType(), o2.getModelType());
            if (typeComparison == 0) {
                return o1.getDescription().compareToIgnoreCase(o2.getDescription());
            }
            return typeComparison;
        });
    }
}
