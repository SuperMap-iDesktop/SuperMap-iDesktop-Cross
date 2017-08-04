package com.supermap.desktop.process.parameter.ipls;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IConGetter;
import com.supermap.desktop.process.parameter.interfaces.ISingleSelectionParameter;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author XiaJT
 */
public class ParameterComboBox extends AbstractParameter implements ISingleSelectionParameter {

	public static final String comboBoxItems = "comboBoxItems";
	@ParameterField(name = comboBoxItems)
	private ArrayList<ParameterDataNode> items = new ArrayList<>();
	/**
	 * label的描述文本
	 */
	private String describe;

	public static final String comboBoxValue = "comboBoxValue";
	@ParameterField(name = comboBoxValue)
	protected ParameterDataNode value;

	private IConGetter iConGetter;
	private boolean isEditable = true;

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
	public ArrayList<ParameterDataNode> getItems() {
		return items;
	}

	@Override
	public int getItemIndex(Object item) {
		for (int i = 0; i < items.size(); i++) {
			ParameterDataNode parameterDataNode = items.get(i);
			if (item == parameterDataNode) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public ParameterDataNode getItemAt(int index) {
		return items.get(index);
	}

	public void setItems(ParameterDataNode... items) {
		ArrayList<ParameterDataNode> oldValue = (ArrayList<ParameterDataNode>) this.items.clone();
		this.items.clear();
		Collections.addAll(this.items, items);
		if (items.length > 0) {
			value = items[0];
		}
		firePropertyChangeListener(new PropertyChangeEvent(this, comboBoxItems, oldValue, items));
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
		if (value == this.value) {
			return;
		}
		if (value == null || value instanceof ParameterDataNode) {
			ParameterDataNode oldValue = this.value;
			this.value = (ParameterDataNode) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, "comboBoxValue", oldValue, value));
		}
	}

	@Override
	public int getItemCount() {
		if (items == null) {
			return 0;
		}
		return items.size();
	}

	@Override
	public Object getSelectedItem() {
		return this.value;
	}

	public Object getSelectedData() {
		return this.value == null ? null : this.value.getData();
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

	public void addItem(ParameterDataNode parameterDataNode) {
		items.add(parameterDataNode);
		firePropertyChangeListener(new PropertyChangeEvent(this, comboBoxItems, null, null));
	}

	public void removeAllItems() {
		items.clear();
		firePropertyChangeListener(new PropertyChangeEvent(this, comboBoxItems, null, null));
	}

	public void removeItem(Object item) {
		if (item == null) {
			return;
		}
		boolean isRemove = false;
		if (items.contains(item)) {
			items.remove(item);
			isRemove = true;
		} else {
			for (int i = items.size() - 1; i >= 0; i--) {
				if (items.get(i).getData() == item) {
					items.remove(i);
					isRemove = true;
				}
			}
		}
		if (isRemove) {
			firePropertyChangeListener(new PropertyChangeEvent(this, comboBoxItems, null, null));
		}
	}

	public IConGetter getIConGetter() {
		return iConGetter;
	}

	public void setIConGetter(IConGetter iConGetter) {
		this.iConGetter = iConGetter;
	}

	public boolean isEditable() {
		return this.isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
}
