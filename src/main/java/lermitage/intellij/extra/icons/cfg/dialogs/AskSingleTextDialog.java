// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.dialogs;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A dialog which asks for a single text input. Input can be empty.
 */
public class AskSingleTextDialog extends DialogWrapper {

    private JPanel pane;
    private JTextField textField;
    private JLabel label;

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return pane;
    }

    public AskSingleTextDialog(String windowTitle, String fieldTitle) {
        super(true);
        init();
        setTitle(windowTitle);
        initComponents(fieldTitle);
    }

    private void initComponents(String fieldTitle) {
        label.setText(fieldTitle);
    }

    public String getTextFromInput() {
        return textField.getText();
    }
}
