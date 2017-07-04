package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.ISingleSelectionParameter;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author XiaJT
 */
public class ParameterRadioButton extends AbstractParameter implements ISingleSelectionParameter {
	private String describe;
	@ParameterField(name = "items")
	private ArrayList<ParameterDataNode> items = new ArrayList<>();
    public static final String RADIO_BUTTON_VALUE = "selectedItem";
    @ParameterField(name = RADIO_BUTTON_VALUE)
	private ParameterDataNode selectedItem;
	public static final String VATICAL = "vertical";
	private String layout;

	@Override
	public String getType() {
		return ParameterType.RADIO_BUTTON;
	}

	@Override
	public void setSelectedItem(Object value) {
		if (value instanceof ParameterDataNode) {
			ParameterDataNode oldValue = this.selectedItem;
			this.selectedItem = (ParameterDataNode) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, RADIO_BUTTON_VALUE, oldValue, value));
		}
	}

	@Override
	public int getItemCount() {
		if (items != null) {
			return items.size();
		}
		return 0;
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	@Override
	public ArrayList<ParameterDataNode> getItems() {
		return items;
	}

	@Override
	public int getItemIndex(Object item) {
		for (int i = 0; i < items.size(); i++) {
			ParameterDataNode parameterDataNode = items.get(i);
			if (parameterDataNode == item) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public ParameterDataNode getItemAt(int index) {
		return items.get(index);
	}

	public void setItems(ParameterDataNode[] parameterDataNodes) {
		this.items.clear();
		Collections.addAll(items, parameterDataNodes);
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterRadioButton setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	@Override
	public void dispose() {

	}

}
