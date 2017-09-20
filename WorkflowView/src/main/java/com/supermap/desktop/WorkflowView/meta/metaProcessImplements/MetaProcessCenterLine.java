package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;


import com.supermap.analyst.spatialanalyst.Generalization;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
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
public abstract class MetaProcessCenterLine extends MetaProcess {

	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "CenterLineResult";

	public abstract DatasetType getSonDatasetType();

	public abstract String getResultDatasetName();

	public abstract String getSonKey();

	public abstract String getSonTitle();

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset dataset;

	private ParameterSaveDataset saveDataset;
	private ParameterNumber parameterNumberMaxWidth;
	private ParameterNumber parameterNumberMinWidth;

	public MetaProcessCenterLine() {
		initParameters();
		initParameterConstraint();
		initParametersState();
	}

	private void initParameters() {
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.dataset = new ParameterSingleDataset(getSonDatasetType());
		this.saveDataset = new ParameterSaveDataset();
		this.parameterNumberMaxWidth = new ParameterNumber(CommonProperties.getString("String_MaxWidth"));
		this.parameterNumberMinWidth = new ParameterNumber(CommonProperties.getString("String_MinWidth"));

		this.parameterNumberMaxWidth.setRequisite(true);
		this.parameterNumberMinWidth.setRequisite(true);
		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(this.sourceDatasource, this.dataset);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(this.saveDataset);
		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(this.parameterNumberMaxWidth);
		paramSet.addParameters(this.parameterNumberMinWidth);

		this.parameters.setParameters(sourceData, paramSet, targetData);
		if (getSonDatasetType() == DatasetType.LINE) {
			this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.LINE, sourceData);
		} else if (getSonDatasetType() == DatasetType.REGION) {
			this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.REGION, sourceData);
		}
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_CenterLine"), DatasetTypes.LINE, targetData);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(this.saveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset(getSonDatasetType());
		if (defaultDataset != null) {
			this.sourceDatasource.setSelectedItem(defaultDataset.getDatasource());
			if (defaultDataset.getType() == getSonDatasetType()) {
				this.dataset.setSelectedItem(defaultDataset);
			}
			this.saveDataset.setResultDatasource(defaultDataset.getDatasource());
		}
		this.saveDataset.setDefaultDatasetName(getResultDatasetName());
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.parameterNumberMaxWidth.setSelectedItem("30");
		this.parameterNumberMaxWidth.setMinValue(0);
		this.parameterNumberMaxWidth.setIsIncludeMin(false);
		this.parameterNumberMinWidth.setSelectedItem("0");
		this.parameterNumberMinWidth.setMinValue(0);
		this.parameterNumberMinWidth.setIsIncludeMin(true);
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {

			Double maxWidth = Double.valueOf(this.parameterNumberMaxWidth.getSelectedItem());
			Double minWidth = Double.valueOf(this.parameterNumberMinWidth.getSelectedItem());
			if (Double.compare(maxWidth, minWidth) == -1 || Double.compare(maxWidth, minWidth) == 0 || Double.compare(maxWidth, 0) != 1 || Double.compare(minWidth, 0) == -1) {
				Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_MinWidthShouldSmallerThanMaxWidth"));
			} else {
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
				Dataset result = Generalization.dualLineToCenterLine(recordset, maxWidth, minWidth, this.saveDataset.getResultDatasource(), datasetName);
				this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
				isSuccessful = result != null;
			}
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
		return parameters;
	}

	@Override
	public String getKey() {
		return getSonKey();
	}

	@Override
	public String getTitle() {
		return getSonTitle();
	}
}
