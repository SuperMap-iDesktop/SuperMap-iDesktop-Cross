package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IMultiSelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created By Chens on 2017/8/24 0024
 */
public class ParameterSaveDatasetTable extends AbstractParameter implements IMultiSelectionParameter {
	public static final String DATASOURCE_FIELD_NAME = "datasource";
	public static final String DATASET_FIELD_NAME = "datasetName";
	public static final String FIELD_DATASET = "Dataset";

	@ParameterField(name = FIELD_DATASET)
	private DatasetVector datasetVector;
	@ParameterField(name = DATASOURCE_FIELD_NAME)
	private Datasource resultDatasource;
	@ParameterField(name = DATASET_FIELD_NAME)
	private String[] datasetNames;
	private DatasetType[] datasetTypes;

	public ParameterSaveDatasetTable() {
		Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
		if (activeDatasources.length > 0) {
			for (Datasource activeDatasource : activeDatasources) {
				if (!activeDatasource.isReadOnly()) {
					resultDatasource = activeDatasource;
					break;
				}
			}
		} else if (Application.getActiveApplication().getActiveDatasets().length > 0) {
			Datasource datasource = Application.getActiveApplication().getActiveDatasets()[0].getDatasource();
			if (!datasource.isReadOnly()) {
				resultDatasource = datasource;
			}
		}
	}

	@Override
	public void setSelectedItem(Object item) {

	}

	@Override
	public Object getSelectedItem() {
		return getDatasetNames();
	}

	public DatasetVector getDatasetVector() {
		return datasetVector;
	}

	public void setDatasetVector(DatasetVector datasetVector) {
		DatasetVector oldValue = this.datasetVector;
		this.datasetVector = datasetVector;
		firePropertyChangeListener(new PropertyChangeEvent(this, FIELD_DATASET, oldValue, datasetVector));
	}

	public Datasource getResultDatasource() {
		return resultDatasource;
	}

	public void setResultDatasource(Datasource resultDatasource) {
		this.resultDatasource = resultDatasource;
	}

	public String[] getDatasetNames() {
		return datasetNames;
	}

	public void setDatasetNames(String[] datasetNames) {
		this.datasetNames = datasetNames;
	}

	public void setDatasetName(String datasetName,int i) {
		this.datasetNames[i] = datasetName;
	}

	public DatasetType[] getDatasetTypes() {
		return datasetTypes;
	}

	public void setDatasetTypes(DatasetType[] datasetTypes) {
		this.datasetTypes = datasetTypes;
	}

	public int getCount() {
		return datasetNames.length;
	}

	@Override
	public String getType() {
		return ParameterType.SAVE_DATASET_TABLE;
	}
}
