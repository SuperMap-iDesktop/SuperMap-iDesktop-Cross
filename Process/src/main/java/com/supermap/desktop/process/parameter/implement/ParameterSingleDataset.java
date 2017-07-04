package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.properties.CommonProperties;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/2/16.
 */
public class ParameterSingleDataset extends AbstractParameter implements ISelectionParameter {

	public static final String DATASET_FIELD_NAME = "selectedItem";
	//	public static final String DATASET_TYPES_FIELD_NAME = "datasetTypes";
	public static final String DATASOURCE_FIELD_NAME = "datasource";

	@ParameterField(name = DATASET_FIELD_NAME)
	private Dataset selectedItem;
	//	@ParameterField(name = DATASET_TYPES_FIELD_NAME) not ready
	private DatasetType[] datasetTypes;
	@ParameterField(name = DATASOURCE_FIELD_NAME)
	private Datasource datasource;
	private String describe = CommonProperties.getString(CommonProperties.Label_Dataset);

	public ParameterSingleDataset(DatasetType... datasetTypes) {
		this.datasetTypes = datasetTypes;
	}


	@Override
	public void setSelectedItem(Object item) {
		Dataset oldValue = null;
		if (item == null) {
			oldValue = this.selectedItem;
			this.selectedItem = null;
		} else if (item instanceof Dataset) {
			oldValue = this.selectedItem;
			this.selectedItem = (Dataset) item;
			datasource = selectedItem.getDatasource();
		}
		firePropertyChangeListener(new PropertyChangeEvent(this, DATASET_FIELD_NAME, oldValue, selectedItem));
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	public Dataset getSelectedDataset() {
		return selectedItem;
	}

	@Override
	public String getType() {
		return ParameterType.SINGLE_DATASET;
	}


	@Override
	public void dispose() {

	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public String getDescribe() {
		return describe;
	}

	public void setDatasource(Datasource datasource) {
		firePropertyChangeListener(new PropertyChangeEvent(this, DATASOURCE_FIELD_NAME, this.datasource, datasource));
		this.datasource = datasource;
	}

	public Datasource getDatasource() {
		return datasource;
	}

	public DatasetType[] getDatasetTypes() {
		return datasetTypes;
	}

	public void setDatasetTypes(DatasetType... datasetTypes) {
		this.datasetTypes = datasetTypes;
	}

    private boolean isShowNullValue = false;

    public ParameterSingleDataset setShowNullValue(boolean isShowNullValue) {
        this.isShowNullValue = isShowNullValue;
        return this;
    }

    public boolean isShowNullValue() {
        return isShowNullValue;
    }
}
