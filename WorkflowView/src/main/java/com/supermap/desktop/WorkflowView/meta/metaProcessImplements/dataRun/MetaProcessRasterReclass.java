package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.RasterReclass.ParameterRasterReclass;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by lixiaoyao on 2017/9/1.
 */
public class MetaProcessRasterReclass extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "ReclassResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset dataset;
	private ParameterRasterReclass parameterRasterReclass;
	private ParameterSaveDataset saveDataset;

	public MetaProcessRasterReclass() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		//registerListener();
	}

	private void initParameters() {
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.dataset = new ParameterSingleDataset(DatasetType.GRID);
		this.parameterRasterReclass=new ParameterRasterReclass();
		this.saveDataset = new ParameterSaveDataset();

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, dataset);
		ParameterCombine parameterSetting=new ParameterCombine();
		parameterSetting.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		parameterSetting.addParameters(this.parameterRasterReclass);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(saveDataset);

		this.parameters.setParameters(sourceData, parameterSetting, targetData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.GRID, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_ReclassResult"), DatasetTypes.GRID, targetData);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(dataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(parameterRasterReclass, ParameterRasterReclass.FIELD_DATASET);

		DatasourceConstraint.getInstance().constrained(saveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset(DatasetType.GRID);
		if (defaultDataset != null) {
			this.sourceDatasource.setSelectedItem(defaultDataset.getDatasource());
			this.dataset.setSelectedItem(defaultDataset);
			this.saveDataset.setResultDatasource(defaultDataset.getDatasource());
			this.saveDataset.setSelectedItem(defaultDataset.getDatasource().getDatasets().getAvailableDatasetName("result_Reclass"));
			this.parameterRasterReclass.setDataset((DatasetGrid) defaultDataset);
		}
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.saveDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.saveDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.RASTER_RECLASS;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_RasterReclass");
	}
}
