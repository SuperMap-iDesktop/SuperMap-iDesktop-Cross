package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Dataset;
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
	private DatasetFieldInfo datasetInfo;
	@ParameterField(name = SOURCE_DATASET)
	private Dataset dataset;

	public DatasetFieldInfo getDatasetInfo() {
		return datasetInfo;
	}

	public void setDataset(Dataset dataset) {
		firePropertyChangeListener(new PropertyChangeEvent(this, DATASET_FIELDINFO, this.datasetInfo, datasetInfo));
		this.datasetInfo = datasetInfo;
	}


	@Override
	public String getType() {
		return ParameterType.MULTIFIELDSET;
	}

	public class DatasetFieldInfo {
		private Dataset dataset;
		private String[] sourceFields;
		private String[] targetFields;
		public DatasetFieldInfo(Dataset dataset){
			this.dataset = dataset;
			initFieldsInfo();
		}

		private void initFieldsInfo() {
		}

		public Dataset getDataset() {
			return dataset;
		}

		public void setDataset(Dataset dataset) {
			this.dataset = dataset;
		}

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
