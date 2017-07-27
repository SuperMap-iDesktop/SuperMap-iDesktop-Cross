package com.supermap.desktop.process.meta.metaProcessImplements;


import com.supermap.analyst.spatialanalyst.Generalization;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by lixiaoyao on 2017/7/22.
 */
public abstract class MetaProcessCenterLine extends MetaProcess {

	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "CenterLineResult";

	public abstract DatasetType getSonDatasetType() ;

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
		sourceData.addParameters(sourceDatasource, dataset);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(saveDataset);
		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(parameterNumberMaxWidth);
		paramSet.addParameters(parameterNumberMinWidth);

		this.parameters.setParameters(sourceData, paramSet, targetData);
		if (getSonDatasetType()==DatasetType.LINE) {
			this.parameters.addInputParameters(INPUT_DATA,DatasetTypes.LINE, sourceData);
		}else if (getSonDatasetType()==DatasetType.REGION){
			this.parameters.addInputParameters(INPUT_DATA,DatasetTypes.REGION, sourceData);
		}
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.LINE, targetData);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(saveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset(getSonDatasetType());
		if (defaultDataset != null) {
			sourceDatasource.setSelectedItem(defaultDataset.getDatasource());
			if (defaultDataset.getType() == getSonDatasetType()) {
				dataset.setSelectedItem(defaultDataset);
			}
			saveDataset.setResultDatasource(defaultDataset.getDatasource());
			saveDataset.setSelectedItem(defaultDataset.getDatasource().getDatasets().getAvailableDatasetName(getResultDatasetName()));
		}
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.saveDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.saveDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
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
			fireRunning(new RunningEvent(MetaProcessCenterLine.this, 0, "start"));

			Double maxWidth= Double.valueOf(parameterNumberMaxWidth.getSelectedItem().toString());
			Double minWidth= Double.valueOf(parameterNumberMinWidth.getSelectedItem().toString());
			if (Double.compare(maxWidth,minWidth)==-1 || Double.compare(maxWidth,minWidth)==0 ||Double.compare(maxWidth,0)!=1 ||Double.compare(minWidth,0)==-1){
				Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_Params_error"));
			}else {
				String datasetName = saveDataset.getDatasetName();
				datasetName = saveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);
				DatasetVector src = null;
				if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() != null) {
					src = (DatasetVector) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
				} else {
					src = (DatasetVector) dataset.getSelectedItem();
				}

				Recordset recordset = src.getRecordset(false, CursorType.STATIC);

				Generalization.addSteppedListener(steppedListener);
				Dataset result = Generalization.dualLineToCenterLine(recordset, maxWidth, minWidth, this.saveDataset.getResultDatasource(), datasetName);
				this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
				isSuccessful = result != null;
				fireRunning(new RunningEvent(MetaProcessCenterLine.this, 100, "finished"));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}finally {
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
