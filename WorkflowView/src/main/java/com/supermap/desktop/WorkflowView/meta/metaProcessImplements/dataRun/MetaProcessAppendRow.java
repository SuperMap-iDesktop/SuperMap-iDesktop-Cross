package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
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
public class MetaProcessAppendRow extends MetaProcess {
	private final String INPUT_DATA = CommonProperties.getString("String_ColumnHeader_SourceData");
	private final String OUTPUT_DATA = CommonProperties.getString("String_ColumnHeader_TargetData");
	private ParameterDatasource datasource;
	private ParameterSingleDataset dataset;
	private ParameterCombine targetData;
	private ParameterDatasetChooseTable chooseTable;
	private ParameterCheckBox checkBox;

	public MetaProcessAppendRow() {
		initParameters();
	}

	private void initParameters() {
		this.datasource = new ParameterDatasource();
		this.dataset = new ParameterSingleDataset(DatasetTypeUtilities.getDatasetTypeVector());
		this.dataset.setDatasource(DatasourceUtilities.getDefaultResultDatasource());

		this.targetData = new ParameterCombine();
		this.targetData.setDescribe(OUTPUT_DATA);
		this.targetData.addParameters(this.datasource, this.dataset);
		this.chooseTable = new ParameterDatasetChooseTable();
		Dataset dataset = DatasetUtilities.getDefaultDatasetVector();
		if (null != dataset) {
			this.dataset.setSelectedItem(dataset);
			this.chooseTable.setDataset(dataset);
		}
		this.checkBox = new ParameterCheckBox(CommonProperties.getString("String_SaveNewFields"));
		this.steppedListener = new SteppedListener() {
			@Override
			public void stepped(SteppedEvent steppedEvent) {
				fireRunning(new RunningEvent(MetaProcessAppendRow.this, steppedEvent.getPercent(), steppedEvent.getMessage()));
			}
		};
		this.checkBox.setSelectedItem(true);
		this.parameters.setParameters(this.targetData, chooseTable, checkBox);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, this.chooseTable);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR, this.targetData);

	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_AppendRow");
	}

	@Override
	public boolean execute() {
		boolean result = false;
		fireRunning(new RunningEvent(MetaProcessAppendRow.this, 0, "start"));
		DatasetVector datasetVector = (DatasetVector) this.dataset.getSelectedDataset();
		datasetVector.addSteppedListener(this.steppedListener);
		ArrayList<FieldInfo> sourceDatasetFieldInfos = getFieldInfos(datasetVector);
		ArrayList<String> fieldNames = new ArrayList<>();
		for (int i = 0, length = sourceDatasetFieldInfos.size(); i < length; i++) {
			fieldNames.add(sourceDatasetFieldInfos.get(i).getName());
		}
		datasetVector.addSteppedListener(this.steppedListener);
		ArrayList<Dataset> datasets = (ArrayList<Dataset>) chooseTable.getSelectedItem();
		if (null == datasets) {
			Application.getActiveApplication().getOutput().output("String_AppendRowDatasetIsNull");
			return result;
		}
		for (int i = 0, datasetsLength = datasets.size(); i < datasetsLength; i++) {
			DatasetVector tempDataset = (DatasetVector) datasets.get(i);
			ArrayList<FieldInfo> tempFieldInfos = getFieldInfos(tempDataset);
			Recordset recordset = tempDataset.getRecordset(false, CursorType.STATIC);
			if (Boolean.valueOf(checkBox.getSelectedItem().toString())) {
				for (int j = 0, size = tempFieldInfos.size(); j < size; j++) {
					if (!fieldNames.contains(tempFieldInfos.get(j).getName())) {
						datasetVector.getFieldInfos().add(tempFieldInfos.get(j));
					}
				}
				result = getResult(datasetVector, tempDataset, recordset);
			} else {
				result = getResult(datasetVector, tempDataset, recordset);
			}
			TabularUtilities.refreshTabularDatas(datasetVector);
			fireRunning(new RunningEvent(MetaProcessAppendRow.this, 100, "finished"));
			if (null != recordset) {
				recordset.dispose();
			}
		}
		datasetVector.removeSteppedListener(this.steppedListener);
		return result;
	}

	private boolean getResult(DatasetVector datasetVector, DatasetVector tempDataset, Recordset recordset) {
		boolean result;
		result = datasetVector.append(recordset);
		if (result) {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_Message_AppendRowSuccess"), tempDataset.getName(), datasetVector.getName()));
		} else {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_Message_AppendRowFail"), tempDataset.getName(), datasetVector.getName()));
		}
		return result;
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public String getKey() {
		return MetaKeys.APPEND_ROWS;
	}

	public ArrayList<FieldInfo> getFieldInfos(DatasetVector datasetVector) {
		ArrayList<FieldInfo> result = new ArrayList<>();
		for (int i = 0, length = datasetVector.getFieldCount(); i < length; i++) {
			if (!datasetVector.getFieldInfos().get(i).isSystemField()) {
				result.add(datasetVector.getFieldInfos().get(i));
			}
		}
		return result;
	}
}
