package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * Created by xie on 2017/8/9.
 */
public class ParameterDatasetChooseTable extends AbstractParameter implements ISelectionParameter {
	private static final String SOURCE_DATASETS = "SourceDatasets";
	private static final String SUPPORT_DATASET_TYPES="SupportDatasetTypes";
	@ParameterField(name = SOURCE_DATASETS)
	private ArrayList<Dataset> datasets;
	@ParameterField(name = SUPPORT_DATASET_TYPES)
	private DatasetType[] datasetTypes;
	@Override
	public void setSelectedItem(Object item) {
		firePropertyChangeListener(new PropertyChangeEvent(ParameterDatasetChooseTable.this, SOURCE_DATASETS, datasets, item));
		this.datasets = (ArrayList<Dataset>) item;
	}

	@Override
	public Object getSelectedItem() {
		return datasets;
	}

	public DatasetType[] getDatasetTypes() {
		return datasetTypes;
	}

	public void setDatasetTypes(DatasetType[] datasetTypes) {
		this.datasetTypes = datasetTypes;
	}

	@Override
	public String getType() {
		return ParameterType.DATASET_CHOOSE_TABLE;
	}
}
