package com.supermap.desktop.process.parameter.ipls;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IMultiSelectionParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * undo
 * @author XiaJT
 */
public class ParameterDatasets extends AbstractParameter implements IMultiSelectionParameter {

	private List datasets = new ArrayList<>();

	@Override
	public String getType() {
		return ParameterType.DATASETS;
	}



	@Override
	public Object getSelectedItem() {
		return new Object[0];
	}

	@Override
	public void setSelectedItem(Object selectedItems) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public String getDescribe() {
		return null;
	}
}
