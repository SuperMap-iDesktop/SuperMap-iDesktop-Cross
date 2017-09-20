package com.supermap.desktop.process.parameters.ParameterPanels.SolarRadiation;

import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameters.ParameterPanels.SwingPanel;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lixiaoyao on 2017/9/7.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.CHECK_AND_TEXTFIELD)
public class ParameterCheckboBoxAndTextFieldPanel extends SwingPanel {
	private ParameterCheckboBoxAndTextField checkboBoxAndTextField;
	private JCheckBox checkBox;
	private SmTextFieldLegit textField;

	public ParameterCheckboBoxAndTextFieldPanel(IParameter parameter) {
		super(parameter);
		this.checkboBoxAndTextField = (ParameterCheckboBoxAndTextField) parameter;
		initComponent();
		initLayout();
		initListener();
	}

	private void initComponent() {
		this.checkBox = new JCheckBox();
		this.checkBox.setText(this.checkboBoxAndTextField.getDescribe());
		this.checkBox.setToolTipText(this.checkboBoxAndTextField.getDescribe());
		this.checkBox.setSelected(true);
		this.textField = new SmTextFieldLegit();
		this.textField.setText(this.checkboBoxAndTextField.getSelectedItem());
	}

	private void initLayout() {
		this.checkBox.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		this.textField.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(this.checkBox, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		panel.add(this.textField, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 3, 0, 0));
	}

	private void initListener() {
		this.checkBox.removeActionListener(this.selectedListener);
		this.checkBox.addActionListener(this.selectedListener);
		this.textField.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return false;
				} else {
					checkboBoxAndTextField.setSelectedItem(textFieldValue);
					return true;
				}
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
	}

	private ActionListener selectedListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			checkboBoxAndTextField.setSelected(checkBox.isSelected());
			textField.setEnabled(checkBox.isSelected());
		}
	};
}
