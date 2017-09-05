package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridStatisticsAnalyst;

import com.supermap.analyst.spatialanalyst.StatisticsAnalyst;
import com.supermap.analyst.spatialanalyst.StatisticsCompareType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.MetaProcessGridAnalyst;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCheckBox;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterCommonStatisticCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasource;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.ipls.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.util.ArrayList;

/**
 * Created By Chens on 2017/8/15 0015
 */
public class MetaProcessCommonStatistics extends MetaProcessGridAnalyst {
	private final static String INPUT_DATA = SOURCE_PANEL_DESCRIPTION;
	private final static String OUTPUT_DATA = "CommonStatisticsResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterSaveDataset resultDataset;
	private ParameterComboBox comboBoxCompareType;
	private ParameterCheckBox checkBoxIgnore;
	private ParameterCommonStatisticCombine commonStatisticCombine;

	public MetaProcessCommonStatistics() {
		initParameters();
		initParameterConstraint();
		initParametersState();
	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDataset = new ParameterSingleDataset(DatasetType.GRID);
		resultDataset = new ParameterSaveDataset();
		comboBoxCompareType = new ParameterComboBox(ProcessProperties.getString("String_CompareType"));
		checkBoxIgnore = new ParameterCheckBox(ProcessProperties.getString("String_IgnoreNoValue"));
		commonStatisticCombine = new ParameterCommonStatisticCombine();

		ParameterCombine sourceCombine = new ParameterCombine();
		sourceCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceCombine.addParameters(sourceDatasource, sourceDataset);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(ProcessProperties.getString("String_setParameter"));
		settingCombine.addParameters(comboBoxCompareType, commonStatisticCombine, checkBoxIgnore);
		ParameterCombine resultCombine = new ParameterCombine();
		resultCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultCombine.addParameters(resultDataset);

		parameters.setParameters(sourceCombine, settingCombine, resultCombine);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.GRID, sourceCombine);
		this.parameters.addOutputParameters(OUTPUT_DATA,
				ProcessOutputResultProperties.getString("String_CommonStatisticResult"),
				DatasetTypes.GRID, resultCombine);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint constraintSource = new EqualDatasourceConstraint();
		constraintSource.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			sourceDatasource.setSelectedItem(datasetGrid.getDatasource());
			sourceDataset.setSelectedItem(datasetGrid);
			commonStatisticCombine.setDataset(datasetGrid);
		}
		resultDataset.setSelectedItem("result_commonStatistics");
		comboBoxCompareType.setItems(new ParameterDataNode("<", StatisticsCompareType.LESS),
				new ParameterDataNode("<=", StatisticsCompareType.LESS_OR_EQUAL),
				new ParameterDataNode("==", StatisticsCompareType.EQUAL),
				new ParameterDataNode(">=", StatisticsCompareType.GREATER_OR_EQUAL),
				new ParameterDataNode(">", StatisticsCompareType.GREATER));
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public String getKey() {
		return MetaKeys.COMMON_STATISTIC;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_CommonStatistics");
	}

	@Override
	public boolean childExecute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			DatasetGrid src = null;
			if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetGrid) parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetGrid) sourceDataset.getSelectedItem();
			}
			StatisticsCompareType type = (StatisticsCompareType) comboBoxCompareType.getSelectedData();
			boolean isIgnore = Boolean.parseBoolean(checkBoxIgnore.getSelectedItem().toString());
			String datasetName = resultDataset.getDatasetName();
			datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);
			StatisticsAnalyst.addSteppedListener(steppedListener);
			DatasetGrid result = null;
			if (commonStatisticCombine.isValueChosen()) {
				double value = commonStatisticCombine.getValue();
				StatisticsAnalyst.commonStatistics(src, value, type, isIgnore, resultDataset.getResultDatasource(), datasetName);
			} else {
				ArrayList<Dataset> datasetArrayList = commonStatisticCombine.getDatasets();
				DatasetGrid[] datasetGrids = new DatasetGrid[datasetArrayList.size()];
				for (int i = 0; i < datasetArrayList.size(); i++) {
					datasetGrids[i] = (DatasetGrid) datasetArrayList.get(i);
				}
				result = StatisticsAnalyst.commonStatistics(src, datasetGrids, type, isIgnore, resultDataset.getResultDatasource(), datasetName);
			}
			isSuccessful = result != null;
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			fireRunning(new RunningEvent(this, 100, "finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			StatisticsAnalyst.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}
}
