package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.analyst.spatialanalyst.Generalization;
import com.supermap.data.*;
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
 * Created by lixiaoyao on 2017/7/22.
 */
public class MetaProcessRegionTrunkToCenterLine extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "TrunkCenterLineResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset dataset;

	private ParameterSaveDataset saveDataset;

	public MetaProcessRegionTrunkToCenterLine() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		initParametersListener();
	}

	private void initParameters() {
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.dataset = new ParameterSingleDataset(DatasetType.REGION);
		this.saveDataset = new ParameterSaveDataset();

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(this.sourceDatasource, this.dataset);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(this.saveDataset);
		this.parameters.setParameters(sourceData, targetData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.REGION, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_CenterLine"), DatasetTypes.LINE, targetData);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(saveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		this.saveDataset.setDefaultDatasetName("result_RegionTrunkToCenterLine");
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset(DatasetType.REGION);
		if (defaultDataset != null) {
			this.sourceDatasource.setSelectedItem(defaultDataset.getDatasource());
			this.dataset.setSelectedItem(defaultDataset);
			this.saveDataset.setResultDatasource(defaultDataset.getDatasource());
		}
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));

	}

	private void initParametersListener() {
		// do nohting ,Because the weather is so hot
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
				src = (DatasetVector) this.dataset.getSelectedItem();
			}

			Recordset recordset = src.getRecordset(false, CursorType.STATIC);

			Generalization.addSteppedListener(steppedListener);
			Dataset result = Generalization.regionToCenterLine(recordset, this.saveDataset.getResultDatasource(), datasetName);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
		} finally {
			Generalization.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return this.parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.REGION_TRUNK_TO_CENTERLINE;
	}

	@Override
	public String getTitle() {
		return CommonProperties.getString("String_RegionTrunkToCenterLine");
	}
}
