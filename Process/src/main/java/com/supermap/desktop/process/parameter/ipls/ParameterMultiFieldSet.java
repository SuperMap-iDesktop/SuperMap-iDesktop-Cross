package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/8/7.
 */
public class ParameterMultiFieldSet extends AbstractParameter {
	public static final String DATASET_FIELDINFO = "datasetFieldInfo";
	public static final String SOURCE_DATASET = "sourceDataset";
	@ParameterField(name = DATASET_FIELDINFO)
	public DatasetFieldInfo datasetFieldInfo;
	@ParameterField(name = SOURCE_DATASET)
	private DatasetVector datasetVector;

	public void setDatasetFieldInfo(DatasetFieldInfo datasetFieldInfo) {
		this.datasetFieldInfo = datasetFieldInfo;
	}

	public DatasetFieldInfo getDatasetFieldInfo() {
		return datasetFieldInfo;
	}

	public DatasetVector getDatasetVector() {
		return datasetVector;
	}

	public void setDataset(DatasetVector datasetVector) {
		firePropertyChangeListener(new PropertyChangeEvent(this, DATASET_FIELDINFO, this.datasetVector, datasetVector));
		this.datasetVector = datasetVector;
	}


	@Override
	public String getType() {
		return ParameterType.MULTIFIELDSET;
	}

	public static class DatasetFieldInfo {
		private String[] sourceFields;
		private String[] targetFields;

		public String[] getSourceFields() {
			return sourceFields;
		}

		public void setSourceFields(String[] sourceFields) {
			this.sourceFields = sourceFields;
		}

		public String[] getTargetFields() {
			return targetFields;
		}

		public void setTargetFields(String[] targetFields) {
			this.targetFields = targetFields;
		}
	}
}
