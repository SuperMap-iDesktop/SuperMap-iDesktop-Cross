package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.*;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.properties.CommonProperties;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/2/16.
 */
public class ParameterSingleDataset extends AbstractParameter implements ISelectionParameter {

	public static final String DATASET_FIELD_NAME = "value";
	//	public static final String DATASET_TYPES_FIELD_NAME = "datasetTypes";
	public static final String DATASOURCE_FIELD_NAME = "datasource";

	@ParameterField(name = DATASET_FIELD_NAME)
	private Dataset selectedItem;
	//	@ParameterField(name = DATASET_TYPES_FIELD_NAME) not ready
	private DatasetType[] datasetTypes;
	@ParameterField(name = DATASOURCE_FIELD_NAME)
	private Datasource datasource;
	private String describe = CommonProperties.getString(CommonProperties.Label_Dataset);
	private DatasourceClosingListener datasourceClosingListener = new DatasourceClosingListener() {
		@Override
		public void datasourceClosing(DatasourceClosingEvent datasourceClosingEvent) {
			if (datasourceClosingEvent.getDatasource() == ParameterSingleDataset.this.datasource) {
				setDatasource(null);
			}
		}
	};

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
			setDatasource(selectedItem.getDatasource());
		}
		firePropertyChangeListener(new PropertyChangeEvent(this, DATASET_FIELD_NAME, oldValue, selectedItem));
	}

	@Override
	public Dataset getSelectedItem() {
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
		if (this.datasource != datasource) {
			if (this.datasource != null) {
				try {
					this.datasource.getWorkspace().getDatasources().removeClosingListener(this.datasourceClosingListener);
				} catch (Exception e) {
					// 对象已被释放，无视之
				}
			}
			Datasource oldValue = this.datasource;
			this.datasource = datasource;
			firePropertyChangeListener(new PropertyChangeEvent(this, DATASOURCE_FIELD_NAME, oldValue, this.datasource));


			if (this.datasource != null) {
				this.datasource.getWorkspace().getDatasources().addClosingListener(this.datasourceClosingListener);
			}
		}
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
