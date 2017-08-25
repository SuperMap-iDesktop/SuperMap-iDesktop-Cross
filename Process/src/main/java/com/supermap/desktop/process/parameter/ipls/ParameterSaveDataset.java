package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.properties.CommonProperties;

/**
 * @author XiaJT
 */
public class ParameterSaveDataset extends AbstractParameter implements ISelectionParameter {

	public static final String DATASOURCE_FIELD_NAME = "datasource";
	public static final String DATASET_FIELD_NAME = "datasetName";

	@ParameterField(name = DATASOURCE_FIELD_NAME)
	private Datasource resultDatasource;
	@ParameterField(name = DATASET_FIELD_NAME)
	private String datasetName;
	private String datasourceDescribe;
	private String datasetDescribe;


	public ParameterSaveDataset() {
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
	public String getType() {
		return ParameterType.SAVE_DATASET;
	}


	@Override
	public void setSelectedItem(Object value) {
		this.datasetName = (String) value;
	}

	@Override
	public Object getSelectedItem() {
		return datasetName;
	}

	@Override
	public void dispose() {

	}

    @Override
    public String getDescribe() {
        return CommonProperties.getString("String_TargetDataset");
    }

    public Datasource getResultDatasource() {
        return resultDatasource;
    }

	public void setResultDatasource(Datasource resultDatasource) {
		this.resultDatasource = resultDatasource;

	}

	public String getDatasetName() {
		// parameter简单点，判断数据集存在放在panel里面，以后可以考虑做覆盖操作。
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public void setDatasourceDescribe(String datasourceDescribe) {
		this.datasourceDescribe = datasourceDescribe;
	}

	public String getDatasourceDescribe() {
		return datasourceDescribe;
	}

	public String getDatasetDescribe() {
		return datasetDescribe;
	}

	public void setDatasetDescribe(String datasetDescribe) {
		this.datasetDescribe = datasetDescribe;
	}
}
