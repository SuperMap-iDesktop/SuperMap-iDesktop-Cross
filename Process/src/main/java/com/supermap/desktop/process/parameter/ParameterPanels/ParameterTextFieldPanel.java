package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.TEXTFIELD)
public class ParameterTextFieldPanel extends SwingPanel implements IParameterPanel {
	private ParameterTextField parameterTextField;
	private JLabel label = new JLabel();
	private JTextField textField = new JTextField();
	private boolean isSelectingItem = false;

	public ParameterTextFieldPanel(IParameter parameterTextField) {
		super(parameterTextField);
		this.parameterTextField = (ParameterTextField) parameterTextField;
		label.setText(this.parameterTextField.getDescribe());
		label.setToolTipText(this.parameterTextField.getDescribe());
		textField.setText(String.valueOf(this.parameterTextField.getSelectedItem()));
		initLayout();
		initListeners();
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		textField.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		panel.add(textField, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
		if (!parameterTextField.isEnabled()) {
			UICommonToolkit.setComponentEnabled(textField, parameterTextField.isEnabled());
		}
	}

	public void setText(String text){
		textField.setText(text);
	}

	private void initListeners() {
		parameterTextField.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					try {
						isSelectingItem = true;
						ParameterTextFieldPanel.this.textField.setText(evt.getNewValue() == null ? null : evt.getNewValue().toString());
					} finally {
						isSelectingItem = false;
					}
				}
			}
		});
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				textFieldValueChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				textFieldValueChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textFieldValueChanged();
			}

			private void textFieldValueChanged() {
				if (!isSelectingItem) {
					isSelectingItem = true;
					String text = textField.getText() == null ? "" : textField.getText();
					parameterTextField.setSelectedItem(text);
					isSelectingItem = false;
				}
			}
		});
	}

}
