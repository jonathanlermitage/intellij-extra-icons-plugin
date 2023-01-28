// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg;

import com.google.common.base.Strings;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.table.JBTable;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelTag;
import lermitage.intellij.extra.icons.ModelType;
import lermitage.intellij.extra.icons.cfg.dialogs.ModelDialog;
import lermitage.intellij.extra.icons.cfg.models.PluginIconsSettingsTableModel;
import lermitage.intellij.extra.icons.cfg.models.UserIconsSettingsTableModel;
import lermitage.intellij.extra.icons.cfg.services.SettingsProjectService;
import lermitage.intellij.extra.icons.cfg.services.SettingsService;
import lermitage.intellij.extra.icons.enablers.EnablerUtils;
import lermitage.intellij.extra.icons.utils.ComboBoxWithImageItem;
import lermitage.intellij.extra.icons.utils.ComboBoxWithImageRenderer;
import lermitage.intellij.extra.icons.utils.IconUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.intellij.lang.regexp.RegExpFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SettingsForm implements Configurable, Configurable.NoScroll {

    private static final Logger LOGGER = Logger.getInstance(SettingsForm.class);

    private JPanel pane;
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
    private JComboBox<ComboBoxWithImageItem> comboBoxIconsGroupSelector;
    private JLabel disableOrEnableOrLabel;
    private JLabel disableOrEnableLabel;
    private JCheckBox ignoreWarningsCheckBox;
    private JButton buttonReloadProjectsIcons;

    private PluginIconsSettingsTableModel pluginIconsSettingsTableModel;
    private UserIconsSettingsTableModel userIconsSettingsTableModel;
    private Project project;
    private boolean isProjectForm = false;
    private List<Model> customModels = new ArrayList<>();

    private boolean forceUpdate = false;

    public SettingsForm() {
        buttonEnableAll.addActionListener(e -> enableAll(true));
        buttonDisableAll.addActionListener(e -> enableAll(false));
        filterResetBtn.addActionListener(e -> resetFilter());
        filterTextField.addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                DocumentListener.super.documentChanged(event);
                applyFilter();
            }
        });
        buttonReloadProjectsIcons.addActionListener(al -> {
            try {
                EnablerUtils.forceInitAllEnablers();
                ProjectUtils.refreshAllOpenedProjects();
                JOptionPane.showMessageDialog(null,
                    "All icons in project views should have been reloaded.", "Icons reloaded",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                LOGGER.warn("Config updated, but failed to reload icons for project " + project.getName(), e);
                JOptionPane.showMessageDialog(null,
                    "Failed to reload icons, please try again later.", "Icons not reloaded",
                    JOptionPane.ERROR_MESSAGE);
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
        if (forceUpdate) {
            return true;
        }
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
        if (service.getIgnoreWarnings() != ignoreWarningsCheckBox.isSelected()) {
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
            boolean iconEnabled = (boolean) pluginIconsSettingsTableModel.getValueAt(settingsTableRow, PluginIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER);
            if (!iconEnabled) {
                disabledModelIds.add((String) pluginIconsSettingsTableModel.getValueAt(settingsTableRow, PluginIconsSettingsTableModel.ICON_ID_COL_NUMBER));
            }
        }
        return disabledModelIds;
    }

    private List<Boolean> collectUserIconEnabledStates() {
        return IntStream.range(0, userIconsSettingsTableModel.getRowCount()).mapToObj(
            index -> ((boolean) userIconsSettingsTableModel.getValueAt(index, UserIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER))
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
        service.setIgnoreWarnings(ignoreWarningsCheckBox.isSelected());
        List<Boolean> enabledStates = collectUserIconEnabledStates();
        for (int i = 0; i < customModels.size(); i++) {
            Model model = customModels.get(i);
            model.setEnabled(enabledStates.get(i));
        }
        service.setCustomModels(customModels);

        try {
            if (isProjectForm) {
                EnablerUtils.forceInitAllEnablers(project);
                ProjectUtils.refreshProject(project);
            } else {
                EnablerUtils.forceInitAllEnablers();
                ProjectUtils.refreshAllOpenedProjects();
            }
        } catch (Exception e) {
            LOGGER.warn("Config updated, but failed to reload icons for project " + project.getName(), e);
        }

        forceUpdate = false;
    }

    @Nullable
    public Project getProject() {
        return this.project;
    }

    private void initComponents() {
        buttonEnableAll.setText("Enable all...");
        buttonDisableAll.setText("Disable all...");
        ignoredPatternTitle.setText("Regex to ignore relative paths:");
        ignoredPatternTextField.setToolTipText("<html>Regex is a <b>Java regex</b>, and file path is <b>lowercased</b> before check.</html>");
        additionalUIScaleTitle.setText("Additional UI Scale Factor to adjust user icons size:");
        additionalUIScaleTextField.setToolTipText(
            "<html>Useful if you run IDE with <b>-Dsun.java2d.uiScale.enabled=false</b> " +
                "flag and <b>user icons are too small</b>.<br>Float value. Defaults to <b>1.0</b>.</html>");
        ignoreWarningsCheckBox.setText("Ignore plugin's warnings");
        ignoreWarningsCheckBox.setToolTipText(
            "You may see notifications saying that some features have been disabled<br>" +
                "due to plugin or IDE errors, like IDE filename index issues.<br>" +
                "Use this checkbox to silent these notifications.");
        filterLabel.setText("Regex to filter Plugin icons table by:");
        filterTextField.setText("");
        filterTextField.setToolTipText(
            "<html>Regex is a <b>Java regex</b> and <b>is not case-sensitive</b><br>" +
                "You can also type <b>yes</b> or <b>no</b> to find the icons that are enabled or disabled.</html>");
        filterResetBtn.setText("Reset filter");
        bottomTip.setText(
            "<html><b>Icons are ordered by priority</b>. To use an <b>alternative</b> icon (as for Markdown files), " +
                "deactivate the icon(s) with higher priority.<br/>The <b>Restart</b> column indicates if you need to restart " +
                "the IDE to see changes.</html>");
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
        loadIgnoreWarnings();
        if (isProjectForm) {
            additionalUIScaleTitle.setVisible(false);
            additionalUIScaleTextField.setVisible(false);
            ignoreWarningsCheckBox.setVisible(false);
            buttonReloadProjectsIcons.setVisible(false);
        }
        buttonReloadProjectsIcons.setText("Reload projects icons");
        buttonReloadProjectsIcons.setToolTipText("<b>Reload icons in all project views.</b><br>" +
            "Use it if some icons were not loaded due to errors like IDE filename index issues.");
        comboBoxIconsGroupSelector.setRenderer(new ComboBoxWithImageRenderer());
        comboBoxIconsGroupSelector.addItem(new ComboBoxWithImageItem("icons"));
        Arrays.stream(ModelTag.values()).forEach(modelTag -> comboBoxIconsGroupSelector.addItem(new ComboBoxWithImageItem(modelTag, " icons")));
    }

    private void createUIComponents() {
        // Use default project here because project is not available yet
        Project defaultProject = ProjectManager.getInstance().getDefaultProject();
        ignoredPatternTextField = new EditorTextField("", defaultProject, RegExpFileType.INSTANCE);
        ignoredPatternTextField.setFontInheritedFromLAF(false);
        filterTextField = new EditorTextField("", defaultProject, RegExpFileType.INSTANCE);
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
        Stream.of(pluginIconsTable, userIconsTable, ignoredPatternTitle, ignoredPatternTextField,
            iconsTabbedPane, addToIDEUserIconsCheckbox, filterLabel,
            filterTextField, filterResetBtn, buttonEnableAll,
            disableOrEnableOrLabel, buttonDisableAll, disableOrEnableLabel,
            comboBoxIconsGroupSelector).forEach(jComponent -> jComponent.setEnabled(enabled));
    }

    @Override
    public void reset() {
        initCheckbox();
        loadPluginIconsTable();
        loadUserIconsTable();
        loadIgnoredPattern();
        loadAdditionalUIScale();
        loadIgnoreWarnings();
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
        TableColumnModel columnModel = userIconsTable.getColumnModel();
        columnModel.getColumn(UserIconsSettingsTableModel.ICON_COL_NUMBER).setMaxWidth(28);
        columnModel.getColumn(UserIconsSettingsTableModel.ICON_COL_NUMBER).setWidth(28);
        columnModel.getColumn(UserIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER).setWidth(28);
        columnModel.getColumn(UserIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER).setMaxWidth(28);
        columnModel.getColumn(UserIconsSettingsTableModel.ICON_LABEL_COL_NUMBER).sizeWidthToFit();
        if (currentSelected != -1 && currentSelected < userIconsTable.getRowCount()) {
            userIconsTable.setRowSelectionInterval(currentSelected, currentSelected);
        }
    }

    /** Get the selected tag for quick action. Empty if "all icons" is selected, otherwise returns selected tag. */
    private Optional<ModelTag> getSelectedTag() {
        int selectedItemIdx = comboBoxIconsGroupSelector.getSelectedIndex();
        if (selectedItemIdx == 0) {
            return Optional.empty();
        }
        return Optional.of(ModelTag.values()[selectedItemIdx - 1]);
    }

    private void enableAll(boolean enable) {
        boolean isPluginIconsSettingsTableModelSelected = iconsTabbedPane.getSelectedIndex() == 0;
        DefaultTableModel tableModel = isPluginIconsSettingsTableModelSelected ? pluginIconsSettingsTableModel : userIconsSettingsTableModel;
        for (int settingsTableRow = 0; settingsTableRow < tableModel.getRowCount(); settingsTableRow++) {
            Optional<ModelTag> selectedTag = getSelectedTag();
            if (selectedTag.isEmpty()) {
                tableModel.setValueAt(enable, settingsTableRow, PluginIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER); // Enabled column number is the same for both models
            } else if (isPluginIconsSettingsTableModelSelected) {
                @SuppressWarnings("unchecked") List<ModelTag> rowTags = (List<ModelTag>) tableModel.getValueAt(settingsTableRow, PluginIconsSettingsTableModel.ICON_TAGS_ENUM_LIST_COL_NUMBER);
                if (rowTags.contains(selectedTag.get())) {
                    tableModel.setValueAt(enable, settingsTableRow, PluginIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER);
                }
            }
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
                    String desc = model.getValueAt(row, PluginIconsSettingsTableModel.ICON_LABEL_COL_NUMBER).toString();
                    return desc + " " + desc.toLowerCase(Locale.ENGLISH) + " " + desc.toUpperCase(Locale.ENGLISH);
                }
            });
            // "yes"/"no" to filter by icons enabled/disabled, otherwise regex filter
            boolean isYesFilter = "yes".equalsIgnoreCase(filter);
            if (isYesFilter || "no".equalsIgnoreCase(filter)) {
                sorter.setRowFilter(new RowFilter<>() {
                    @Override
                    public boolean include(Entry<? extends PluginIconsSettingsTableModel, ? extends Integer> entry) {
                        boolean iconEnabled = ((boolean) entry.getValue(PluginIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER));
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
        Icon restartIcon = IconLoader.getIcon("extra-icons/plugin-internals/reboot.svg", SettingsForm.class);
        allRegisteredModels.forEach(m -> pluginIconsSettingsTableModel.addRow(new Object[]{
                IconUtils.getIcon(m, additionalUIScale),
                !disabledModelIds.contains(m.getId()),
                m.getDescription(),
                Arrays.toString(m.getTags().stream().map(ModelTag::getName).toArray()).replaceAll("\\[|]*", "").trim(),
                Strings.isNullOrEmpty(m.getIdeIcon()) ? null : restartIcon,
                m.getTags(),
                m.getId()
            })
        );
        pluginIconsTable.setModel(pluginIconsSettingsTableModel);
        pluginIconsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pluginIconsTable.setRowHeight(28);
        TableColumnModel columnModel = pluginIconsTable.getColumnModel();
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_COL_NUMBER).setMaxWidth(28);
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_COL_NUMBER).setWidth(28);
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER).setWidth(28);
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER).setMaxWidth(28);
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_LABEL_COL_NUMBER).sizeWidthToFit();
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_TAGS_LABEL_COL_NUMBER).setMaxWidth(120);
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_TAGS_LABEL_COL_NUMBER).setMinWidth(120);
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_REQUIRE_IDE_RESTART).setMaxWidth(65);
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_REQUIRE_IDE_RESTART).setMinWidth(65);
        // set invisible but keep data
        columnModel.removeColumn(columnModel.getColumn(PluginIconsSettingsTableModel.ICON_ID_COL_NUMBER));
        columnModel.removeColumn(columnModel.getColumn(PluginIconsSettingsTableModel.ICON_TAGS_ENUM_LIST_COL_NUMBER));
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

    private void loadIgnoreWarnings() {
        ignoreWarningsCheckBox.setSelected(SettingsService.getIDEInstance().getIgnoreWarnings());
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
            })

            .setEditAction(anActionButton -> {
                int currentSelected = userIconsTable.getSelectedRow();
                ModelDialog modelDialog = new ModelDialog(this);
                modelDialog.setModelToEdit(customModels.get(currentSelected));
                if (modelDialog.showAndGet()) {
                    Model newModel = modelDialog.getModelFromInput();
                    customModels.set(currentSelected, newModel);
                    setUserIconsTableModel();
                }
            })

            .setRemoveAction(anActionButton -> {
                customModels.remove(userIconsTable.getSelectedRow());
                setUserIconsTableModel();
            })

            .setButtonComparator("Add", "Edit", "Remove")

            .setMoveUpAction(anActionButton -> reorderUserIcons(MoveDirection.UP, userIconsTable.getSelectedRow()))

            .setMoveDownAction(anActionButton -> reorderUserIcons(MoveDirection.DOWN, userIconsTable.getSelectedRow()));
        return decorator.createPanel();
    }

    private void reorderUserIcons(MoveDirection moveDirection, int selectedItemIdx) {
        Model modelToMove = customModels.get(selectedItemIdx);
        int newSelectedItemIdx = moveDirection == MoveDirection.UP ? selectedItemIdx - 1 : selectedItemIdx + 1;
        boolean selectedItemIsEnabled = (boolean) userIconsTable.getValueAt(selectedItemIdx, UserIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER);
        boolean newSelectedItemIsEnabled = (boolean) userIconsTable.getValueAt(newSelectedItemIdx, UserIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER);
        List<Boolean> itemsAreEnabled = new ArrayList<>();
        for (int i = 0; i < userIconsTable.getRowCount(); i++) {
            itemsAreEnabled.add((Boolean) userIconsTable.getValueAt(i, UserIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER));
        }

        customModels.set(selectedItemIdx, customModels.get(newSelectedItemIdx));
        customModels.set(newSelectedItemIdx, modelToMove);
        setUserIconsTableModel();

        // User may have enabled or disabled some items, but changes are not applied yet to customModels, so setUserIconsTableModel will reset
        // the Enabled column to previous state. We need to reapply user changes on this column.
        for (int i = 0; i < userIconsTable.getRowCount(); i++) {
            userIconsTable.setValueAt(itemsAreEnabled.get(i), i, UserIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER);
        }
        userIconsTable.setValueAt(selectedItemIsEnabled, newSelectedItemIdx, UserIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER);
        userIconsTable.setValueAt(newSelectedItemIsEnabled, selectedItemIdx, UserIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER);

        userIconsTable.clearSelection();
        userIconsTable.setRowSelectionInterval(newSelectedItemIdx, newSelectedItemIdx);

        // TODO fix Model and ModelCondition equals & hashCode methods in order
        //  to fix CollectionUtils.isEqualCollection(customModels, service.getCustomModels()).
        //  For now, the comparison returns true when customModels ordering changed. It should return false.
        forceUpdate = true;
    }

    private enum MoveDirection {
        UP,
        DOWN
    }

    private void foldersFirst(List<Model> models) {
        models.sort((o1, o2) -> {
            // folders first, then files
            return ModelType.compare(o1.getModelType(), o2.getModelType());
        });
    }
}
