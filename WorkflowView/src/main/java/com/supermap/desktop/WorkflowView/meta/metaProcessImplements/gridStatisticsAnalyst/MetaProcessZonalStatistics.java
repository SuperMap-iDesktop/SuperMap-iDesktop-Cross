package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridStatisticsAnalyst;

import com.supermap.analyst.spatialanalyst.GridStatisticsMode;
import com.supermap.analyst.spatialanalyst.StatisticsAnalyst;
import com.supermap.analyst.spatialanalyst.ZonalStatisticsAnalystParameter;
import com.supermap.analyst.spatialanalyst.ZonalStatisticsAnalystResult;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.GridStatisticsModeUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created By Chens on 2017/8/17 0017
 */
public class MetaProcessZonalStatistics extends MetaProcess {
	private final static String VALUE_DATA = ProcessProperties.getString("String_ZonalStatistic_ValueData");
	private final static String ZONAL_DATA = ProcessProperties.getString("String_ZonalStatistic_ZonalData");
	private final static String OUTPUT_DATA_GRID = "ZonalStatisticsGridResult";
	private final static String OUTPUT_DATA_TABLE = "ZonalStatisticsTableResult";

	private ParameterDatasourceConstrained valueDatasource;
	private ParameterSingleDataset valueDataset;
	private ParameterDatasourceConstrained zonalDatasource;
	private ParameterSingleDataset zonalDataset;
	private ParameterDatasource resultDatasource;
	private ParameterTextField resultDatasetGrid;
	private ParameterTextField resultTable;
	private ParameterFieldComboBox comboBoxZonalField;
	private ParameterComboBox comboBoxStatisticMode;
	private ParameterCheckBox checkBoxIgnore;

	public MetaProcessZonalStatistics() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		valueDatasource = new ParameterDatasourceConstrained();
		valueDataset = new ParameterSingleDataset(DatasetType.GRID);
		zonalDatasource = new ParameterDatasourceConstrained();
		zonalDataset = new ParameterSingleDataset(DatasetType.GRID,DatasetType.REGION);
		resultDatasource = new ParameterDatasourceConstrained();
		resultDatasetGrid = new ParameterTextField(ProcessProperties.getString("String_Label_StatisticResult_Grid"));
		resultTable = new ParameterTextField(ProcessProperties.getString("String_Label_StatisticResult_Tabular"));
		comboBoxZonalField = new ParameterFieldComboBox(ProcessProperties.getString("String_Label_StatisticField"));
		comboBoxStatisticMode = new ParameterComboBox(ProcessProperties.getString("String_Label_StatisticType"));
		checkBoxIgnore = new ParameterCheckBox(ProcessProperties.getString("String_IgnoreNoValue"));

