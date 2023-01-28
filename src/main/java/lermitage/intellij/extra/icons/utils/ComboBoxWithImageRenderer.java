// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;

/**
 * A {@link JComboBox} renderer with PNG/SVG image + optional text.
 * Example:
 * <pre>
 * myComboBox.setRenderer(new ComboBoxWithImageRenderer());
 * myComboBox.addItem(new ComboBoxWithImageItem("first item", "extra-icons/first_item.svg"));
 * </pre>
 */
public class ComboBoxWithImageRenderer extends JLabel implements ListCellRenderer<Object> {

    private static final Logger LOGGER = Logger.getInstance(ComboBoxWithImageRenderer.class);

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ComboBoxWithImageRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        ComboBoxWithImageItem item = (ComboBoxWithImageItem) value;
        if (item == null) {
            return this;
        }
        setText(item.getTitle());
        try {
            if (item.getImagePath() == null) {
                setIcon(null);
            } else {
                setIcon(IconLoader.getIcon(item.getImagePath(), ComboBoxWithImageRenderer.class));
            }
        } catch (Exception e) {
            setIcon(null);
            LOGGER.warn("Failed to load Tool icon '" + item + "'", e);
        }
        setIconTextGap(6);
        return this;
    }
}
