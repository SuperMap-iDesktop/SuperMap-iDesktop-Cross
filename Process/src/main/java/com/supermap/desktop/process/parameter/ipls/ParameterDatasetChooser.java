package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/6/27.
 */
public class ParameterDatasetChooser extends AbstractParameter implements ISelectionParameter {
	@ParameterField(name = "value")
	private Dataset selectDataset;
	private DatasetType[] supportTypes;

	public ParameterDatasetChooser() {
		super();
	}

	@Override
	public void setSelectedItem(Object item) {
		if (item instanceof Dataset) {
			Dataset oldDataset = this.selectDataset;
			this.selectDataset = (Dataset) item;
			firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldDataset, selectDataset));
		}
	}

	@Override
	public Object getSelectedItem() {
		return this.selectDataset;
	}

	@Override
	public String getType() {
		return ParameterType.DATASET_CHOOSER;
	}

	@Override
	public String getDescribe() {
		return "";
	}

	@Override
	public void dispose() {

	}

	public DatasetType[] getSupportTypes() {
		return supportTypes;
	}

	public void setSupportTypes(DatasetType[] supportTypes) {
		this.supportTypes = supportTypes;
	}
}
