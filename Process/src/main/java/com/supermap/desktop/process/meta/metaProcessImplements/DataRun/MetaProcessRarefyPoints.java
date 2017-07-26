package com.supermap.desktop.process.meta.metaProcessImplements.DataRun;

import com.supermap.analyst.spatialanalyst.ProximityAnalyst;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/7/25.
 */
public class MetaProcessRarefyPoints extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	;
	private final static String OUTPUT_DATA = "RarefyPointsResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset dataset;

	private ParameterSaveDataset saveDataset;
	private ParameterNumber parameterNumberRadius;
	private ParameterLabel parameterLabelRadiusUnit;

	private static final int radiusNoneEarth = 100;
	private static final double radiusDegree = 0.0001 * 100;

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
		this.parameterLabelRadiusUnit = new ParameterLabel();

		this.parameterNumberRadius.setRequisite(true);
		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, dataset);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(saveDataset);

		ParameterCombine parameterCombineParent = new ParameterCombine(ParameterCombine.HORIZONTAL).addParameters(parameterNumberRadius, parameterLabelRadiusUnit);
		parameterCombineParent.setWeightIndex(0);
		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(parameterCombineParent);

		this.parameters.setParameters(sourceData, paramSet, targetData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.POINT, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.POINT, targetData);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(saveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset(DatasetType.POINT);
		if (defaultDataset != null) {
			this.sourceDatasource.setSelectedItem(defaultDataset.getDatasource());
			this.dataset.setSelectedItem(defaultDataset);
			this.saveDataset.setResultDatasource(defaultDataset.getDatasource());
			this.saveDataset.setSelectedItem(defaultDataset.getDatasource().getDatasets().getAvailableDatasetName("result_RarefyPoints"));
			this.parameterLabelRadiusUnit.setDescribe(defaultDataset.getPrjCoordSys().getCoordUnit().toString());
			this.parameterLabelRadiusUnit.setSelectedItem(defaultDataset.getPrjCoordSys().getCoordUnit().toString());
			updateTextRadius(defaultDataset.getPrjCoordSys());
		}
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.saveDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.saveDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
		this.parameterNumberRadius.setMinValue(0);
		this.parameterNumberRadius.setIsIncludeMin(false);
	}

	private void registerListener() {
		this.dataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (dataset.getSelectedItem() != null && evt.getNewValue() instanceof DatasetVector) {
					parameterLabelRadiusUnit.setSelectedItem(((Dataset) evt.getNewValue()).getPrjCoordSys().getCoordUnit().toString());
					updateTextRadius(((Dataset) evt.getNewValue()).getPrjCoordSys());
				}
			}
		});
	}

	private void updateTextRadius(PrjCoordSys prjCoordSys){
		if (prjCoordSys.getType()== PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE){
			this.parameterNumberRadius.setSelectedItem(radiusDegree);
		}else{
			this.parameterNumberRadius.setSelectedItem(radiusNoneEarth);
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
			Dataset result = ProximityAnalyst.rarefyPoints(src, Double.valueOf(this.parameterNumberRadius.getSelectedItem().toString()), datasetName, this.saveDataset.getResultDatasource(), true);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;
			fireRunning(new RunningEvent(MetaProcessRarefyPoints.this, 100, "finished"));

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_Params_error"));
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
