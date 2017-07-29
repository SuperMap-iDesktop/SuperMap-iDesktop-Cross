package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.topology.TopologyProcessing;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;


/**
 * Created by lixiaoyao on 2017/7/21.
 */
public class MetaProcessPickupBorder extends MetaProcess {

	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "PickupResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset dataset;

	private ParameterSaveDataset saveDataset;
	private ParameterCheckBox isPreProcessed;


	public MetaProcessPickupBorder() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		initParametersListener();
	}

	private void initParameters() {
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.dataset = new ParameterSingleDataset(DatasetType.REGION, DatasetType.LINE);
		this.saveDataset = new ParameterSaveDataset();
		this.isPreProcessed = new ParameterCheckBox(CommonProperties.getString("String_TopyPreProcessed"));

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, dataset);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(saveDataset);
		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(isPreProcessed);
		this.parameters.setParameters(sourceData, paramSet, targetData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.REGION, sourceData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.LINE, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.LINE, targetData);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(saveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset(DatasetType.REGION, DatasetType.LINE);
		if (defaultDataset != null) {
			sourceDatasource.setSelectedItem(defaultDataset.getDatasource());
			if (defaultDataset.getType() == DatasetType.REGION || defaultDataset.getType() == DatasetType.LINE) {
				dataset.setSelectedItem(defaultDataset);
			}
			saveDataset.setResultDatasource(defaultDataset.getDatasource());
			saveDataset.setSelectedItem(defaultDataset.getDatasource().getDatasets().getAvailableDatasetName("result_PickupBorder"));
		}
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.saveDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.saveDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
	}

	private void initParametersListener() {
		// do nohting ,just for good looking
		// Yes, that is so proud
	}


	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(MetaProcessPickupBorder.this, 0, "start"));

			String datasetName = saveDataset.getDatasetName();
			datasetName = saveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);
			DatasetVector src = null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetVector) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) dataset.getSelectedItem();
			}

			boolean isPreProcess = false;
			if (this.isPreProcessed.getSelectedItem().equals("true")) {
				isPreProcess = true;
			}

			TopologyProcessing.addSteppedListener(steppedListener);
			Dataset result = TopologyProcessing.pickupBorder(src, this.saveDataset.getResultDatasource(), datasetName, isPreProcess);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;
			fireRunning(new RunningEvent(MetaProcessPickupBorder.this, 100, "finished"));


		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_Params_error"));
		} finally {
			TopologyProcessing.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.PickupBorder;
	}

	@Override
	public String getTitle() {
		return CommonProperties.getString("String_PickupBorder");
	}
}
