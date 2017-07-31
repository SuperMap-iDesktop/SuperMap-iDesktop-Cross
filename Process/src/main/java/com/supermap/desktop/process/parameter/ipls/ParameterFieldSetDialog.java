package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterFieldSetDialog extends AbstractParameter {

	public static final String SOURCE_DATASET_FIELD_NAME = "sourceDataset";
	public static final String RESULT_DATASET_FIELD_NAME = "resultDataset";
	public static final String SOURCE_DATASET_FIELDS_FIELD_NAME = "sourceDatasetField";
	public static final String RESULT_DATASET_FIELDS_FIELD_NAME = "resultDatasetField";

	@ParameterField(name = SOURCE_DATASET_FIELD_NAME)
	private DatasetVector sourceDataset;

	@ParameterField(name = RESULT_DATASET_FIELD_NAME)
	private DatasetVector resultDataset;
	@ParameterField(name = SOURCE_DATASET_FIELDS_FIELD_NAME)
	private String[] sourceFieldNames;
	@ParameterField(name = RESULT_DATASET_FIELDS_FIELD_NAME)
	private String[] resultFieldNames;

	private String describe;

	@Override
	public String getType() {
		return ParameterType.FIELD_SET_DIALOG;
	}

	//region getter and setter
	@Override
	public String getDescribe() {
		return describe;
	}

	public DatasetVector getSourceDataset() {
		return sourceDataset;
	}

	public void setSourceDataset(DatasetVector sourceDataset) {
		firePropertyChangeListener(new PropertyChangeEvent(this, SOURCE_DATASET_FIELD_NAME, this.sourceDataset, sourceDataset));
		this.sourceDataset = sourceDataset;
		sourceFieldNames = null;
	}

	public DatasetVector getResultDataset() {
		return resultDataset;
	}

	public void setResultDataset(DatasetVector resultDataset) {
		firePropertyChangeListener(new PropertyChangeEvent(this, RESULT_DATASET_FIELD_NAME, this.resultDataset, resultDataset));
		this.resultDataset = resultDataset;
		resultFieldNames = null;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String[] getSourceFieldNames() {
		return sourceFieldNames;
	}

	public void setSourceFieldNames(String[] sourceFieldNames) {
		this.sourceFieldNames = sourceFieldNames;
	}

	public String[] getResultFieldNames() {
		return resultFieldNames;
	}

	public void setResultFieldNames(String[] resultFieldNames) {
		this.resultFieldNames = resultFieldNames;
	}

	//endregion
}
