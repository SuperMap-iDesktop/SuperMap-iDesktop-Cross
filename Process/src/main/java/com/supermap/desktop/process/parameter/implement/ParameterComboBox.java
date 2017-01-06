package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.IParameter;
import com.supermap.desktop.process.parameter.ParameterComboBoxCellRender;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author XiaJT
 */
public class ParameterComboBox implements IParameter {

	private JPanel panel;
	private ParameterDataNode[] items;
	/**
	 * label的描述文本
	 */
	private String describe;

	@Override
	public ParameterType getType() {
		return ParameterType.COMBO_BOX;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterComboBoxPanel();
		}
		return panel;
	}

	public ParameterDataNode[] getItems() {
		return items;
	}

	public ParameterComboBox setItems(ParameterDataNode[] items) {
		this.items = items;
		return this;
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterComboBox setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	protected class ParameterComboBoxPanel extends JPanel {
		private JLabel label = new JLabel();
		private JComboBox<ParameterDataNode> comboBox = new JComboBox<>();

		public ParameterComboBoxPanel() {
			ParameterDataNode[] items = getItems();
			if (items != null && items.length > 0) {
				for (ParameterDataNode item : items) {
					comboBox.addItem(item);
				}
			}
			comboBox.setPreferredSize(new Dimension(20, 23));
			comboBox.setRenderer(new ParameterComboBoxCellRender());
			label.setText(getDescribe());
			this.setLayout(new GridBagLayout());
			this.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
			this.add(comboBox, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));

			comboBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						// todo 值存放在Parameter中还是从combobox中取
					}
				}
			});
		}

	}

}
