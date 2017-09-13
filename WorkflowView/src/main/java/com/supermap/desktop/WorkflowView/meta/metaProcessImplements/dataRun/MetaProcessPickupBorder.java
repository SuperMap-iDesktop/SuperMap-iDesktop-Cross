package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.topology.TopologyProcessing;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
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
		sourceData.addParameters(this.sourceDatasource, this.dataset);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(this.saveDataset);
		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(this.isPreProcessed);
		this.parameters.setParameters(sourceData, paramSet, targetData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.REGION, sourceData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.LINE, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_PickupBorder"), DatasetTypes.LINE, targetData);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(this.saveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		this.saveDataset.setDefaultDatasetName("result_PickupBorder");
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset(DatasetType.REGION, DatasetType.LINE);
		if (defaultDataset != null) {
			this.sourceDatasource.setSelectedItem(defaultDataset.getDatasource());
			if (defaultDataset.getType() == DatasetType.REGION || defaultDataset.getType() == DatasetType.LINE) {
				this.dataset.setSelectedItem(defaultDataset);
			}
			this.saveDataset.setResultDatasource(defaultDataset.getDatasource());
		}
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));

	}

	private void initParametersListener() {
		// do nohting ,just for good looking
		// Yes, that is so proud
	}


	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			String datasetName = this.saveDataset.getDatasetName();
			datasetName = this.saveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);
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

		} catch (Exception e) {

			Application.getActiveApplication().getOutput().output(e);
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
		return MetaKeys.PICKUP_BORDER;
	}

	@Override
	public String getTitle() {
		return CommonProperties.getString("String_PickupBorder");
	}
}
