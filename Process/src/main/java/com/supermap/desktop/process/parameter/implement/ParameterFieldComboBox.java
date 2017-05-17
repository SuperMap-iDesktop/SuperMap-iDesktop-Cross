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
 * fixme: 需要支持可以选择空，并能置为第一个选项；最好支持添加任意像，如果"0"
 */
public class ParameterFieldComboBox extends AbstractParameter implements ISelectionParameter {

	public static final String DATASET_FIELD_NAME = "Dataset";
	@ParameterField(name = DATASET_FIELD_NAME)
	private DatasetVector dataset;
	public static final String FILED_INFO_FILED_NAME = "FILED_INFO_FILED_NAME";
	@ParameterField(name = FILED_INFO_FILED_NAME)
	private FieldInfo fieldInfo;

	private FieldType[] fieldTypes;

	private String describe;

	public ParameterFieldComboBox() {
		super();
	}

	public ParameterFieldComboBox(String describe) {
		super();
		this.describe = describe;
	}

	@Override
	public void setSelectedItem(Object item) {
		if (item == null || item instanceof FieldInfo) {
			fieldInfo = ((FieldInfo) item);
		}
	}

	@Override
	public Object getSelectedItem() {
		return fieldInfo;
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
}
