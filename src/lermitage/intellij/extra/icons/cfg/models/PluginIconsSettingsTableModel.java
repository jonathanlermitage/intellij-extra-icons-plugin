package lermitage.intellij.extra.icons.cfg.models;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PluginIconsSettingsTableModel extends DefaultTableModel {

    public static int ICON_ROW_NUMBER = 0;
    public static int ICON_ENABLED_ROW_NUMBER = 1;
    public static int ICON_LABEL_ROW_NUMBER = 2;
    public static int ICON_ID_ROW_NUMBER = 3;

    /**
     * Table columns type.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private final Class<Object>[] types = new Class[]{Icon.class, Boolean.class, String.class, String.class};

    /**
     * Indicates if table columns are editable.
     */
    private final boolean[] canEdit = new boolean[]{false, true, false, false};

    public PluginIconsSettingsTableModel() {
        super(new Object[][]{}, new String[]{"", "", "Description", ""});
    }

    @Override
    public Class<Object> getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
    }
}
