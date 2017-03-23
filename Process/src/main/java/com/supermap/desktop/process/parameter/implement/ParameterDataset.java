package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.Dataset;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterDataset extends AbstractParameter implements ISelectionParameter {

	@ParameterField(name = "value")
	private Dataset dataset;

	@Override
	public String getType() {
		return ParameterType.DATASET;
	}

	@Override
	public void setSelectedItem(Object value) {
		if (value instanceof Dataset) {
			Dataset oldValue = this.dataset;
			dataset = (Dataset) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, "value", oldValue, dataset));
		}
	}

	@Override
	public Object getSelectedItem() {
		return dataset;
	}

	@Override
	public void dispose() {

	}

	@Override
	public String getDescribe() {
		return "";
	}
}
