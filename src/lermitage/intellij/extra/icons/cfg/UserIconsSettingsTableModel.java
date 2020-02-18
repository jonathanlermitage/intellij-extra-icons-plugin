package lermitage.intellij.extra.icons.cfg;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserIconsSettingsTableModel extends DefaultTableModel {

    static int ICON_ROW_NUMBER = 0;
    static int ICON_ENABLED_ROW_NUMBER = 1;
    static int ICON_LABEL_ROW_NUMBER = 2;

    /**
     * Table columns type.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private final Class<Object>[] types = new Class[]{Icon.class, Boolean.class, String.class};

    /**
     * Indicates if table columns are editable.
     */
    private final boolean[] canEdit = new boolean[]{false, true, false};

    UserIconsSettingsTableModel() {
        super(new Object[][]{}, new String[]{"", "", "Description"});
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
