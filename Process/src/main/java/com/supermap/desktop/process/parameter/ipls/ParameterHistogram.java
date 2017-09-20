package com.supermap.desktop.process.parameter.ipls;

import com.supermap.analyst.spatialanalyst.GridHistogram;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.properties.CommonProperties;

import java.beans.PropertyChangeEvent;

/**
 * Created By Chens on 2017/8/17 0017
 */
public class ParameterHistogram extends AbstractParameter implements ISelectionParameter {
	@ParameterField(name = "value")
	private GridHistogram selectedItem;
	private int groupCount;
	private boolean isCreate;

	@Override
	public String getDescribe() {
		return CommonProperties.getString("String_Histogram");
	}

	@Override
	public String getType() {
		return ParameterType.HISTOGRAM;
	}

	@Override
	public void setSelectedItem(Object item) {
		if (item instanceof GridHistogram) {
			GridHistogram oldValue = (GridHistogram) item;
			this.selectedItem = (GridHistogram) item;
			firePropertyChangeListener(new PropertyChangeEvent(ParameterHistogram.this, "value", oldValue, selectedItem));
		}
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}

	public boolean isCreate() {
		return isCreate;
	}

	public void setCreate(boolean create) {
		isCreate = create;
	}

	@Override
	public void dispose() {

	}
}
