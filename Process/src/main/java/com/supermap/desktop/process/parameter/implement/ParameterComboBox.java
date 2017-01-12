package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterComboBoxPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterComboBox extends AbstractParameter {

	private JPanel panel;
	private ParameterDataNode[] items;
	/**
	 * label的描述文本
	 */
	private String describe;
	private ParameterDataNode value;

	@Override
	public ParameterType getType() {
		return ParameterType.COMBO_BOX;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterComboBoxPanel(this);
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

	@Override
	public void setSelectedItem(Object value) {
		if (value instanceof ParameterDataNode) {
			ParameterDataNode oldValue = this.value;
			this.value = (ParameterDataNode) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, value));
		}
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


}