		ParameterCombine valueCombine = new ParameterCombine();
		valueCombine.setDescribe(VALUE_DATA);
		valueCombine.addParameters(valueDatasource,valueDataset);
		ParameterCombine zonalCombine = new ParameterCombine();
		zonalCombine.setDescribe(ZONAL_DATA);
		zonalCombine.addParameters(zonalDatasource,zonalDataset);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(ProcessProperties.getString("String_setParameter"));
		settingCombine.addParameters(comboBoxZonalField, comboBoxStatisticMode, checkBoxIgnore);
		ParameterCombine resultCombine = new ParameterCombine();
		resultCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultCombine.addParameters(resultDatasource, resultDatasetGrid, resultTable);
		parameters.setParameters(valueCombine, zonalCombine, settingCombine, resultCombine);
		this.parameters.addInputParameters(VALUE_DATA, DatasetTypes.GRID, valueCombine);
		this.parameters.addInputParameters(ZONAL_DATA, new DatasetTypes("",DatasetTypes.GRID.getValue()|DatasetTypes.REGION.getValue()), zonalCombine);
		this.parameters.addOutputParameters(OUTPUT_DATA_GRID, DatasetTypes.GRID, resultDatasetGrid);
		this.parameters.addOutputParameters(OUTPUT_DATA_TABLE, DatasetTypes.TABULAR, resultTable);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint constraintSourceValue = new EqualDatasourceConstraint();
		constraintSourceValue.constrained(valueDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSourceValue.constrained(valueDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		EqualDatasourceConstraint constraintSourceZonal = new EqualDatasourceConstraint();
		constraintSourceZonal.constrained(zonalDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSourceZonal.constrained(zonalDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(resultDatasetGrid, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(resultTable, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(zonalDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(comboBoxZonalField, ParameterFieldComboBox.DATASET_FIELD_NAME);
	}

	private void initParametersState() {
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			valueDatasource.setSelectedItem(datasetGrid.getDatasource());
			valueDataset.setSelectedItem(datasetGrid);
		}
		Dataset datasetZonal = DatasetUtilities.getDefaultDataset(DatasetType.REGION, DatasetType.GRID);
		if (datasetZonal != null) {
			zonalDatasource.setSelectedItem(datasetZonal.getDatasource());
			zonalDataset.setSelectedItem(datasetZonal);
			if (datasetZonal instanceof DatasetVector) {
				comboBoxZonalField.setFieldName((DatasetVector) datasetZonal);
			} else {
				comboBoxZonalField.setEnabled(false);
			}
		}
		resultDatasetGrid.setSelectedItem("result_ZonalStatisticsGrid");
		resultTable.setSelectedItem("result_ZonalStatisticsTable");
		comboBoxStatisticMode.setItems(new ParameterDataNode(GridStatisticsModeUtilities.getGridStatisticsModeName(GridStatisticsMode.MIN),GridStatisticsMode.MIN),
				new ParameterDataNode(GridStatisticsModeUtilities.getGridStatisticsModeName(GridStatisticsMode.MAX),GridStatisticsMode.MAX),
				new ParameterDataNode(GridStatisticsModeUtilities.getGridStatisticsModeName(GridStatisticsMode.MEAN),GridStatisticsMode.MEAN),
				new ParameterDataNode(GridStatisticsModeUtilities.getGridStatisticsModeName(GridStatisticsMode.STDEV),GridStatisticsMode.STDEV),
				new ParameterDataNode(GridStatisticsModeUtilities.getGridStatisticsModeName(GridStatisticsMode.SUM),GridStatisticsMode.SUM),
				new ParameterDataNode(GridStatisticsModeUtilities.getGridStatisticsModeName(GridStatisticsMode.VARIETY),GridStatisticsMode.VARIETY),
				new ParameterDataNode(GridStatisticsModeUtilities.getGridStatisticsModeName(GridStatisticsMode.RANGE),GridStatisticsMode.RANGE),
				new ParameterDataNode(GridStatisticsModeUtilities.getGridStatisticsModeName(GridStatisticsMode.MAJORITY),GridStatisticsMode.MAJORITY),
				new ParameterDataNode(GridStatisticsModeUtilities.getGridStatisticsModeName(GridStatisticsMode.MINORITY),GridStatisticsMode.MINORITY),
				new ParameterDataNode(GridStatisticsModeUtilities.getGridStatisticsModeName(GridStatisticsMode.MEDIAN),GridStatisticsMode.MEDIAN));
		comboBoxStatisticMode.setSelectedItem(comboBoxStatisticMode.getItemAt(4));
	}

	private void registerListener() {
		zonalDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				comboBoxZonalField.setEnabled(zonalDataset.getSelectedItem() != null && evt.getNewValue() instanceof DatasetVector);
			}
		});
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public String getKey() {
		return MetaKeys.ZONAL_STATISTICS_ON_RASTER_VALUE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_ZonalStatisticsOnRasterValue");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			DatasetGrid src = null;
			if (parameters.getInputs().getData(VALUE_DATA).getValue() != null) {
				src = (DatasetGrid) parameters.getInputs().getData(VALUE_DATA).getValue();
			} else {
				src = (DatasetGrid) valueDataset.getSelectedItem();
			}
			Dataset srcZonal = null;
			if (parameters.getInputs().getData(ZONAL_DATA).getValue() != null) {
				srcZonal = (Dataset)parameters.getInputs().getData(ZONAL_DATA).getValue();
			} else {
				srcZonal = zonalDataset.getSelectedItem();
			}
			if (srcZonal instanceof DatasetGrid) {
				PixelFormat pixelFormat = ((DatasetGrid) srcZonal).getPixelFormat();
				if (!(pixelFormat.equals(PixelFormat.UBIT1) || pixelFormat.equals(PixelFormat.UBIT4) || pixelFormat.equals(PixelFormat.UBIT8) || pixelFormat.equals(PixelFormat.UBIT16))) {
					Application.getActiveApplication().getOutput().output(new Exception().getMessage());
					return false;
				}
			}
			boolean isIgnore = Boolean.parseBoolean(checkBoxIgnore.getSelectedItem().toString());
			ZonalStatisticsAnalystParameter zonalStatisticsAnalystParameter = new ZonalStatisticsAnalystParameter();

			GridStatisticsMode mode = (GridStatisticsMode) comboBoxStatisticMode.getSelectedData();
			zonalStatisticsAnalystParameter.setIgnoreNoValue(isIgnore);
			zonalStatisticsAnalystParameter.setStatisticsMode(mode);
			zonalStatisticsAnalystParameter.setValueDataset(src);
			zonalStatisticsAnalystParameter.setZonalDataset(srcZonal);
			if (srcZonal instanceof DatasetVector) {
				zonalStatisticsAnalystParameter.setZonalFieldName(comboBoxZonalField.getFieldName());
			}
			zonalStatisticsAnalystParameter.setTargetDatasource(resultDatasource.getSelectedItem());
			zonalStatisticsAnalystParameter.setTargetTableName(resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(resultTable.getSelectedItem().toString()));
			zonalStatisticsAnalystParameter.setTargetDatasetName(resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(resultDatasetGrid.getSelectedItem().toString()));
			StatisticsAnalyst.addSteppedListener(steppedListener);
			ZonalStatisticsAnalystResult result = StatisticsAnalyst.zonalStatisticsOnRasterValue(zonalStatisticsAnalystParameter);
			isSuccessful = result != null;
			this.getParameters().getOutputs().getData(OUTPUT_DATA_GRID).setValue(result.getResultDatasetGrid());
			this.getParameters().getOutputs().getData(OUTPUT_DATA_TABLE).setValue(result.getResultTable());
			fireRunning(new RunningEvent(this,100,"finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}finally {
			StatisticsAnalyst.removeSteppedListener(steppedListener);
		}

		return isSuccessful;
	}
}
