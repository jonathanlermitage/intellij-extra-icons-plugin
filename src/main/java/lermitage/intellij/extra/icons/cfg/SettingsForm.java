// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg;

import com.google.common.base.Strings;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.table.JBTable;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelTag;
import lermitage.intellij.extra.icons.ModelType;
import lermitage.intellij.extra.icons.UITypeIconsPreference;
import lermitage.intellij.extra.icons.cfg.dialogs.AskSingleTextDialog;
import lermitage.intellij.extra.icons.cfg.dialogs.IconPackUninstallerDialog;
import lermitage.intellij.extra.icons.cfg.dialogs.ModelDialog;
import lermitage.intellij.extra.icons.cfg.models.PluginIconsSettingsTableModel;
import lermitage.intellij.extra.icons.cfg.models.UserIconsSettingsTableModel;
import lermitage.intellij.extra.icons.cfg.services.SettingsIDEService;
import lermitage.intellij.extra.icons.cfg.services.SettingsProjectService;
import lermitage.intellij.extra.icons.cfg.services.SettingsService;
import lermitage.intellij.extra.icons.enablers.EnablerUtils;
import lermitage.intellij.extra.icons.utils.ComboBoxWithImageItem;
import lermitage.intellij.extra.icons.utils.ComboBoxWithImageRenderer;
import lermitage.intellij.extra.icons.utils.FileChooserUtils;
import lermitage.intellij.extra.icons.utils.I18nUtils;
import lermitage.intellij.extra.icons.utils.IconPackUtils;
import lermitage.intellij.extra.icons.utils.IconUtils;
import lermitage.intellij.extra.icons.utils.OS;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;
import java.awt.event.ItemEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SettingsForm implements Configurable, Configurable.NoScroll {

    private static final @NonNls Logger LOGGER = Logger.getInstance(SettingsForm.class);

    private JPanel pane;
    private JButton buttonEnableAll;
    private JButton buttonDisableAll;
    private JCheckBox overrideSettingsCheckbox;
    private JTextField ignoredPatternTextField;
    private JBLabel ignoredPatternTitle;
    private JTabbedPane iconsTabbedPane;
    private JBTable pluginIconsTable;
    private JPanel userIconsTablePanel;
    private final JBTable userIconsTable = new JBTable();
    private JPanel overrideSettingsPanel;
    private JCheckBox addToIDEUserIconsCheckbox;
    private JLabel filterLabel;
    private JTextField filterTextField;
    private JButton filterResetBtn;
    private JBLabel bottomTip;
    private JLabel additionalUIScaleTitle;
    private JTextField additionalUIScaleTextField;
    private JComboBox<ComboBoxWithImageItem> comboBoxIconsGroupSelector;
    private JLabel disableOrEnableOrLabel;
    private JLabel disableOrEnableLabel;
    private JButton buttonReloadProjectsIcons;
    private JLabel iconPackLabel;
    private JButton buttonImportIconPackFromFile;
    private JButton buttonExportUserIconsAsIconPack;
    private JButton buttonUninstallIconPack;
    private JLabel iconPackContextHelpLabel;
    private JButton buttonShowIconPacksFromWeb;
    private JPanel iconPackPanel;
    private JComboBox<ComboBoxWithImageItem> uiTypeSelector;
    private JLabel uiTypeSelectorTitle;
    private JLabel uiTypeSelectorHelpLabel;
    private JTabbedPane mainTabbedPane;
    private JPanel experimentalPanel;
    private JCheckBox useIDEFilenameIndexCheckbox;
    private JBLabel useIDEFilenameIndexTip;
    private JButton detectAdditionalUIScaleButton;

    private PluginIconsSettingsTableModel pluginIconsSettingsTableModel;
    private UserIconsSettingsTableModel userIconsSettingsTableModel;
    private @Nullable Project project;
    private List<Model> customModels = new ArrayList<>();

    private boolean forceUpdate = false;

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    public SettingsForm() {
        buttonEnableAll.addActionListener(e -> enableAll(true));
        buttonDisableAll.addActionListener(e -> enableAll(false));
        filterResetBtn.addActionListener(e -> resetFilter());
        filterTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }
        });
        buttonReloadProjectsIcons.addActionListener(al -> {
            try {
                EnablerUtils.forceInitAllEnablers();
                ProjectUtils.refreshAllOpenedProjects();
                Messages.showInfoMessage(
                    i18n.getString("icons.reloaded"),
                    i18n.getString("icons.reloaded.title")
                );
            } catch (Exception e) {
                LOGGER.warn("Config updated, but failed to reload icons for project", e);
                Messages.showErrorDialog(
                    i18n.getString("icons.failed.to.reload"),
                    i18n.getString("icons.failed.to.reload.title")
                );
            }
        });
        buttonImportIconPackFromFile.addActionListener(al -> {
            try {
                Optional<String> iconPackPath = FileChooserUtils.chooseFile(i18n.getString("dialog.import.icon.pack.title"),
                    this.pane, "*.json", "json"); //NON-NLS
                if (iconPackPath.isPresent()) {
                    IconPack iconPack = IconPackUtils.fromJsonFile(new File(iconPackPath.get()));
                    for (Model model : iconPack.getModels()) {
                        if (iconPack.getName() != null && !iconPack.getName().isBlank()) {
                            model.setIconPack(iconPack.getName());
                        }
                        customModels.add(model);
                    }
                    foldersFirst(customModels);
                    setUserIconsTableModel();
                    apply();
                    Messages.showInfoMessage(
                        i18n.getString("dialog.import.icon.pack.success"),
                        i18n.getString("dialog.import.icon.pack.success.title")
                    );
                }
            } catch (Exception e) {
                LOGGER.error("Failed to import Icon Pack", e); // TODO replace by error dialog
            }
        });
        buttonShowIconPacksFromWeb.addActionListener(al ->
            BrowserUtil.browse("https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/themes/THEMES.md#downloadable-icon-packs"));
        buttonExportUserIconsAsIconPack.addActionListener(al -> {
            try {
                Optional<String> folderPath = FileChooserUtils.chooseFolder(i18n.getString("dialog.export.icon.pack.title"), this.pane);
                if (folderPath.isPresent()) {
                    String filename = "extra-icons-" + System.currentTimeMillis() + "-icon-pack.json"; //NON-NLS
                    AskSingleTextDialog askSingleTextDialog = new AskSingleTextDialog( // TODO replace by Messages.showInputDialog
                        i18n.getString("dialog.export.ask.icon.pack.name.window.title"),
                        i18n.getString("dialog.export.ask.icon.pack.name.title"));
                    String iconPackName = "";
                    if (askSingleTextDialog.showAndGet()) {
                        iconPackName = askSingleTextDialog.getTextFromInput();
                    }
                    File exportFile = new File(folderPath.get() + "/" + filename);
                    IconPackUtils.writeToJsonFile(exportFile, new IconPack(iconPackName, SettingsService.getBestSettingsService(project, false).getCustomModels()));
                    Messages.showInfoMessage(
                        i18n.getString("dialog.export.icon.pack.success") + "\n" + exportFile.getAbsolutePath(),
                        i18n.getString("dialog.export.icon.pack.success.title")
                    );
                }
            } catch (Exception e) {
                LOGGER.error("Failed to export user icons", e); // TODO replace by error dialog
            }
        });
        buttonUninstallIconPack.addActionListener(al -> {
            try {
                apply();
                IconPackUninstallerDialog iconPackUninstallerDialog = new IconPackUninstallerDialog(customModels);
                if (iconPackUninstallerDialog.showAndGet()) {
                    String iconPackToUninstall = iconPackUninstallerDialog.getIconPackNameFromInput();
                    if (!iconPackToUninstall.isBlank()) {
                        customModels = customModels.stream()
                            .filter(model -> !iconPackToUninstall.equals(model.getIconPack()))
                            .collect(Collectors.toList());
                        foldersFirst(customModels);
                        setUserIconsTableModel();
                        apply();
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to uninstall Icon Pack", e); // TODO replace by error dialog
            }
        });
        detectAdditionalUIScaleButton.addActionListener(al -> {
            String uiScale = Double.toString(SettingsService.DEFAULT_ADDITIONAL_UI_SCALE);
            additionalUIScaleTextField.setText(uiScale);
            additionalUIScaleTextField.grabFocus();
            Messages.showInfoMessage(
                MessageFormat.format(i18n.getString("btn.scalefactor.detect.infomessage.message"), uiScale),
                i18n.getString("btn.scalefactor.detect.infomessage.title"));
        });
    }

    public SettingsForm(@NotNull Project project) {
        this();
        this.project = project;
    }

    public boolean isProjectForm() {
        return project != null;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Extra Icons"; //NON-NLS
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

        SettingsService bestSettingsService = SettingsService.getBestSettingsService(project, false);
        SettingsIDEService settingsIDEService = SettingsIDEService.getInstance();

        if (isProjectForm()) {
            //noinspection DataFlowIssue
            SettingsProjectService projectService = SettingsProjectService.getInstance(project);
            if (projectService.isOverrideIDESettings() != overrideSettingsCheckbox.isSelected()) {
                return true;
            }
            if (projectService.isAddToIDEUserIcons() != addToIDEUserIconsCheckbox.isSelected()) {
                return true;
            }
        }
        if (!CollectionUtils.isEqualCollection(collectDisabledModelIds(), bestSettingsService.getDisabledModelIds())) {
            return true;
        }
        if (!CollectionUtils.isEqualCollection(customModels, bestSettingsService.getCustomModels())) {
            return true;
        }
        if (!CollectionUtils.isEqualCollection(customModels.stream().map(Model::isEnabled).collect(Collectors.toList()), collectUserIconEnabledStates())) {
            return true;
        }
        if (settingsIDEService.getUiTypeIconsPreference() != getSelectedUITypeIconsPreference()) {
            return true;
        }
        if (!bestSettingsService.getIgnoredPattern().equals(ignoredPatternTextField.getText())) {
            return true;
        }
        if (settingsIDEService.getUseIDEFilenameIndex() != useIDEFilenameIndexCheckbox.isSelected()) {
            return true;
        }
        return !ignoredPatternTextField.getText().equals(bestSettingsService.getIgnoredPattern())
            || !additionalUIScaleTextField.getText().equals(Double.toString(settingsIDEService.getAdditionalUIScale2()));
    }

    private List<String> collectDisabledModelIds() {
        if (pluginIconsSettingsTableModel == null) {
            return Collections.emptyList();
        }

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
        if (userIconsSettingsTableModel == null) {
            return Collections.emptyList();
        }

        return IntStream.range(0, userIconsSettingsTableModel.getRowCount()).mapToObj(
            index -> ((boolean) userIconsSettingsTableModel.getValueAt(index, UserIconsSettingsTableModel.ICON_ENABLED_COL_NUMBER))
        ).collect(Collectors.toList());
    }

    private UITypeIconsPreference getSelectedUITypeIconsPreference() {
        int selectedIndex = uiTypeSelector.getSelectedIndex();
        if (selectedIndex == 0) {
            return UITypeIconsPreference.BASED_ON_ACTIVE_UI_TYPE;
        } else if (selectedIndex == 1) {
            return UITypeIconsPreference.PREFER_OLD_UI_ICONS;
        } else {
            return UITypeIconsPreference.PREFER_NEW_UI_ICONS;
        }
    }

    private void setSelectedUITypeIconsPreference(UITypeIconsPreference uiTypeIconsPreference) {
        switch (uiTypeIconsPreference) {
            case BASED_ON_ACTIVE_UI_TYPE -> uiTypeSelector.setSelectedIndex(0);
            case PREFER_OLD_UI_ICONS -> uiTypeSelector.setSelectedIndex(1);
            case PREFER_NEW_UI_ICONS -> uiTypeSelector.setSelectedIndex(2);
        }
    }

    @Override
    public void apply() {
        SettingsService bestSettingsService = SettingsService.getBestSettingsService(project, false);
        SettingsIDEService settingsIDEService = SettingsIDEService.getInstance();

        if (isProjectForm()) {
            //noinspection DataFlowIssue
            SettingsProjectService projectService = SettingsProjectService.getInstance(project);
            projectService.setOverrideIDESettings(overrideSettingsCheckbox.isSelected());
            projectService.setAddToIDEUserIcons(addToIDEUserIconsCheckbox.isSelected());
        }
        settingsIDEService.setUseIDEFilenameIndex(useIDEFilenameIndexCheckbox.isSelected());
        settingsIDEService.setUiTypeIconsPreference(getSelectedUITypeIconsPreference());
        bestSettingsService.setDisabledModelIds(collectDisabledModelIds());
        bestSettingsService.setIgnoredPattern(ignoredPatternTextField.getText());
        try {
            settingsIDEService.setAdditionalUIScale2(Double.valueOf(additionalUIScaleTextField.getText()));
        } catch (NumberFormatException e) {
            Messages.showErrorDialog(
                MessageFormat.format(i18n.getString("invalid.ui.scalefactor"), additionalUIScaleTextField.getText()),
                i18n.getString("invalid.ui.scalefactor.title")
            );
        }
        List<Boolean> enabledStates = collectUserIconEnabledStates();
        for (int i = 0; i < customModels.size(); i++) {
            Model model = customModels.get(i);
            model.setEnabled(enabledStates.get(i));
        }
        bestSettingsService.setCustomModels(customModels);

        try {
            if (isProjectForm()) {
                EnablerUtils.forceInitAllEnablers(project);
                ProjectUtils.refreshProject(project);
            } else {
                EnablerUtils.forceInitAllEnablers();
                ProjectUtils.refreshAllOpenedProjects();
            }
        } catch (Exception e) {
            LOGGER.warn("Config updated, but failed to reload icons", e);
        }

        forceUpdate = false;
    }

    @Nullable
    public Project getProject() {
        return this.project;
    }

    private void initComponents() {
        uiTypeSelector.setRenderer(new ComboBoxWithImageRenderer());
        uiTypeSelector.addItem(new ComboBoxWithImageItem(
            "extra-icons/plugin-internals/auto.svg", //NON-NLS
            i18n.getString("uitype.selector.auto.select")));
        uiTypeSelector.addItem(new ComboBoxWithImageItem(
            "extra-icons/plugin-internals/folder_oldui.svg",//NON-NLS
            i18n.getString("uitype.selector.prefer.old")));
        uiTypeSelector.addItem(new ComboBoxWithImageItem(
            "extra-icons/plugin-internals/folder_newui.svg", //NON-NLS
            i18n.getString("uitype.selector.prefer.new")));
        setSelectedUITypeIconsPreference(SettingsIDEService.getInstance().getUiTypeIconsPreference());

        disableOrEnableLabel.setText(i18n.getString("quick.action.label"));

        buttonEnableAll.setText(i18n.getString("btn.enable.all"));
        buttonEnableAll.setIcon(IconLoader.getIcon("extra-icons/plugin-internals/gutterCheckBoxSelected.svg", SettingsForm.class)); //NON-NLS

        buttonDisableAll.setText(i18n.getString("btn.disable.all"));
        buttonDisableAll.setIcon(IconLoader.getIcon("extra-icons/plugin-internals/gutterCheckBox.svg", SettingsForm.class)); //NON-NLS

        ignoredPatternTitle.setText(i18n.getString("label.regex.ignore.relative.paths"));
        ignoredPatternTextField.setToolTipText(i18n.getString("field.regex.ignore.relative.paths"));
        additionalUIScaleTitle.setText(i18n.getString("label.ui.scalefactor"));
        additionalUIScaleTextField.setToolTipText(i18n.getString("field.ui.scalefactor"));
        detectAdditionalUIScaleButton.setText(i18n.getString("btn.scalefactor.detect"));
        additionalUIScaleTextField.setColumns(4);
        filterLabel.setText(i18n.getString("plugin.icons.table.filter"));
        filterTextField.setText("");
        filterTextField.setToolTipText(i18n.getString("plugin.icons.table.filter.tooltip"));
        filterResetBtn.setText(i18n.getString("btn.plugin.icons.table.filter.reset"));
        bottomTip.setText(i18n.getString("plugin.icons.table.bottom.tip"));

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

        if (isProjectForm()) {
            additionalUIScaleTitle.setVisible(false);
            additionalUIScaleTextField.setVisible(false);
            buttonReloadProjectsIcons.setVisible(false);
            iconPackPanel.setVisible(false);
            uiTypeSelector.setVisible(false);
            uiTypeSelectorTitle.setVisible(false);
            uiTypeSelectorHelpLabel.setVisible(false);
            experimentalPanel.setVisible(false);
        }

        overrideSettingsCheckbox.setText(i18n.getString("checkbox.override.ide.settings"));
        overrideSettingsCheckbox.setToolTipText(i18n.getString("checkbox.override.ide.settings.tooltip"));
        overrideSettingsCheckbox.addItemListener(item -> {
            boolean enabled = item.getStateChange() == ItemEvent.SELECTED;
            setComponentState(enabled);
        });

        addToIDEUserIconsCheckbox.setText(i18n.getString("checkbox.dont.overwrite.ide.user.icons"));
        addToIDEUserIconsCheckbox.setToolTipText(i18n.getString("checkbox.dont.overwrite.ide.user.icons.tooltip"));

        buttonReloadProjectsIcons.setText(i18n.getString("btn.reload.project.icons"));
        buttonReloadProjectsIcons.setToolTipText(i18n.getString("btn.reload.project.icons.tooltip"));
        buttonReloadProjectsIcons.setIcon(IconLoader.getIcon("extra-icons/plugin-internals/refresh.svg", SettingsForm.class)); //NON-NLS

        comboBoxIconsGroupSelector.setRenderer(new ComboBoxWithImageRenderer());
        comboBoxIconsGroupSelector.addItem(new ComboBoxWithImageItem(i18n.getString("icons")));
        Arrays.stream(ModelTag.values()).forEach(modelTag -> comboBoxIconsGroupSelector.addItem(
            new ComboBoxWithImageItem(modelTag, MessageFormat.format(i18n.getString("icons.tag.name"), modelTag.getName()))
        ));

        iconPackLabel.setText(i18n.getString("icon.pack.label"));

        buttonImportIconPackFromFile.setText(i18n.getString("btn.import.icon.pack.file"));
        buttonImportIconPackFromFile.setIcon(IconLoader.getIcon("extra-icons/plugin-internals/import.svg", SettingsForm.class)); //NON-NLS

        buttonShowIconPacksFromWeb.setText(i18n.getString("btn.import.icon.pack.web"));
        buttonShowIconPacksFromWeb.setIcon(IconLoader.getIcon("extra-icons/plugin-internals/web.svg", SettingsForm.class)); //NON-NLS
        buttonShowIconPacksFromWeb.setToolTipText(i18n.getString("btn.import.icon.pack.web.tooltip"));

        buttonExportUserIconsAsIconPack.setText(i18n.getString("btn.export.icon.pack"));
        buttonExportUserIconsAsIconPack.setIcon(IconLoader.getIcon("extra-icons/plugin-internals/export.svg", SettingsForm.class)); //NON-NLS

        iconPackContextHelpLabel.setText("");
        iconPackContextHelpLabel.setIcon(IconLoader.getIcon("extra-icons/plugin-internals/contextHelp.svg", SettingsForm.class)); //NON-NLS
        iconPackContextHelpLabel.setToolTipText(i18n.getString("icon.pack.context.help"));

        uiTypeSelectorTitle.setText(i18n.getString("uitype.selector.context.title"));
        uiTypeSelectorHelpLabel.setText("");
        uiTypeSelectorHelpLabel.setIcon(IconLoader.getIcon("extra-icons/plugin-internals/contextHelp.svg", SettingsForm.class)); //NON-NLS
        uiTypeSelectorHelpLabel.setToolTipText(i18n.getString("uitype.selector.context.help"));

        buttonUninstallIconPack.setText(i18n.getString("btn.uninstall.icon.pack"));
        buttonUninstallIconPack.setIcon(IconLoader.getIcon("extra-icons/plugin-internals/remove.svg", SettingsForm.class)); //NON-NLS

        mainTabbedPane.setTitleAt(0, "  " + i18n.getString("main.pane.main.config.title") + "  ");
        mainTabbedPane.setTitleAt(1, "  " + i18n.getString("main.pane.advanced.config.title") + "  ");

        iconsTabbedPane.setTitleAt(0, "  " + i18n.getString("plugin.icons.table.tab.name") + "  ");
        iconsTabbedPane.setTitleAt(1, "  " + i18n.getString("user.icons.table.tab.name") + "  ");

        experimentalPanel.setBorder(IdeBorderFactory.createTitledBorder(i18n.getString("experimental.panel.title")));

        useIDEFilenameIndexCheckbox.setText(i18n.getString("checkbox.use.ide.filename.index.label"));
        useIDEFilenameIndexTip.setText(i18n.getString("checkbox.use.ide.filename.index.tip"));

        initCheckbox();
    }

    private void createUIComponents() {
        // Use default project here because project is not available yet
    }

    private void initCheckbox() {
        if (!isProjectForm()) {
            useIDEFilenameIndexCheckbox.setSelected(SettingsIDEService.getInstance().getUseIDEFilenameIndex());
            overrideSettingsPanel.setVisible(false);
            return;
        }
        //noinspection DataFlowIssue  project is not null here
        SettingsProjectService settingsService = SettingsProjectService.getInstance(project);
        boolean shouldOverride = settingsService.isOverrideIDESettings();
        overrideSettingsCheckbox.setSelected(shouldOverride);
        setComponentState(shouldOverride);
        boolean shouldAdd = settingsService.isAddToIDEUserIcons();
        addToIDEUserIconsCheckbox.setSelected(shouldAdd);
    }

    private void setComponentState(boolean enabled) {
        Stream.of(pluginIconsTable, userIconsTable, ignoredPatternTitle, ignoredPatternTextField,
            iconsTabbedPane, addToIDEUserIconsCheckbox, filterLabel,
            filterTextField, filterResetBtn, buttonEnableAll,
            disableOrEnableOrLabel, buttonDisableAll, disableOrEnableLabel,
            comboBoxIconsGroupSelector, mainTabbedPane).forEach(jComponent -> jComponent.setEnabled(enabled));
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
        customModels = new ArrayList<>(SettingsService.getBestSettingsService(project, false).getCustomModels());
        foldersFirst(customModels);
        setUserIconsTableModel();
    }

    private void setUserIconsTableModel() {
        SettingsIDEService settingsIDEService = SettingsIDEService.getInstance();

        int currentSelected = userIconsSettingsTableModel != null ? userIconsTable.getSelectedRow() : -1;
        userIconsSettingsTableModel = new UserIconsSettingsTableModel();
        final Double additionalUIScale = settingsIDEService.getAdditionalUIScale2();
        final UITypeIconsPreference uiTypeIconsPreference = settingsIDEService.getUiTypeIconsPreference();
        customModels.forEach(m -> {
                try {
                    userIconsSettingsTableModel.addRow(new Object[]{
                        IconUtils.getIcon(m, additionalUIScale, uiTypeIconsPreference),
                        m.isEnabled(),
                        m.getDescription(),
                        m.getIconPack()
                    });
                } catch (Throwable e) {
                    LOGGER.warn(e);
                }
            }
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
        columnModel.getColumn(UserIconsSettingsTableModel.ICON_PACK_COL_NUMBER).setMinWidth(250);
        columnModel.getColumn(UserIconsSettingsTableModel.ICON_PACK_COL_NUMBER).setMaxWidth(450);
        columnModel.getColumn(UserIconsSettingsTableModel.ICON_PACK_COL_NUMBER).setPreferredWidth(280);
        if (currentSelected != -1 && currentSelected < userIconsTable.getRowCount()) {
            userIconsTable.setRowSelectionInterval(currentSelected, currentSelected);
        }
    }

    /**
     * Get the selected tag for quick action. Empty if "all icons" is selected, otherwise returns selected tag.
     */
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
            boolean isYesFilter = "yes".equalsIgnoreCase(filter); //NON-NLS
            if (isYesFilter || "no".equalsIgnoreCase(filter)) { //NON-NLS
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
        SettingsIDEService settingsIDEService = SettingsIDEService.getInstance();

        int currentSelected = pluginIconsSettingsTableModel != null ? pluginIconsTable.getSelectedRow() : -1;
        pluginIconsSettingsTableModel = new PluginIconsSettingsTableModel();
        List<Model> allRegisteredModels = SettingsService.getAllRegisteredModels();
        if (isProjectForm()) {
            // IDE icon overrides work at IDE level only, not a project level, that's why
            // the project-level icons list won't show IDE icons.
            allRegisteredModels = allRegisteredModels.stream()
                .filter(model -> model.getModelType() != ModelType.ICON)
                .collect(Collectors.toList());
        }
        foldersFirst(allRegisteredModels);
        List<String> disabledModelIds = SettingsService.getBestSettingsService(project, false).getDisabledModelIds();
        final Double additionalUIScale = settingsIDEService.getAdditionalUIScale2();
        final UITypeIconsPreference uiTypeIconsPreference = settingsIDEService.getUiTypeIconsPreference();
        final Icon restartIcon = IconLoader.getIcon("extra-icons/plugin-internals/reboot.svg", SettingsForm.class); //NON-NLS
        allRegisteredModels.forEach(m -> pluginIconsSettingsTableModel.addRow(new Object[]{
                IconUtils.getIcon(m, additionalUIScale, uiTypeIconsPreference),
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
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_TAGS_LABEL_COL_NUMBER).setMaxWidth(120);
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_TAGS_LABEL_COL_NUMBER).setMinWidth(120);
        int requireRestartColWidth = I18nUtils.isChineseUIEnabled() ? 100 : 80;
        if (OS.detectOS() == OS.WIN) {
            requireRestartColWidth -= 5;
        }
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_REQUIRE_IDE_RESTART).setMaxWidth(requireRestartColWidth);
        columnModel.getColumn(PluginIconsSettingsTableModel.ICON_REQUIRE_IDE_RESTART).setMinWidth(requireRestartColWidth);

        // set invisible but keep data
        columnModel.removeColumn(columnModel.getColumn(PluginIconsSettingsTableModel.ICON_ID_COL_NUMBER));
        columnModel.removeColumn(columnModel.getColumn(PluginIconsSettingsTableModel.ICON_TAGS_ENUM_LIST_COL_NUMBER));
        if (currentSelected != -1) {
            pluginIconsTable.setRowSelectionInterval(currentSelected, currentSelected);
        }
    }

    private void loadIgnoredPattern() {
        ignoredPatternTextField.setText(SettingsService.getBestSettingsService(project, false).getIgnoredPattern());
    }

    private void loadAdditionalUIScale() {
        additionalUIScaleTextField.setText(Double.toString(SettingsIDEService.getInstance().getAdditionalUIScale2()));
    }

    private JComponent createToolbarDecorator() {
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(userIconsTable)

            .setAddAction(anActionButton -> {
                ModelDialog modelDialog = new ModelDialog(this, this.project);
                if (modelDialog.showAndGet()) {
                    Model newModel = modelDialog.getModelFromInput();
                    customModels.add(newModel);
                    foldersFirst(customModels);
                    setUserIconsTableModel();
                }
            })

            .setEditAction(anActionButton -> {
                int currentSelected = userIconsTable.getSelectedRow();
                ModelDialog modelDialog = new ModelDialog(this, this.project);
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

            .setButtonComparator(i18n.getString("btn.add"), i18n.getString("btn.edit"), i18n.getString("btn.remove"))

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
