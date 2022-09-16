// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.models;

import javax.swing.Icon;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PluginIconsSettingsTableModel extends DefaultTableModel {

    public static int ICON_COL_NUMBER = 0;
    public static int ICON_ENABLED_COL_NUMBER = 1;
    public static int ICON_LABEL_COL_NUMBER = 2;
    public static int ICON_TAGS_LABEL_COL_NUMBER = 3;
    public static int ICON_REQUIRE_IDE_RESTART = 4;
    public static int ICON_TAGS_ENUM_LIST_COL_NUMBER = 5;
    public static int ICON_ID_COL_NUMBER = 6;

    /**
     * Table columns type.
     */
    @SuppressWarnings("unchecked")
    private final Class<Object>[] types = new Class[]{Icon.class, Boolean.class, String.class, String.class, Icon.class, List.class, String.class};

    /**
     * Indicates if table columns are editable.
     */
    private final boolean[] canEdit = new boolean[]{false, true, false, false, false, false, false};

    public PluginIconsSettingsTableModel() {
        super(new Object[][]{}, new String[]{"", "", "Description", "Tags", "Restart", "", ""});
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
