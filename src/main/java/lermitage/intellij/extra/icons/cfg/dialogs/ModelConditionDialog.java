// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.dialogs;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBLabel;
import lermitage.intellij.extra.icons.ModelCondition;
import lermitage.intellij.extra.icons.utils.I18nUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.event.ItemEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ModelConditionDialog extends DialogWrapper {

    public static final String FIELD_SEPARATOR = ";";
    public static final String FIELD_SEPARATOR_NAME = "semicolon"; //NON-NLS

    private JPanel dialogPanel;
    private JCheckBox regexCheckBox;
    private JTextField regexTextField;
    private JCheckBox parentsCheckBox;
    private JTextField parentsTextField;
    private JCheckBox namesCheckBox;
    private JTextField namesTextField;
    private JCheckBox extensionsCheckBox;
    private JTextField extensionsTextField;
    private JRadioButton mayEndWithRadioButton;
    private JRadioButton endsWithRadioButton;
    private JRadioButton startsWithRadioButton;
    private JRadioButton equalsRadioButton;
    private JCheckBox noDotCheckBox;
    private JBLabel tipsLabel;
    private JCheckBox facetsCheckBox;
    private JTextField facetsTextField;

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    public ModelConditionDialog() {
        super(false);
        init();
        setTitle(i18n.getString("model.condition.dialog.title"));
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        initComponents();
        return dialogPanel;
    }

    private void initComponents() {
        ButtonGroup endButtonGroup = new ButtonGroup();
        endButtonGroup.add(endsWithRadioButton);
        endButtonGroup.add(mayEndWithRadioButton);
        endsWithRadioButton.setSelected(true);
        endsWithRadioButton.setEnabled(false);
        mayEndWithRadioButton.setEnabled(false);

        ButtonGroup namesButtonGroup = new ButtonGroup();
        namesButtonGroup.add(startsWithRadioButton);
        namesButtonGroup.add(equalsRadioButton);
        startsWithRadioButton.setSelected(true);
        startsWithRadioButton.setEnabled(false);
        equalsRadioButton.setEnabled(false);
        noDotCheckBox.setEnabled(false);

        regexCheckBox.addItemListener(item -> {
            boolean selected = item.getStateChange() == ItemEvent.SELECTED;
            regexTextField.setEnabled(selected);
        });

        parentsCheckBox.addItemListener(item -> {
            boolean selected = item.getStateChange() == ItemEvent.SELECTED;
            parentsTextField.setEnabled(selected);
        });

        namesCheckBox.addItemListener(item -> {
            boolean selected = item.getStateChange() == ItemEvent.SELECTED;
            namesTextField.setEnabled(selected);
            startsWithRadioButton.setEnabled(selected);
            equalsRadioButton.setEnabled(selected);
        });

        extensionsCheckBox.addItemListener(item -> {
            boolean selected = item.getStateChange() == ItemEvent.SELECTED;
            extensionsTextField.setEnabled(selected);
            mayEndWithRadioButton.setEnabled(selected);
            endsWithRadioButton.setEnabled(selected);
        });

        startsWithRadioButton.addItemListener(item -> {
            boolean selected = item.getStateChange() == ItemEvent.SELECTED;
            noDotCheckBox.setEnabled(selected);
        });

        startsWithRadioButton.addPropertyChangeListener("enabled", propertyChange ->
            noDotCheckBox.setEnabled((boolean) propertyChange.getNewValue())
        );

        facetsCheckBox.addItemListener(item -> {
            boolean selected = item.getStateChange() == ItemEvent.SELECTED;
            facetsTextField.setEnabled(selected);
        });

        regexTextField.setEnabled(false);
        parentsTextField.setEnabled(false);
        namesTextField.setEnabled(false);
        extensionsTextField.setEnabled(false);
        facetsTextField.setEnabled(false);

        tipsLabel.setText(MessageFormat.format(i18n.getString("model.condition.dialog.tips"), FIELD_SEPARATOR_NAME));

        regexCheckBox.setText(i18n.getString("model.condition.dialog.regex.checkbox"));
        parentsCheckBox.setText(i18n.getString("model.condition.dialog.parents.checkbox"));
        namesCheckBox.setText(i18n.getString("model.condition.dialog.names.checkbox"));
        startsWithRadioButton.setText(i18n.getString("model.condition.dialog.startswith.checkbox"));
        equalsRadioButton.setText(i18n.getString("model.condition.dialog.equals.checkbox"));
        noDotCheckBox.setText(i18n.getString("model.condition.dialog.nodot.checkbox"));
        extensionsCheckBox.setText(i18n.getString("model.condition.dialog.extensions.checkbox"));
        endsWithRadioButton.setText(i18n.getString("model.condition.dialog.endswith.checkbox"));
        mayEndWithRadioButton.setText(i18n.getString("model.condition.dialog.mayendwith.checkbox"));
        facetsCheckBox.setText(i18n.getString("model.condition.dialog.facets.checkbox"));
    }

    private void createUIComponents() {
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (regexCheckBox.isSelected()) {
            String regex = regexTextField.getText();
            PatternSyntaxException exception = tryCompileRegex(regex);
            if (regex.isEmpty() || exception != null) {
                String message = i18n.getString("model.condition.dialog.err.invalid.regex");
                if (exception != null) {
                    message += " ( " + exception.getMessage() + ")";
                }
                return new ValidationInfo(message);
            }
        }

        if (parentsCheckBox.isSelected()) {
            if (parentsTextField.getText().isEmpty()) {
                return new ValidationInfo(i18n.getString("model.condition.dialog.err.parent.missing"), parentsTextField);
            }
        }

        if (namesCheckBox.isSelected()) {
            if (namesTextField.getText().isEmpty()) {
                return new ValidationInfo(i18n.getString("model.condition.dialog.err.name.missing"), namesTextField);
            }
        }

        if (extensionsCheckBox.isSelected()) {
            if (extensionsTextField.getText().isEmpty()) {
                return new ValidationInfo(i18n.getString("model.condition.dialog.err.extension.missing"), extensionsTextField);
            }
        }

        if (mayEndWithRadioButton.isSelected() && mayEndWithRadioButton.isEnabled()) {
            if (!namesCheckBox.isSelected()) {
                return new ValidationInfo(i18n.getString("model.condition.dialog.err.names.checkbox.if.may.end"), namesCheckBox);
            }
        }

        if (facetsCheckBox.isSelected()) {
            if (facetsTextField.getText().isEmpty()) {
                return new ValidationInfo(i18n.getString("model.condition.dialog.err.facet.missing"), facetsTextField);
            }
        }

        if (!getModelConditionFromInput().isValid()) {
            return new ValidationInfo(i18n.getString("model.condition.dialog.err.select.at.least.one.checkbox"));
        }

        return null;
    }

    /**
     * Creates a {@link ModelCondition} object from the user input.
     */
    public ModelCondition getModelConditionFromInput() {
        ModelCondition modelCondition = new ModelCondition();

        if (regexCheckBox.isSelected()) {
            modelCondition.setRegex(regexTextField.getText());
        }

        if (parentsCheckBox.isSelected()) {
            String[] parents = parentsTextField.getText().split(FIELD_SEPARATOR);
            modelCondition.setParents(parents);
        }

        if (namesCheckBox.isSelected()) {
            String[] names = namesTextField.getText().split(FIELD_SEPARATOR);
            if (startsWithRadioButton.isSelected()) {
                modelCondition.setStart(names);
                if (noDotCheckBox.isSelected()) {
                    modelCondition.setNoDot();
                }
            } else {
                modelCondition.setEq(names);
            }
        }

        if (extensionsCheckBox.isSelected()) {
            String[] extensions = extensionsTextField.getText().split(FIELD_SEPARATOR);
            if (mayEndWithRadioButton.isSelected()) {
                modelCondition.setMayEnd(extensions);
            } else {
                modelCondition.setEnd(extensions);
            }
        }

        if (facetsCheckBox.isSelected()) {
            String[] facets = facetsTextField.getText().toLowerCase().split(FIELD_SEPARATOR);
            modelCondition.setFacets(facets);
        }

        return modelCondition;
    }

    /**
     * Sets a condition that can be edited using this dialog.
     */
    public void setCondition(ModelCondition modelCondition) {
        setTitle(i18n.getString("model.condition.dialog.edit.condition.title"));

        if (modelCondition.hasRegex()) {
            regexCheckBox.setSelected(true);
            regexTextField.setText(modelCondition.getRegex());
        }

        if (modelCondition.hasCheckParent()) {
            parentsCheckBox.setSelected(true);
            parentsTextField.setText(String.join(FIELD_SEPARATOR, modelCondition.getParents()));
        }

        if (modelCondition.hasStart() || modelCondition.hasEq()) {
            namesCheckBox.setSelected(true);
            namesTextField.setText(String.join(FIELD_SEPARATOR, modelCondition.getNames()));
            startsWithRadioButton.setSelected(modelCondition.hasStart());
            noDotCheckBox.setSelected(modelCondition.hasNoDot());
            equalsRadioButton.setSelected(modelCondition.hasEq());
        }

        if (modelCondition.hasEnd() || modelCondition.hasMayEnd()) {
            extensionsCheckBox.setSelected(true);
            extensionsTextField.setText(String.join(FIELD_SEPARATOR, modelCondition.getExtensions()));
            endsWithRadioButton.setSelected(modelCondition.hasEnd());
            mayEndWithRadioButton.setSelected(modelCondition.hasMayEnd());
        }

        if (modelCondition.hasFacets()) {
            facetsCheckBox.setSelected(true);
            facetsTextField.setText(String.join(FIELD_SEPARATOR, modelCondition.getFacets()));
        }
    }

    /**
     * Tries to compile a given regex and returns an exception if it failed.
     */
    @Nullable
    private PatternSyntaxException tryCompileRegex(String regex) {
        try {
            Pattern.compile(regex);
            return null;
        } catch (PatternSyntaxException ex) {
            return ex;
        }
    }
}
