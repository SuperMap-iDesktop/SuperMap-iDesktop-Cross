package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterSearchModeInfo;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/2/17.
 */
public class ParameterSearchMode extends AbstractParameter implements ISelectionParameter {

	@ParameterField(name = "value")
	private ParameterSearchModeInfo selectedItem;

	@Override
	public void setSelectedItem(Object item) {
		if (item instanceof ParameterSearchModeInfo) {
			ParameterSearchModeInfo oldValue = selectedItem;
			selectedItem = (ParameterSearchModeInfo) item;
			firePropertyChangeListener(new PropertyChangeEvent(ParameterSearchMode.this, "value", oldValue, selectedItem));
		}
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	@Override
	public String getType() {
		return ParameterType.SEARCH_MODE;
	}


	@Override
	public void dispose() {

	}
}
