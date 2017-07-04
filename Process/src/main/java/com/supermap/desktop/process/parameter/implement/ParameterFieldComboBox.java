package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterFieldComboBox extends AbstractParameter implements ISelectionParameter {

	public static final String DATASET_FIELD_NAME = "Dataset";
	@ParameterField(name = DATASET_FIELD_NAME)
	private DatasetVector dataset;
	public static final String FILED_INFO_FILED_NAME = "FILED_INFO_FILED_NAME";
	@ParameterField(name = FILED_INFO_FILED_NAME)
	private String fieldName;

	private FieldType[] fieldTypes;

	private String describe;
	private boolean isShowNullValue = false;
	private boolean isShowSystemField = false;
	private boolean isEditable = true;

	public ParameterFieldComboBox() {
		super();
	}

	public ParameterFieldComboBox(String describe) {
		super();
		this.describe = describe;
	}

	@Override
	public void setSelectedItem(Object item) {
		if (item == null) {
			fieldName = "";
		} else if (item instanceof FieldInfo) {
			fieldName = ((FieldInfo) item).getCaption();
		} else {
			fieldName = item.toString();
		}
	}

	@Override
	public Object getSelectedItem() {
		return getFieldName();
	}

	public String getFieldName() {
		return fieldName;
	}

	@Override
	public String getType() {
		return ParameterType.FIELD_COMBO_BOX;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public String getDescribe() {
		return describe;
	}

	public DatasetVector getDataset() {
		return dataset;
	}

	public boolean isShowNullValue() {
		return isShowNullValue;
	}

	public ParameterFieldComboBox setShowNullValue(boolean showNullValue) {
		isShowNullValue = showNullValue;
		return this;
	}


	public boolean isShowSystemField() {
		return isShowSystemField;
	}

	public void setShowSystemField(boolean showSystemField) {
		isShowSystemField = showSystemField;
	}

	public void setFieldType(FieldType[] fieldType) {
		this.fieldTypes = fieldType;
	}

	public FieldType[] getFieldTypes() {
		return fieldTypes;
	}

	public void setDataset(DatasetVector dataset) {
		DatasetVector oldValue = this.dataset;
		this.dataset = dataset;
		firePropertyChangeListener(new PropertyChangeEvent(this, DATASET_FIELD_NAME, oldValue, this.dataset));
	}
	public boolean isEditable() {
		return this.isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

}
