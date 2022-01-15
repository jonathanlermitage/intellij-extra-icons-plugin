// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.table.JBTable;
import lermitage.intellij.extra.icons.utils.IconUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelType;
import lermitage.intellij.extra.icons.cfg.dialogs.ModelDialog;
import lermitage.intellij.extra.icons.cfg.models.PluginIconsSettingsTableModel;
import lermitage.intellij.extra.icons.cfg.models.UserIconsSettingsTableModel;
import lermitage.intellij.extra.icons.cfg.services.impl.SettingsProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.intellij.lang.regexp.RegExpFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SettingsForm implements Configurable, Configurable.NoScroll {

    private static final Logger LOGGER = Logger.getInstance(SettingsForm.class);

    private JPanel pane;
    private JBLabel title;
    private JButton buttonEnableAll;
    private JButton buttonDisableAll;
    private JCheckBox overrideSettingsCheckbox;
    private EditorTextField ignoredPatternTextField;
    private JBLabel ignoredPatternTitle;
    private JTabbedPane iconsTabbedPane;
    private JBTable pluginIconsTable;
    private JPanel userIconsTablePanel;
    private final JBTable userIconsTable = new JBTable();
    private JPanel overrideSettingsPanel;
    private JCheckBox addToIDEUserIconsCheckbox;
    private JLabel filterLabel;
    private EditorTextField filterTextField;
    private JButton filterResetBtn;
    private JBLabel bottomTip;
    private JLabel additionalUIScaleTitle;
    private EditorTextField additionalUIScaleTextField;

    private PluginIconsSettingsTableModel pluginIconsSettingsTableModel;
    private UserIconsSettingsTableModel userIconsSettingsTableModel;
    private Project project;
    private boolean isProjectForm = false;
    private List<Model> customModels = new ArrayList<>();

    public SettingsForm() {
        buttonEnableAll.addActionListener(e -> enableAll());
        buttonDisableAll.addActionListener(e -> disableAll());
        filterResetBtn.addActionListener(e -> resetFilter());
        filterTextField.addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                DocumentListener.super.documentChanged(event);
                applyFilter();
            }
        });
    }

    public SettingsForm(Project project) {
        this();
        this.project = project;
        this.isProjectForm = true;
    }

    public boolean isProjectForm() {
        return isProjectForm;
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
        return !ignoredPatternTextField.getText().equals(service.getIgnoredPattern())
            || !additionalUIScaleTextField.getText().equals(Double.toString(service.additionalUIScale));
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
        try {
            service.setAdditionalUIScale(Double.valueOf(additionalUIScaleTextField.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                "Ignoring invalid value '" + additionalUIScaleTextField.getText() + "' for Additional UI Scale Factor.\n" +
                    "Please use a valid Float value next time, like 1 or 1.25.",
                "Invalid Additional UI Scale Factor",
                JOptionPane.WARNING_MESSAGE);
        }
        List<Boolean> enabledStates = collectUserIconEnabledStates();
        for (int i = 0; i < customModels.size(); i++) {
            Model model = customModels.get(i);
            model.setEnabled(enabledStates.get(i));
        }
        service.setCustomModels(customModels);

        if (isProjectForm) {
            ProjectUtils.refresh(project);
        } else {
            ProjectUtils.refreshOpenedProjects();
        }
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
        ignoredPatternTextField.setToolTipText("<html>Regex is a <b>Java regex</b>, and file path is <b>lowercased</b> before check.</html>");
        additionalUIScaleTitle.setText("Additional UI Scale Factor to adjust user icons size:");
        additionalUIScaleTextField.setToolTipText("<html>Useful if you run IDE with <b>-Dsun.java2d.uiScale.enabled=false</b> " +
            "flag and <b>user icons are too small</b>.<br>Float value. Defaults to <b>1.0</b>.</html>");
        filterLabel.setText("Regex to filter Plugin icons table by:");
        filterTextField.setText("");
        filterTextField.setToolTipText("<html>Regex is a <b>Java regex</b> and <b>is not case-sensitive</b><br>" +
            "You can also type <b>yes</b> or <b>no</b> to find the icons that are enabled or disabled.</html>");
        filterResetBtn.setText("Reset filter");
        bottomTip.setText("<html><b>Icons are ordered by priority</b>. To use an <i>alternative</i> icon (as for Markdown files), " +
            "deactivate the icon(s) with higher priority.</html>");
        initCheckbox();
        loadPluginIconsTable();
        userIconsTable.setShowHorizontalLines(false);
        userIconsTable.setShowVerticalLines(false);
        userIconsTable.setFocusable(false);
        userIconsTable.setRowSelectionAllowed(true);
        userIconsTablePanel.add(createToolbarDecorator());
        loadUserIconsTable();
        loadIgnoredPattern();
        loadAdditionalUIScale();
        if (isProjectForm) {
            additionalUIScaleTitle.setVisible(false);
            additionalUIScaleTextField.setVisible(false);
        }
    }

    private void createUIComponents() {
        // Use default project here because project is not available yet
        ignoredPatternTextField = new EditorTextField("", ProjectManager.getInstance().getDefaultProject(), RegExpFileType.INSTANCE);
        ignoredPatternTextField.setFontInheritedFromLAF(false);
        filterTextField = new EditorTextField("", ProjectManager.getInstance().getDefaultProject(), RegExpFileType.INSTANCE);
        filterTextField.setFontInheritedFromLAF(false);
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
        addToIDEUserIconsCheckbox.setText("Don't overwrite IDE user icons");
        addToIDEUserIconsCheckbox.setToolTipText("If unchecked, project user icons will overwrite IDE user icons");
        boolean shouldAdd = SettingsProjectService.getInstance(project).isAddToIDEUserIcons();
        addToIDEUserIconsCheckbox.setSelected(shouldAdd);
    }

    private void setComponentState(boolean enabled) {
        buttonEnableAll.setEnabled(enabled);
        buttonDisableAll.setEnabled(enabled);
        pluginIconsTable.setEnabled(enabled);
        userIconsTable.setEnabled(enabled);
        ignoredPatternTitle.setEnabled(enabled);
        ignoredPatternTextField.setEnabled(enabled);
        additionalUIScaleTitle.setEnabled(enabled);
        additionalUIScaleTextField.setEnabled(enabled);
        title.setEnabled(enabled);
        iconsTabbedPane.setEnabled(enabled);
        addToIDEUserIconsCheckbox.setEnabled(enabled);
        filterTextField.setEnabled(enabled);
        filterResetBtn.setEnabled(enabled);
    }

    @Override
    public void reset() {
        initCheckbox();
        loadPluginIconsTable();
        loadUserIconsTable();
        loadIgnoredPattern();
        loadAdditionalUIScale();
    }

    private void loadUserIconsTable() {
        customModels = new ArrayList<>(SettingsService.getInstance(project).getCustomModels());
        foldersFirst(customModels);
        setUserIconsTableModel();
    }

    private void setUserIconsTableModel() {
        int currentSelected = userIconsSettingsTableModel != null ? userIconsTable.getSelectedRow() : -1;
        userIconsSettingsTableModel = new UserIconsSettingsTableModel();
        Double additionalUIScale = SettingsService.getIDEInstance().getAdditionalUIScale();
        customModels.forEach(m -> userIconsSettingsTableModel.addRow(new Object[]{
                IconUtils.getIcon(m, additionalUIScale),
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

    private void applyFilter() {
        String filter = filterTextField.getText();
        TableRowSorter<PluginIconsSettingsTableModel> sorter = new TableRowSorter<>(((PluginIconsSettingsTableModel) pluginIconsTable.getModel()));
        if (StringUtil.isEmpty(filter)) {
            filter = ".*";
        }
        try {
            sorter.setStringConverter(new TableStringConverter() {
                @Override
                public String toString(TableModel model, int row, int column) {
                    String desc = model.getValueAt(row, PluginIconsSettingsTableModel.ICON_LABEL_ROW_NUMBER).toString();
                    return desc + " " + desc.toLowerCase(Locale.ENGLISH) + " " + desc.toUpperCase(Locale.ENGLISH);
                }
            });
            // "yes"/"no" filter to filter by icons enabled/disabled, otherwise regex filter
            boolean isYesFilter = "yes".equalsIgnoreCase(filter);
            if (isYesFilter || "no".equalsIgnoreCase(filter)) {
                sorter.setRowFilter(new RowFilter<>() {
                    @Override
                    public boolean include(Entry<? extends PluginIconsSettingsTableModel, ? extends Integer> entry) {
                        boolean iconEnabled = ((boolean) entry.getValue(PluginIconsSettingsTableModel.ICON_ENABLED_ROW_NUMBER));
                        if (isYesFilter) {
                            return iconEnabled;
                        } else {
                            return !iconEnabled;
                        }
                    }
                });
            } else {
                sorter.setRowFilter(RowFilter.regexFilter(filter));
            }
            pluginIconsTable.setRowSorter(sorter);
        } catch (PatternSyntaxException pse) {
            LOGGER.debug(pse);
        }
    }

    private void resetFilter() {
        filterTextField.setText("");
        applyFilter();
    }

    private void loadPluginIconsTable() {
        int currentSelected = pluginIconsSettingsTableModel != null ? pluginIconsTable.getSelectedRow() : -1;
        pluginIconsSettingsTableModel = new PluginIconsSettingsTableModel();
        List<Model> allRegisteredModels = SettingsService.getAllRegisteredModels();
        if (isProjectForm) {
            // IDE icon overrides work at IDE level only, not a project level, that's why
            // the project-level icons list won't show IDE icons.
            allRegisteredModels = allRegisteredModels.stream()
                .filter(model -> model.getModelType() != ModelType.ICON)
                .collect(Collectors.toList());
        }
        foldersFirst(allRegisteredModels);
        List<String> disabledModelIds = SettingsService.getInstance(project).getDisabledModelIds();
        Double additionalUIScale = SettingsService.getIDEInstance().getAdditionalUIScale();
        allRegisteredModels.forEach(m -> pluginIconsSettingsTableModel.addRow(new Object[]{
                IconUtils.getIcon(m, additionalUIScale),
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

    private void loadAdditionalUIScale() {
        additionalUIScaleTextField.setText(Double.toString(SettingsService.getIDEInstance().getAdditionalUIScale()));
    }

    private JComponent createToolbarDecorator() {
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(userIconsTable)
            .setAddAction(anActionButton -> {
                ModelDialog modelDialog = new ModelDialog(this);
                if (modelDialog.showAndGet()) {
                    Model newModel = modelDialog.getModelFromInput();
                    customModels.add(newModel);
                    foldersFirst(customModels);
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

    private void foldersFirst(List<Model> models) {
        models.sort((o1, o2) -> {
            // folders first, then files
            return ModelType.compare(o1.getModelType(), o2.getModelType());
        });
    }
}
