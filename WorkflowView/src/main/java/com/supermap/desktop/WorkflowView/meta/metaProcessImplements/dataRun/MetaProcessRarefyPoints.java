package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.analyst.spatialanalyst.ProximityAnalyst;
import com.supermap.analyst.spatialanalyst.StatisticsField;
import com.supermap.analyst.spatialanalyst.StatisticsFieldType;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
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
import com.supermap.desktop.utilities.StatisticsTypeUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/7/25.
 */
public class MetaProcessRarefyPoints extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "RarefyPointsResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset dataset;

	private ParameterSaveDataset saveDataset;
	private ParameterNumber parameterNumberRadius;
	private ParameterComboBox comboBoxStatisticsType;
	private ParameterCheckBox checkBoxRandomSaveRerefyPoints;
	private ParameterCheckBox checkBoxSaveOriginField;
	private ParameterStatisticsFieldGroupForRarefyPoints statisticsFieldGroupForRarefyPoints;
	private ParameterDataNode parameterDataNodeAverage = new ParameterDataNode(StatisticsTypeUtilities.getStatisticsTypeNameForDatasetRarefyPointsType(StatisticsFieldType.AVERAGE), StatisticsFieldType.AVERAGE);

	private static final int RADIUS_NONE_EARTH = 100;
	private static final double RADIUS_DEGREE = 0.0001 * 100;

	public MetaProcessRarefyPoints() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.dataset = new ParameterSingleDataset(DatasetType.POINT);
		this.saveDataset = new ParameterSaveDataset();
		this.parameterNumberRadius = new ParameterNumber(CommonProperties.getString("String_RarefyPointsRadius"));
		this.parameterNumberRadius.setRequisite(true);
		this.comboBoxStatisticsType = new ParameterComboBox(ProcessProperties.getString("String_StatisticsMode"));
		this.checkBoxRandomSaveRerefyPoints = new ParameterCheckBox(ProcessProperties.getString("String_RandomSaveRerefyPoints"));
		this.checkBoxSaveOriginField = new ParameterCheckBox(ProcessProperties.getString("String_RandomSaveOriginField"));
		this.statisticsFieldGroupForRarefyPoints = new ParameterStatisticsFieldGroupForRarefyPoints(ProcessProperties.getString("String_StatisticsField"));


		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, dataset);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(saveDataset);

		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(parameterNumberRadius, comboBoxStatisticsType, checkBoxRandomSaveRerefyPoints, checkBoxSaveOriginField, statisticsFieldGroupForRarefyPoints);

		this.parameters.setParameters(sourceData, paramSet, targetData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.POINT, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_RarefyPoints"), DatasetTypes.POINT, targetData);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(dataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(statisticsFieldGroupForRarefyPoints, ParameterStatisticsFieldGroupForRarefyPoints.FIELD_DATASET);

		DatasourceConstraint.getInstance().constrained(saveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset(DatasetType.POINT);
		if (defaultDataset != null) {
			this.sourceDatasource.setSelectedItem(defaultDataset.getDatasource());
			this.dataset.setSelectedItem(defaultDataset);
			this.saveDataset.setResultDatasource(defaultDataset.getDatasource());
			this.saveDataset.setSelectedItem(defaultDataset.getDatasource().getDatasets().getAvailableDatasetName("result_RarefyPoints"));
			this.parameterNumberRadius.setUnit(defaultDataset.getPrjCoordSys().getCoordUnit().toString());
			this.statisticsFieldGroupForRarefyPoints.setDataset((DatasetVector) defaultDataset);
			updateTextRadius(defaultDataset.getPrjCoordSys());
		}
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.saveDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.saveDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
		this.parameterNumberRadius.setMinValue(0);
		this.parameterNumberRadius.setIsIncludeMin(false);
		this.comboBoxStatisticsType.setItems(
				this.parameterDataNodeAverage,
				new ParameterDataNode(StatisticsTypeUtilities.getStatisticsTypeNameForDatasetRarefyPointsType(StatisticsFieldType.MAXVALUE), StatisticsFieldType.MAXVALUE),
				new ParameterDataNode(StatisticsTypeUtilities.getStatisticsTypeNameForDatasetRarefyPointsType(StatisticsFieldType.MINVALUE), StatisticsFieldType.MINVALUE),
				new ParameterDataNode(StatisticsTypeUtilities.getStatisticsTypeNameForDatasetRarefyPointsType(StatisticsFieldType.SAMPLESTDDEV), StatisticsFieldType.SAMPLESTDDEV),
				new ParameterDataNode(StatisticsTypeUtilities.getStatisticsTypeNameForDatasetRarefyPointsType(StatisticsFieldType.SAMPLEVARIANCE), StatisticsFieldType.SAMPLEVARIANCE),
				new ParameterDataNode(StatisticsTypeUtilities.getStatisticsTypeNameForDatasetRarefyPointsType(StatisticsFieldType.STDDEVIATION), StatisticsFieldType.STDDEVIATION),
				new ParameterDataNode(StatisticsTypeUtilities.getStatisticsTypeNameForDatasetRarefyPointsType(StatisticsFieldType.SUM), StatisticsFieldType.SUM),
				new ParameterDataNode(StatisticsTypeUtilities.getStatisticsTypeNameForDatasetRarefyPointsType(StatisticsFieldType.VARIANCE), StatisticsFieldType.VARIANCE));
	}

	private void registerListener() {
		this.dataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (dataset.getSelectedItem() != null && evt.getNewValue() instanceof DatasetVector) {
					parameterNumberRadius.setUnit(((Dataset) evt.getNewValue()).getPrjCoordSys().getCoordUnit().toString());
					updateTextRadius(((Dataset) evt.getNewValue()).getPrjCoordSys());
					comboBoxStatisticsType.setSelectedItem(parameterDataNodeAverage);
				}
			}
		});
		this.comboBoxStatisticsType.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				statisticsFieldGroupForRarefyPoints.setStatisticsFieldType((StatisticsFieldType) comboBoxStatisticsType.getSelectedData());
			}
		});
	}

	private void updateTextRadius(PrjCoordSys prjCoordSys) {
		if (prjCoordSys.getType() == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
			this.parameterNumberRadius.setSelectedItem(RADIUS_DEGREE);
		} else {
			this.parameterNumberRadius.setSelectedItem(RADIUS_NONE_EARTH);
		}
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(MetaProcessRarefyPoints.this, 0, "start"));

			String datasetName = saveDataset.getDatasetName();
			datasetName = saveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);
			DatasetVector src = null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetVector) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) dataset.getSelectedItem();
			}

			ProximityAnalyst.addSteppedListener(steppedListener);
			StatisticsField[] statisticsFields = statisticsFieldGroupForRarefyPoints.getSelectedStatisticsFields();
			for (int i = 0; i < statisticsFields.length; i++) {
				System.out.println(statisticsFields[i].getSourceField());
				System.out.println(statisticsFields[i].getResultField());
				System.out.println(statisticsFields[i].getMode());
			}

			boolean isRandomSaveRerefyPoints =false;
			if (this.checkBoxRandomSaveRerefyPoints.getSelectedItem().equals("true")){
				isRandomSaveRerefyPoints =true;
			}
			boolean isSaveOriginField =false;
			if (this.checkBoxSaveOriginField.getSelectedItem().equals("true")){
				isSaveOriginField =true;
			}

			Dataset result=ProximityAnalyst.summaryPoints(src,Double.valueOf(this.parameterNumberRadius.getSelectedItem().toString()),
					this.dataset.getSelectedDataset().getPrjCoordSys().getCoordUnit(),
					statisticsFields, this.saveDataset.getResultDatasource(), datasetName,isRandomSaveRerefyPoints,isSaveOriginField);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;
			fireRunning(new RunningEvent(MetaProcessRarefyPoints.this, 100, "finished"));

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			ProximityAnalyst.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.RAREFY_POINTS;
	}

	@Override
	public String getTitle() {
		return CommonProperties.getString("String_Form_RarefyPoints");
	}
}
