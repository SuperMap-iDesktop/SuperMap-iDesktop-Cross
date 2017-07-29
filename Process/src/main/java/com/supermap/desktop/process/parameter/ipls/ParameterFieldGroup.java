package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IMultiSelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterFieldGroup extends AbstractParameter implements IMultiSelectionParameter {
	public static final String FIELD_DATASET = "dataset";

	@ParameterField(name = FIELD_DATASET)
	private DatasetVector dataset;
	private String describe;
	private FieldType[] fieldType;
	private FieldInfo[] selectedFields;

	public ParameterFieldGroup() {

	}

	public ParameterFieldGroup(String describe) {
		this.describe = describe;
	}

	@Override
	public void setSelectedItem(Object item) {

	}

	@Override
	public Object getSelectedItem() {
		throw new UnsupportedOperationException("See GetSelectedFieldInfos");
	}

	@Override
	public String getType() {
		return ParameterType.FIELD_GROUP;
	}

	@Override
	public String getDescribe() {
		return describe;
	}

	public DatasetVector getDataset() {
		return dataset;
	}

	public void setDataset(DatasetVector dataset) {
		DatasetVector oldValue = this.dataset;
		this.dataset = dataset;
		firePropertyChangeListener(new PropertyChangeEvent(this, FIELD_DATASET, oldValue, dataset));
	}

	public void setFieldType(FieldType[] fieldType) {
		this.fieldType = fieldType;
	}

	public FieldType[] getFieldType() {
		return fieldType;
	}

	public void setSelectedFields(FieldInfo[] fieldInfos) {
		this.selectedFields = fieldInfos;
	}

	public FieldInfo[] getSelectedFields() {
		return selectedFields;
	}
}
