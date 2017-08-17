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
import com.supermap.desktop.ui.controls.TextFields.DefaultValueTextField;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/8/17.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.DEFAULTVALUETEXTFIELD)
public class ParameterDefaultValueTextFieldPanel extends SwingPanel implements IParameterPanel {
	protected ParameterDefaultValueTextField parameterTextField;
	protected JLabel label = new JLabel();

	protected JLabel labelUnit = new JLabel();
	protected DefaultValueTextField textField = new DefaultValueTextField();
	protected boolean isSelectingItem = false;

	public ParameterDefaultValueTextFieldPanel(final IParameter parameterTextField) {
		super(parameterTextField);
		this.parameterTextField = (ParameterDefaultValueTextField) parameterTextField;
		label.setText(getDescribe());
		label.setToolTipText(this.parameterTextField.getDescribe());
		label.setVisible(this.parameterTextField.isDescriptionVisible());
		textField.setText(String.valueOf(this.parameterTextField.getSelectedItem()));
		textField.setDefaulWarningText(this.parameterTextField.getDefaultWarningValue());
		textField.setToolTipText(this.parameterTextField.getToolTip());
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
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateSelectedValue(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateSelectedValue(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateSelectedValue(e);
			}

			private void updateSelectedValue(DocumentEvent e) {
				if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textField.getText())) {
					try {
						isSelectingItem = true;
						parameterTextField.setSelectedItem(textField.getText());
					} finally {
						isSelectingItem = false;
					}
				}
			}
		});
		parameterTextField.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					try {
						isSelectingItem = true;
						textField.setText(evt.getNewValue() == null ? null : evt.getNewValue().toString());
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
					parameterTextField.setSelectedItem(textField.getText());
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
