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
 * 重构：
 * 结果数据集结果名称的设置较为混乱，进行优化-yuanR2017.9.12
 * 优化思路：1、增加默认名称属性，开出默认名称的设置方法，主要负责初始化时对结果数据名称的修改
 * 2、去除外部对结果数据集名称的修改，改变的监听放在类的内部实现
 */
public class ParameterSaveDataset extends AbstractParameter implements ISelectionParameter {

	public static final String DATASOURCE_FIELD_NAME = "datasource";
	public static final String DATASET_FIELD_NAME = "datasetName";

	@ParameterField(name = DATASOURCE_FIELD_NAME)
	private Datasource resultDatasource;
	@ParameterField(name = DATASET_FIELD_NAME)
	private String datasetName;
	private String defaultDatasetName = "defaultResultDataName";


	/**
	 * 默认结果数据为必要参数
	 * yuanR
	 *
	 * @return
	 */
	@Override
	public boolean isRequisite() {
		return true;
	}

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
		return CommonProperties.getString("String_Label_ResultDataset");
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

//	public void setDatasetName(String datasetName) {
//		this.datasetName = datasetName;
//	}

	public String getDefaultDatasetName() {
		return defaultDatasetName;
	}

	public void setDefaultDatasetName(String defaultDatasetName) {
		this.defaultDatasetName = defaultDatasetName;
		this.datasetName = defaultDatasetName;
	}

}
