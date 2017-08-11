package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
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
 * Created by xie on 2017/8/8.
 */
public class MetaprocessAppendRow extends MetaProcess {
	private final String INPUT_DATA = CommonProperties.getString("String_ColumnHeader_SourceData");
	private final String OUTPUT_DATA = CommonProperties.getString("String_ColumnHeader_TargetData");
	private ParameterDatasource datasource;
	private ParameterSingleDataset dataset;
	private ParameterCombine targetData;
	private ParameterDatasetChooseTable chooseTable;
	private ParameterCheckBox checkBox;

	public MetaprocessAppendRow() {
		initParameters();
		initConstraint();
	}

	private void initConstraint() {
		EqualDatasourceConstraint datasourceConstraint = new EqualDatasourceConstraint();
		datasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		datasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint datasetConstraint = new EqualDatasetConstraint();
		datasetConstraint.constrained(dataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		datasetConstraint.constrained(chooseTable, ParameterDatasetChooseTable.DATASET_FIELD_NAME);
	}

	private void initParameters() {
		this.datasource = new ParameterDatasource();
		this.dataset = new ParameterSingleDataset(DatasetTypeUtilities.getDatasetTypeVector());
		this.dataset.setDatasource(DatasourceUtilities.getDefaultResultDatasource());
		Dataset dataset = DatasetUtilities.getDefaultDatasetVector();
		this.targetData = new ParameterCombine();
		this.targetData.setDescribe(OUTPUT_DATA);
		this.targetData.addParameters(this.datasource, this.dataset);
		this.chooseTable = new ParameterDatasetChooseTable();
		if (null != dataset) {
			this.dataset.setSelectedItem(dataset);
			this.chooseTable.setDataset(dataset);
		}
		this.checkBox = new ParameterCheckBox(CommonProperties.getString("String_SaveNewFields"));
		this.checkBox.setSelectedItem(true);
		this.parameters.setParameters(this.targetData, chooseTable, checkBox);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, this.chooseTable);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR, this.targetData);
		this.steppedListener = new SteppedListener() {
			@Override
			public void stepped(SteppedEvent steppedEvent) {
				fireRunning(new RunningEvent(MetaprocessAppendRow.this, steppedEvent.getPercent(), steppedEvent.getMessage()));
			}
		};
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_AppendRow");
	}

	@Override
	public boolean execute() {
		fireRunning(new RunningEvent(MetaprocessAppendRow.this, 0, "start"));
		DatasetVector targetDataset = (DatasetVector) dataset.getSelectedDataset();
		ArrayList<FieldInfo> targetFieldStr = getFieldsStr(targetDataset);
		ArrayList<String> targetFieldNames = new ArrayList<>();
		for (int i = 0, size = targetFieldStr.size(); i < size; i++) {
			targetFieldNames.add(targetFieldStr.get(i).getName());
		}
		ArrayList<Dataset> datasets = (ArrayList<Dataset>) chooseTable.getSelectedItem();
		boolean saveNewFields = Boolean.valueOf(checkBox.getSelectedItem().toString());
		Recordset recordset = null;
		targetDataset.addSteppedListener(this.steppedListener);
		for (int i = 0, size = datasets.size(); i < size; i++) {
			DatasetVector sourceDataset = ((DatasetVector) datasets.get(i));
			ArrayList<FieldInfo> sourceFieldStr = getFieldsStr(sourceDataset);
			recordset = sourceDataset.getRecordset(false, CursorType.STATIC);
			if (!saveNewFields) {
				getResultInfo(targetDataset, sourceDataset, targetDataset.append(recordset));
			} else {
				for (int j = 0, sourceFieldSize = sourceFieldStr.size(); j < sourceFieldSize; j++) {
					if (!targetFieldNames.contains(sourceFieldStr.get(j).getName())) {
						targetDataset.getFieldInfos().add(sourceFieldStr.get(j));
					}
				}
				getResultInfo(targetDataset, sourceDataset, targetDataset.append(recordset));
			}
		}
		TabularUtilities.refreshTabularDatas(targetDataset);
		if (null != recordset) {
			recordset.dispose();
		}
		targetDataset.removeSteppedListener(this.steppedListener);
		fireRunning(new RunningEvent(MetaprocessAppendRow.this, 100, "finished"));
		return false;
	}

	private void getResultInfo(DatasetVector targetDataset, DatasetVector sourceDataset, boolean result) {
		if (result) {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_Message_AppendRowSuccess"), sourceDataset.getName(), targetDataset.getName()));
		} else {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_Message_AppendRowFail"), sourceDataset.getName(), targetDataset.getName()));
		}
	}

	private ArrayList<FieldInfo> getFieldsStr(DatasetVector targetDataset) {
		ArrayList<FieldInfo> targetFieldNames = new ArrayList<>();
		FieldInfos fieldInfos = targetDataset.getFieldInfos();
		for (int i = 0, fieldCount = fieldInfos.getCount(); i < fieldCount; i++) {
			if (!fieldInfos.get(i).isSystemField()) {
				targetFieldNames.add(fieldInfos.get(i));
			}
		}
		return targetFieldNames;
	}

	@Override
	public String getKey() {
		return MetaKeys.APPEND_ROWS;
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}
}
