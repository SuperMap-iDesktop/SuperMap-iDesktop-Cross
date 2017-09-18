package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterPassword;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

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
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.PASSWORD)
public class ParameterPasswordPanel extends SwingPanel implements IParameterPanel {

	private ParameterPassword parameterPassword;
	private JLabel label = new JLabel();
	private JPasswordField passwordField = new JPasswordField();
	private boolean isSelectingItem = false;

	public ParameterPasswordPanel(IParameter parameter) {
		super(parameter);
		parameterPassword = (ParameterPassword) parameter;
		label.setText(getDescribe());
		label.setToolTipText(parameterPassword.getDescribe());
		passwordField.setText(String.valueOf(parameterPassword.getSelectedItem()));
		initLayout();
		initListeners();
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		passwordField.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		panel.add(passwordField, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));

	}

	private void initListeners() {
		parameterPassword.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					try {
						isSelectingItem = true;
						ParameterPasswordPanel.this.passwordField.setText(evt.getNewValue() == null ? null : evt.getNewValue().toString());
					} finally {
						isSelectingItem = false;
					}
				}
			}
		});
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!isSelectingItem && e.getKeyCode() == KeyEvent.VK_ENTER) {
					isSelectingItem = true;
					parameterPassword.setSelectedItem(String.valueOf(passwordField.getPassword()));
					isSelectingItem = false;
				}
			}
		});
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!isSelectingItem) {
					isSelectingItem = true;
					parameterPassword.setSelectedItem(String.valueOf(passwordField.getPassword()));
					isSelectingItem = false;
				}
			}
		});
	}

	/**
	 * @return
	 */
	private String getDescribe() {
		String describe = parameterPassword.getDescribe();
		if (parameterPassword.isRequisite()) {
			return MessageFormat.format(CommonProperties.getString("String_IsRequiredLable"), describe);
		} else {
			return describe;
		}
	}
}
