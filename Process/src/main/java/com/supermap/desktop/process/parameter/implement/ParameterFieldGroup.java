package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.Dataset;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.parameter.interfaces.IMultiSelectionParameter;

/**
 * @author XiaJT
 */
public class ParameterFieldGroup extends AbstractParameter implements IMultiSelectionParameter {
	public static final String FIELD_DATASET = "dataset";

	@ParameterField(name = FIELD_DATASET)
	private Dataset dataset;

	@Override
	public void setSelectedItem(Object item) {

	}

	@Override
	public Object getSelectedItem() {
		return null;
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public String getDescribe() {
		return null;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}
}
