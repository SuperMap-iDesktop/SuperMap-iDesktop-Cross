package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.TabularUtilities;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2017/8/5.
 * 追加列
 */
public class MetaProcessAppendFields extends MetaProcess {
	private final String INPUT_DATA = CommonProperties.getString("String_ColumnHeader_SourceData");
	private final String OUTPUT_DATA = CommonProperties.getString("String_ColumnHeader_TargetData");
	private ParameterCombine sourceDataCombine;
	private ParameterDatasource sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterFieldComboBox sourceLinkedField;
	private ParameterCombine targetDataCombine;
	private ParameterDatasource targetDatasource;
	private ParameterSingleDataset targetDataset;
	private ParameterFieldComboBox targetLinkedField;
	private ParameterMultiFieldSet multiFieldSet;

	public MetaProcessAppendFields() {
		initParameters();
		initConstraint();
	}

	private void initConstraint() {
		EqualDatasourceConstraint sourceDatasourceConstraint = new EqualDatasourceConstraint();
		sourceDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		sourceDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint targetDatasourceConstraint = new EqualDatasourceConstraint();
		targetDatasourceConstraint.constrained(targetDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		targetDatasourceConstraint.constrained(targetDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint datasetConstraint1 = new EqualDatasetConstraint();
		datasetConstraint1.constrained(sourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		datasetConstraint1.constrained(multiFieldSet, ParameterMultiFieldSet.SOURCE_DATASET);

		EqualDatasetConstraint datasetConstraint2 = new EqualDatasetConstraint();
		datasetConstraint2.constrained(sourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		datasetConstraint2.constrained(sourceLinkedField, ParameterFieldComboBox.DATASET_FIELD_NAME);

		EqualDatasetConstraint datasetConstraint3 = new EqualDatasetConstraint();
		datasetConstraint3.constrained(targetDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		datasetConstraint3.constrained(targetLinkedField, ParameterFieldComboBox.DATASET_FIELD_NAME);

	}

	private void initParameters() {
		steppedListener = new SteppedListener() {
			@Override
			public void stepped(SteppedEvent steppedEvent) {
				fireRunning(new RunningEvent(MetaProcessAppendFields.this, steppedEvent.getPercent(), steppedEvent.getMessage()));
			}
		};
		Datasource datasource = DatasourceUtilities.getDefaultResultDatasource();
		DatasetType[] vectorTypes = DatasetTypeUtilities.getDatasetTypeVector();
		Dataset dataset = DatasetUtilities.getDefaultDataset(vectorTypes);

		this.targetDatasource = new ParameterDatasource();
		this.targetDatasource.setReadOnlyNeeded(false);
		this.targetDatasource.setSelectedItem(datasource);
		this.targetDataset = new ParameterSingleDataset();
		this.targetDataset.setDatasource(datasource);
		this.targetDataset.setDatasetTypes(DatasetTypeUtilities.getDatasetTypeVector());
		this.targetLinkedField = new ParameterFieldComboBox();
		this.targetLinkedField.setShowSystemField(true);
		this.targetLinkedField.setDescribe(ProcessProperties.getString("String_ConnectionField"));
		this.targetDataCombine = new ParameterCombine();
		this.targetDataCombine.setDescribe(OUTPUT_DATA);
		this.targetDataCombine.addParameters(targetDatasource, targetDataset, targetLinkedField);

		this.sourceDatasource = new ParameterDatasource();
		this.sourceDatasource.setSelectedItem(datasource);
		this.sourceDataset = new ParameterSingleDataset();
		this.sourceDataset.setDatasource(datasource);
		this.sourceDataset.setDatasetTypes(DatasetTypeUtilities.getDatasetTypeVector());
		this.sourceLinkedField = new ParameterFieldComboBox();
		this.sourceLinkedField.setShowSystemField(true);
		this.sourceLinkedField.setDescribe(ProcessProperties.getString("String_ConnectionField"));
		this.sourceDataCombine = new ParameterCombine();
		this.sourceDataCombine.setDescribe(INPUT_DATA);
		this.sourceDataCombine.addParameters(sourceDatasource, sourceDataset, sourceLinkedField);
		this.multiFieldSet = new ParameterMultiFieldSet();

		ArrayList<Dataset> datasetArray = new ArrayList<>();
		Datasets datasets = null;
		if (null != dataset && (null != dataset.getDatasource() && dataset.getDatasource().equals(datasource))) {
			datasets = dataset.getDatasource().getDatasets();
		} else if (null == dataset || (null != dataset.getDatasource() && !dataset.getDatasource().equals(datasource))) {
			datasets = datasource.getDatasets();
		}
		for (int i = 0, size = datasets.getCount(); i < size; i++) {
			for (int j = 0, length = vectorTypes.length; j < length; j++) {
				if (datasets.get(i).getType().equals(vectorTypes[j])) {
					datasetArray.add(datasets.get(i));
				}
			}
		}
		if (datasetArray.size()>1) {
			this.sourceDataset.setSelectedItem(datasetArray.get(1));
			this.targetDataset.setSelectedItem(datasetArray.get(0));
			this.sourceLinkedField.setFieldName((DatasetVector) datasetArray.get(1));
			this.targetLinkedField.setFieldName((DatasetVector) datasetArray.get(0));
			this.multiFieldSet.setDataset((DatasetVector) datasetArray.get(1));
		}
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, sourceDataCombine);
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_Append"), DatasetTypes.VECTOR, targetDataCombine);
		this.parameters.setParameters(targetDataCombine, sourceDataCombine, multiFieldSet);
	}


	@Override
	public boolean execute() {
		boolean result = false;
		fireRunning(new RunningEvent(this, 0, "start"));
		DatasetVector targetDatasetVector = (DatasetVector) targetDataset.getSelectedDataset();
		String sourceLinked = sourceLinkedField.getFieldName();
		String targetLinked = targetLinkedField.getFieldName();
		DatasetVector sourceDatasetVector = (DatasetVector) sourceDataset.getSelectedDataset();
		if (null != multiFieldSet.getDatasetFieldInfo()) {
			String[] sourceFields = multiFieldSet.getDatasetFieldInfo().getSourceFields();
			String[] targetFields = multiFieldSet.getDatasetFieldInfo().getTargetFields();
			targetDatasetVector.addSteppedListener(this.steppedListener);
			result = targetDatasetVector.appendFields(sourceDatasetVector, sourceLinked, targetLinked, sourceFields, targetFields);
			if (result) {
				fireRunning(new RunningEvent(this, 100, "success"));
				Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_AppendFieldsSuccess"), sourceDatasetVector.getName(), targetDatasetVector.getName()));
				TabularUtilities.refreshTabularDatas(targetDatasetVector);
			} else {
				fireRunning(new RunningEvent(this, 100, "failed"));
				Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_AppendFieldsFailed"), sourceDatasetVector.getName(), targetDatasetVector.getName()));
			}
		} else {
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_AppendFieldsIsNull"));
		}
		sourceDatasetVector.removeSteppedListener(this.steppedListener);
		return result;
	}

	@Override
	public String getKey() {
		return MetaKeys.APPEND_FIELDS;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_AppendFields");
	}

	@Override
	public IParameters getParameters() {
		return this.parameters;
	}

	@Override
	public IParameterPanel getComponent() {
		return this.parameters.getPanel();
	}
}
