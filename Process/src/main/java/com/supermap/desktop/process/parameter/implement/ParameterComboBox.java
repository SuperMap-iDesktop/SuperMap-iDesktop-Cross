package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterComboBoxPanel;
import com.supermap.desktop.process.parameter.interfaces.ISingleSelectionParameter;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterComboBox extends AbstractParameter implements ISingleSelectionParameter {

	private JPanel panel;
	private ParameterDataNode[] items;
	/**
	 * label的描述文本
	 */
	private String describe;
	private ParameterDataNode value;

	public ParameterComboBox() {
		this("");
	}

	public ParameterComboBox(String describe) {
		this.describe = describe;
	}

	@Override
	public String getType() {
		return ParameterType.COMBO_BOX;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterComboBoxPanel(this);
		}
		return panel;
	}

	@Override
	public ParameterDataNode[] getItems() {
		return items;
	}

	@Override
	public int getItemIndex(Object item) {
		for (int i = 0; i < items.length; i++) {
			ParameterDataNode parameterDataNode = items[i];
			if (item == parameterDataNode) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public ParameterDataNode getItemAt(int index) {
		return items[index];
	}

	public void setItems(ParameterDataNode... items) {
		this.items = items;
	}

	@Override
	public void setSelectedItem(Object value) {
		if (!(value instanceof ParameterDataNode)) {
			for (ParameterDataNode item : items) {
				if (item.getData() == value) {
					value = item;
					break;
				}
			}
		}
		if (value instanceof ParameterDataNode) {
			ParameterDataNode oldValue = this.value;
			this.value = (ParameterDataNode) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, value));
		}
	}

	@Override
	public int getItemCount() {
		if (items == null) {
			return 0;
		}
		return items.length;
	}

	@Override
	public Object getSelectedItem() {
		return this.value;
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterComboBox setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public void dispose() {

	}

	public int getSelectedIndex() {
		return getItemIndex(getSelectedItem());
	}
}
