package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.parameter.implement.ParameterExpression;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class ParameterExpressionPanel extends SwingPanel implements IParameterPanel {
    protected ParameterExpression parameterExpression;
    protected JLabel label;
    protected SmTextFieldLegit textFieldLegit;
    protected JButton button;

    public ParameterExpressionPanel(IParameter parameter) {
        super(parameter);
        parameterExpression = (ParameterExpression) parameter;
        initComponent();
        initLayout();
        initListener();
    }

    private void initComponent() {
        label = new JLabel(ControlsProperties.getString("String_LabelFilter"));
        label.setToolTipText(ControlsProperties.getString("String_LabelFilter"));
        textFieldLegit = new SmTextFieldLegit(String.valueOf(parameterExpression.getSelectedItem()));
        button = new JButton("...");
        ISmTextFieldLegit textFiled = parameterExpression.getSmTextFieldLegit();
        if (textFiled != null) {
            textFieldLegit.setSmTextFieldLegit(textFiled);
        }
    }

    private void initLayout() {
        label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
        textFieldLegit.setPreferredSize(new Dimension(15, 23));
        button.setPreferredSize(new Dimension(5,23));
        panel.setLayout(new GridBagLayout());
        panel.add(label,new GridBagConstraintsHelper(0,0,1,1).setWeight(0,1));
        panel.add(label,new GridBagConstraintsHelper(1,0,1,1).setWeight(1,1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,5,0,5));
        panel.add(label, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
        if (!parameterExpression.isEnabled()) {
            UICommonToolkit.setComponentEnabled(panel, parameterExpression.isEnabled());
        }
    }

    private void initListener() {

    }
}
