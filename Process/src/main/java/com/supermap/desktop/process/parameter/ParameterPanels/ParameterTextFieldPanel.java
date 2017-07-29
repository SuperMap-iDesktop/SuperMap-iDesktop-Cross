package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.ParameterUpdateValueEvent;
import com.supermap.desktop.process.parameter.events.UpdateValueListener;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.TEXTFIELD)
public class ParameterTextFieldPanel extends SwingPanel implements IParameterPanel {
	private ISmTextFieldLegit smTextFieldLegit;
	protected ParameterTextField parameterTextField;
	protected JLabel label = new JLabel();

	protected JLabel labelUnit = new JLabel();
	protected SmTextFieldLegit textField = new SmTextFieldLegit();
	protected boolean isSelectingItem = false;

	public ParameterTextFieldPanel(final IParameter parameterTextField) {
		super(parameterTextField);
		this.parameterTextField = (ParameterTextField) parameterTextField;
		label.setText(getDescribe());
		label.setToolTipText(this.parameterTextField.getDescribe());
		label.setVisible(this.parameterTextField.isDescriptionVisible());
		textField.setText(String.valueOf(this.parameterTextField.getSelectedItem()));
		this.smTextFieldLegit = ((ParameterTextField) parameterTextField).getSmTextFieldLegit();
		textField.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (smTextFieldLegit == null || smTextFieldLegit.isTextFieldValueLegit(textFieldValue)) {
					isSelectingItem = true;
					((ParameterTextField) parameterTextField).setSelectedItem(textFieldValue);
					isSelectingItem = false;
					return true;
				}
				return false;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return smTextFieldLegit == null ? currentValue : smTextFieldLegit.getLegitValue(currentValue, backUpValue);
			}
		});
		initLayout();
		initListeners();
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		textField.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		panel.add(textField, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
		if (parameterTextField.isSetUnit()) {
			labelUnit.setText(parameterTextField.getUnit());
			panel.add(labelUnit, new GridBagConstraintsHelper(2, 0, 1, 1).setInsets(3, 3, 3, 3));
		}
	}

	private void initListeners() {
		parameterTextField.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					try {
						isSelectingItem = true;
						ParameterTextFieldPanel.this.textField.setText(evt.getNewValue() == null ? null : evt.getNewValue().toString());
						// 当值改变时，同时改变其值得单位-yuanR
						if (parameterTextField.isSetUnit()) {
							labelUnit.setText(parameterTextField.getUnit());
						}
					} finally {
						isSelectingItem = false;
					}
				}
			}
		});
		parameterTextField.addUpdateValueListener(new UpdateValueListener() {
			@Override
			public void fireUpdateValue(ParameterUpdateValueEvent event) {
				if (event.getFieldName().equals(AbstractParameter.PROPERTY_VALE)) {
					isSelectingItem = true;
					parameterTextField.setSelectedItem(ParameterTextFieldPanel.this.textField.getBackUpValue());
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
		String describe = parameterTextField.getDescribe();
		if (parameterTextField.isRequisite()) {
			return MessageFormat.format(CommonProperties.getString("String_IsRequiredLable"), describe);
		} else {
			return describe;
		}
	}
}
