package lermitage.intellij.extra.icons.cfg;

import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBLabel;
import lermitage.intellij.extra.icons.ModelCondition;
import org.intellij.lang.regexp.RegExpFileType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ModelConditionDialog extends DialogWrapper {

    private static final String FIELD_SEPARATOR = ";";

    private JPanel dialogPanel;
    private JCheckBox regexCheckBox;
    private EditorTextField regexTextField;
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
    private JBLabel separatorLabel;

    public ModelConditionDialog() {
        super(false);
        init();
        setTitle("Add Condition");
    }

    private void createUIComponents() {
        regexTextField = new EditorTextField("", ProjectManager.getInstance().getDefaultProject(), RegExpFileType.INSTANCE);
        regexTextField.setFontInheritedFromLAF(false);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        initComponents();
        return dialogPanel;
    }

    public void initComponents() {
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

        startsWithRadioButton.addPropertyChangeListener("enabled", propertyChange -> {
            noDotCheckBox.setEnabled((boolean) propertyChange.getNewValue());
        });

        regexTextField.setEnabled(false);
        parentsTextField.setEnabled(false);
        namesTextField.setEnabled(false);
        extensionsTextField.setEnabled(false);

        separatorLabel.setText("Use " + FIELD_SEPARATOR + " as a separator for multiple values");
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (regexCheckBox.isSelected()) {
            String regex = regexTextField.getText();
            PatternSyntaxException exception = tryCompileRegex(regex);
            if (regex.isEmpty() || exception != null) {
                String message = "Please specify a valid regex.";
                if (exception != null) {
                    message += " ( " + exception.getMessage() + ")";
                }
                return new ValidationInfo(message);
            }
        }

        if (parentsCheckBox.isSelected()) {
            if (parentsTextField.getText().isEmpty()) {
                return new ValidationInfo("Please specify at least one parent.", parentsTextField);
            }
        }

        if (namesCheckBox.isSelected()) {
            if (namesTextField.getText().isEmpty()) {
                return new ValidationInfo("Please specify at least one name.", namesTextField);
            }
        }

        if (extensionsCheckBox.isSelected()) {
            if (extensionsTextField.getText().isEmpty()) {
                return new ValidationInfo("Please specify at least one extension.", extensionsTextField);
            }
        }

        if (mayEndWithRadioButton.isSelected() && mayEndWithRadioButton.isEnabled()) {
            if (!namesCheckBox.isSelected()) {
                return new ValidationInfo("If you select \"May end in\", you need to select the names checkbox.", namesCheckBox);
            }
        }

        if (!getModelConditionFromInput().isValid()) {
            return new ValidationInfo("Please select at least one checkbox.");
        }

        return null;
    }

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
            }
            else {
                modelCondition.setEq(names);
            }
        }

        if (extensionsCheckBox.isSelected()) {
            String[] extensions = extensionsTextField.getText().split(FIELD_SEPARATOR);
            if (mayEndWithRadioButton.isSelected()) {
                modelCondition.setMayEnd(extensions);
            }
            else {
                modelCondition.setEnd(extensions);
            }
        }

        return modelCondition;
    }

    public void setCondition(ModelCondition modelCondition) {
        setTitle("Edit Condition");

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
    }

    @Nullable
    private PatternSyntaxException tryCompileRegex(String regex) {
        try {
            Pattern.compile(regex);
            return null;
        }
        catch (PatternSyntaxException ex) {
            return ex;
        }
    }
}
