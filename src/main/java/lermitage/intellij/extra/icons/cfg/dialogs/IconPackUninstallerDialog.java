// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.dialogs;

import com.intellij.openapi.ui.DialogWrapper;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.utils.I18nUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * A dialog which asks to choose an icon pack name, or nothing.
 */
public class IconPackUninstallerDialog extends DialogWrapper {

    private final List<Model> models;

    private JPanel pane;
    private JLabel iconPackChooserTitleLabel;
    private JComboBox<String> iconPackComboBox;

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return pane;
    }

    public IconPackUninstallerDialog(List<Model> models) {
        super(true);
        this.models = models;
        init();
        setTitle(i18n.getString("model.dialog.uninstall.icon.pack.window.title"));
        initComponents();
    }

    private void initComponents() {
        iconPackChooserTitleLabel.setText(i18n.getString("model.dialog.uninstall.icon.pack.title"));
        Set<String> iconPacks = new HashSet<>();
        models.forEach(model -> {
            if (model.getIconPack() != null && !model.getIconPack().isBlank()) {
                iconPacks.add(model.getIconPack());
            }
        });
        iconPacks.stream().sorted().forEach(s -> iconPackComboBox.addItem(s));
    }

    public String getIconPackNameFromInput() {
        return iconPackComboBox.getItemAt(iconPackComboBox.getSelectedIndex());
    }
}
