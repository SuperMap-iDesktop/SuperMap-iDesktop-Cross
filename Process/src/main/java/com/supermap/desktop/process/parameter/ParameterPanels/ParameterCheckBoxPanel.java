package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class ParameterCheckBoxPanel extends JPanel {

	private ParameterCheckBox parameterCheckBox;
	private JCheckBox checkBox = new JCheckBox();
	private boolean isSelectingItem = false;

	public ParameterCheckBoxPanel(ParameterCheckBox parameterCheckBox) {
		this.parameterCheckBox = parameterCheckBox;
		checkBox.setText(parameterCheckBox.getDescribe());
		checkBox.setSelected(Boolean.valueOf(String.valueOf(parameterCheckBox.getSelectedItem())));
		initLayout();
		initListeners();
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(checkBox, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));
	}

	private void initListeners() {
		parameterCheckBox.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					isSelectingItem = true;
					checkBox.setSelected(Boolean.valueOf(String.valueOf(parameterCheckBox.getSelectedItem())));
					isSelectingItem = false;
				}
			}
		});
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem) {
					isSelectingItem = true;
					parameterCheckBox.setSelectedItem(checkBox.isSelected());
					isSelectingItem = false;
				}
			}
		});
	}
}
