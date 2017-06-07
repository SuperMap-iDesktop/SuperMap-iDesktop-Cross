package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.controls.utilities.JComboBoxUIUtilities;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterComboBoxCellRender;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.COMBO_BOX)
public class ParameterComboBoxPanel extends SwingPanel implements IParameterPanel {
	private ParameterComboBox parameterComboBox;
	// 防止多次触发事件 Prevent multiple trigger events
	private boolean isSelectingItem = false;
	private JLabel label = new JLabel();
	private JComboBox<ParameterDataNode> comboBox = new JComboBox<>();

	public ParameterComboBoxPanel(IParameter parameterComboBox) {
		super(parameterComboBox);
		this.parameterComboBox = ((ParameterComboBox) parameterComboBox);
		ArrayList<ParameterDataNode> items = this.parameterComboBox.getItems();
		if (items != null && items.size() > 0) {
			for (ParameterDataNode item : items) {
				comboBox.addItem(item);
			}
		}
		if (this.parameterComboBox.getSelectedItem() != null) {
			comboBox.setSelectedItem(this.parameterComboBox.getSelectedItem());
		} else {
			parameterComboBox.setFieldVale(ParameterComboBox.comboBoxValue, comboBox.getSelectedItem());
		}
		initListeners(this.parameterComboBox);
		label.setText(this.parameterComboBox.getDescribe());
		label.setToolTipText(this.parameterComboBox.getDescribe());
		comboBox.setRenderer(new ParameterComboBoxCellRender());

		initLayout();

	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		comboBox.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		panel.add(comboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
	}

	private void initListeners(ParameterComboBox parameterComboBox) {
		parameterComboBox.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					isSelectingItem = true;
					ParameterComboBoxPanel.this.comboBox.setSelectedItem(evt.getNewValue());
					isSelectingItem = false;
				} else if (!isSelectingItem && evt.getPropertyName().equals(ParameterComboBox.comboBoxItems)) {
					isSelectingItem = true;
					Object selectedItem = ParameterComboBoxPanel.this.comboBox.getSelectedItem();
					ParameterComboBoxPanel.this.comboBox.removeAllItems();
					ArrayList<ParameterDataNode> items = ParameterComboBoxPanel.this.parameterComboBox.getItems();
					if (items != null && items.size() > 0) {
						for (ParameterDataNode item : items) {
							ParameterComboBoxPanel.this.comboBox.addItem(item);
						}
					}
					if (JComboBoxUIUtilities.getItemIndex(ParameterComboBoxPanel.this.comboBox, selectedItem) != -1) {
						ParameterComboBoxPanel.this.comboBox.setSelectedItem(selectedItem);
					} else {
						ParameterComboBoxPanel.this.comboBox.setSelectedItem(null);
					}
					ParameterComboBoxPanel.this.parameterComboBox.setSelectedItem(ParameterComboBoxPanel.this.comboBox.getSelectedItem());
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
