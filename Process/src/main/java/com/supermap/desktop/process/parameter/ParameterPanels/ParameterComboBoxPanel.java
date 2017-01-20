package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.parameter.ParameterComboBoxCellRender;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.util.ParameterUtil;
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
public class ParameterComboBoxPanel extends JPanel {
	private ParameterComboBox parameterComboBox;
	// 防止多次触发事件
	private boolean isSelectingItem = false;
	private JLabel label = new JLabel();
	private JComboBox<ParameterDataNode> comboBox = new JComboBox<>();

	public ParameterComboBoxPanel(ParameterComboBox parameterComboBox) {
		this.parameterComboBox = parameterComboBox;
		ParameterDataNode[] items = parameterComboBox.getItems();
		if (items != null && items.length > 0) {
			for (ParameterDataNode item : items) {
				comboBox.addItem(item);
			}
		}
		if (parameterComboBox.getSelectedItem() != null) {
			comboBox.setSelectedItem(parameterComboBox.getSelectedItem());
		}
		initListeners(parameterComboBox);
		label.setText(parameterComboBox.getDescribe());
		comboBox.setRenderer(new ParameterComboBoxCellRender());

		initLayout();

	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		comboBox.setPreferredSize(new Dimension(20, 23));
		this.setLayout(new GridBagLayout());
		this.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		this.add(comboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
	}

	private void initListeners(ParameterComboBox parameterComboBox) {
		parameterComboBox.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					isSelectingItem = true;
					ParameterComboBoxPanel.this.comboBox.setSelectedItem(evt.getNewValue());
					isSelectingItem = false;
				}
			}
		});
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					ParameterComboBoxPanel.this.parameterComboBox.setSelectedItem(comboBox.getSelectedItem());
					isSelectingItem = false;
				}
			}
		});
	}
}
