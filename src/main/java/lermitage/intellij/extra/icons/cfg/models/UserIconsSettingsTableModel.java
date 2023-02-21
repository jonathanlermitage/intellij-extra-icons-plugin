// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.models;

import lermitage.intellij.extra.icons.utils.I18nUtils;

import javax.swing.Icon;
import javax.swing.table.DefaultTableModel;
import java.util.ResourceBundle;

public class UserIconsSettingsTableModel extends DefaultTableModel {

    public static int ICON_COL_NUMBER = 0;
    public static int ICON_ENABLED_COL_NUMBER = 1;
    public static int ICON_LABEL_COL_NUMBER = 2;

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    /**
     * Table columns type.
     */
    @SuppressWarnings("unchecked")
    private final Class<Object>[] types = new Class[]{Icon.class, Boolean.class, String.class};

    /**
     * Indicates if table columns are editable.
     */
    private final boolean[] canEdit = new boolean[]{false, true, false};

    public UserIconsSettingsTableModel() {
        super(new Object[][]{}, new String[]{"", "",
            i18n.getString("user.icons.table.col.description")});
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
