package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterRadioButtonPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterRadioButton extends AbstractParameter {
	private JPanel panel;
	private String describe;
	private ParameterDataNode[] items;
	private ParameterDataNode selectedItem;

	@Override
	public ParameterType getType() {
		return ParameterType.RADIO_BUTTON;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterRadioButtonPanel(this);
		}
		return panel;
	}

	@Override
	public void setSelectedItem(Object value) {
		if (value instanceof ParameterDataNode) {
			ParameterDataNode oldValue = this.selectedItem;
			this.selectedItem = (ParameterDataNode) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, value));
		}
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	public ParameterDataNode[] getItems() {
		return items;
	}

	public ParameterRadioButton setItems(ParameterDataNode[] parameterDataNodes) {
		this.items = parameterDataNodes;
		return this;
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterRadioButton setDescribe(String describe) {
		this.describe = describe;
		return this;
	}
}
