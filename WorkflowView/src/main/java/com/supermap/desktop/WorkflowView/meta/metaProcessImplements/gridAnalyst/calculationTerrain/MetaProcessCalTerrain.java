package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridAnalyst.calculationTerrain;

import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasource;
import com.supermap.desktop.process.parameter.ipls.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by yuanR on 2017/8/29 0029.
 * 地形计算基类
 * 地形计算下所有功能输入、输出都为栅格数据类型，将此部分做一封装
 */
public abstract class MetaProcessCalTerrain extends MetaProcess {
	private static final String INPUT_SOURCE_DATASET = CommonProperties.getString("String_GroupBox_SourceData");
	protected ParameterDatasource datasource = new ParameterDatasource();
	protected ParameterSingleDataset sourceDataset = new ParameterSingleDataset(DatasetType.GRID);
	protected ParameterSaveDataset parameterSaveDataset = new ParameterSaveDataset();
	protected ParameterCombine parameterCombineResultDataset = new ParameterCombine();

	public MetaProcessCalTerrain() {
		initParameters();
		initComponentState();
		initParameterConstraint();
		initHook();
	}

	protected void initHook() {

	}

	private void initParameters() {
		ParameterCombine parameterCombineSourceDataset = new ParameterCombine();
		parameterCombineSourceDataset.addParameters(this.datasource, this.sourceDataset);
		parameterCombineSourceDataset.setDescribe(CommonProperties.getString("String_ColumnHeader_SourceData"));

		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.GRID, parameterCombineSourceDataset);
		this.parameters.setParameters(parameterCombineSourceDataset);

		this.parameterCombineResultDataset.addParameters(this.parameterSaveDataset);
		this.parameterCombineResultDataset.setDescribe(CommonProperties.getString("String_ResultSet"));
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(this.datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(this.sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(this.parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void initComponentState() {
		this.parameterSaveDataset.setDefaultDatasetName(getDefaultResultName());
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			this.datasource.setSelectedItem(datasetGrid.getDatasource());
			this.sourceDataset.setSelectedItem(datasetGrid);
			this.parameterSaveDataset.setResultDatasource(datasetGrid.getDatasource());
		}
	}


	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		DatasetGrid datasetGrid;

		if (this.parameters.getInputs().getData(INPUT_SOURCE_DATASET) != null &&
				this.parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
			datasetGrid = (DatasetGrid) this.parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue();
		} else {
			datasetGrid = (DatasetGrid) this.sourceDataset.getSelectedItem();
		}
		try {
			// 运行之前，确保结果数据集名称正确-yuanR2017.9.7
			this.parameterSaveDataset.setSelectedItem(datasetGrid.getDatasource().getDatasets().getAvailableDatasetName(this.parameterSaveDataset.getDatasetName()));
			isSuccessful = doWork(datasetGrid);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		}
		return isSuccessful;
	}

	protected abstract boolean doWork(DatasetGrid datasetGrid);

	protected abstract String getDefaultResultName();

}
