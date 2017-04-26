package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by hanyz on 2017/4/25.
 */
public class ParameterDatasetType extends AbstractParameter implements ISelectionParameter {

	public static final String DATASETTYPE_FIELD_NAME = "DATASOURCE_FIELD_NAME";

	@ParameterField(name = DATASETTYPE_FIELD_NAME)
	private Object datasetType;
	private transient String[] supportedDatasetTypes;
	//是否含有“所有数据类型”这一项，默认不含
	private boolean isAllShown = false;
	//是否含有“简单数据集”这一项，默认不含
	private boolean isSimpleDatasetShown = false;
	private String describe;

	@Override
	public String getType() {
		return ParameterType.DATASETTYPE;
	}

	@Override
	public void setSelectedItem(Object value) {
		Object oldValue = this.datasetType;
		datasetType = value;
		firePropertyChangeListener(new PropertyChangeEvent(this, DATASETTYPE_FIELD_NAME, oldValue, datasetType));
	}


	@Override
	public Object getSelectedItem() {
		return datasetType;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public boolean isAllShown() {
		return isAllShown;
	}

	public void setAllShown(boolean allShown) {
		isAllShown = allShown;
	}

	public String[] getSupportedDatasetTypes() {
		return supportedDatasetTypes;
	}

	public void setSupportedDatasetTypes(String[] supportedDatasetTypes) {
		this.supportedDatasetTypes = supportedDatasetTypes;
	}

	public boolean isSimpleDatasetShown() {
		return isSimpleDatasetShown;
	}

	public void setSimpleDatasetShown(boolean simpleDatasetShown) {
		isSimpleDatasetShown = simpleDatasetShown;
	}
}