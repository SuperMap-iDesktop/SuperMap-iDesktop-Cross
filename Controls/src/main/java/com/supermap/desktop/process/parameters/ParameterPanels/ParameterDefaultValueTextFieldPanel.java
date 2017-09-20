package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.ParameterUpdateValueEvent;
import com.supermap.desktop.process.parameter.events.UpdateValueListener;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterDefaultValueTextField;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.ProviderLabel.NewHelpProvider;
import com.supermap.desktop.ui.controls.TextFields.DefaultValueTextField;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/8/17.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.DEFAULTVALUETEXTFIELD)
public class ParameterDefaultValueTextFieldPanel extends SwingPanel implements IParameterPanel {
	protected ParameterDefaultValueTextField parameterDefaultValueTextField;
	protected JLabel label = new JLabel();

	protected JLabel labelUnit = new JLabel();
	protected DefaultValueTextField textField = new DefaultValueTextField();
	protected boolean isSelectingItem = false;

	public ParameterDefaultValueTextFieldPanel(final IParameter parameterDefaultValueTextField) {
		super(parameterDefaultValueTextField);
		this.parameterDefaultValueTextField = (ParameterDefaultValueTextField) parameterDefaultValueTextField;
		label.setText(getDescribe());
		label.setToolTipText(this.parameterDefaultValueTextField.getDescribe());
		label.setVisible(this.parameterDefaultValueTextField.isDescriptionVisible());
		textField.setText(String.valueOf(this.parameterDefaultValueTextField.getSelectedItem()));
		textField.setDefaulWarningText(this.parameterDefaultValueTextField.getDefaultWarningValue());
		//textField.setToolTipText(this.parameterDefaultValueTextField.getToolTip());
		initLayout();
		initListeners();
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		textField.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		//panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		//panel.add(textField, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
		// 需要用提示icon来显示提示信息
		if (!StringUtilities.isNullOrEmpty(parameterDefaultValueTextField.getTipButtonMessage())) {
			NewHelpProvider newHelpProvider = new NewHelpProvider(getDescribe(), parameterDefaultValueTextField.getTipButtonMessage());
			newHelpProvider.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
			panel.add(newHelpProvider, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
			panel.add(textField, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
		} else {
			panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
			panel.add(textField, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
		}
		// 单位
		if (parameterDefaultValueTextField.isSetUnit()) {
			labelUnit.setText(parameterDefaultValueTextField.getUnit());
			panel.add(labelUnit, new GridBagConstraintsHelper(2, 0, 1, 1).setInsets(3, 3, 3, 3));
		}
	}

	private void initListeners() {
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!isSelectingItem && e.getKeyCode() == KeyEvent.VK_ENTER) {
					isSelectingItem = true;
					parameterDefaultValueTextField.setSelectedItem(textField.getText());
					isSelectingItem = false;
				}
			}
		});
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!isSelectingItem) {
					isSelectingItem = true;
					parameterDefaultValueTextField.setSelectedItem(textField.getText());
					isSelectingItem = false;
				}
			}
		});
		parameterDefaultValueTextField.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					try {
						isSelectingItem = true;
						textField.setText(evt.getNewValue() == null ? null : evt.getNewValue().toString());
						// 当值改变时，同时改变其值得单位-yuanR
						if (parameterDefaultValueTextField.isSetUnit()) {
							labelUnit.setText(parameterDefaultValueTextField.getUnit());
						}
					} finally {
						isSelectingItem = false;
					}
				}
			}
		});
		parameterDefaultValueTextField.addUpdateValueListener(new UpdateValueListener() {
			@Override
			public void fireUpdateValue(ParameterUpdateValueEvent event) {
				if (event.getFieldName().equals(AbstractParameter.PROPERTY_VALE)) {
					isSelectingItem = true;
					parameterDefaultValueTextField.setSelectedItem(textField.getText());
					isSelectingItem = false;
				}
			}
		});
	}


	@Override
	protected void descriptionVisibleChanged(boolean newValue) {
		label.setVisible(newValue);
	}

	/**
	 * @return
	 */
	private String getDescribe() {
		String describe = parameterDefaultValueTextField.getDescribe();
		if (parameterDefaultValueTextField.isRequisite()) {
			return MessageFormat.format(CommonProperties.getString("String_IsRequiredLable"), describe);
		} else {
			return describe;
		}
	}
}
