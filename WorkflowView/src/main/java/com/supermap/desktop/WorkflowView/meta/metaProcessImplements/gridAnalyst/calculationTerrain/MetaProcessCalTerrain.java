package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridAnalyst.calculationTerrain;

import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.MetaProcessGridAnalyst;
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
public abstract class MetaProcessCalTerrain extends MetaProcessGridAnalyst {
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
		parameterCombineSourceDataset.addParameters(datasource, sourceDataset);
		parameterCombineSourceDataset.setDescribe(CommonProperties.getString("String_ColumnHeader_SourceData"));

		parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.GRID, parameterCombineSourceDataset);
		parameters.setParameters(parameterCombineSourceDataset);

		parameterCombineResultDataset.addParameters(parameterSaveDataset);
		parameterCombineResultDataset.setDescribe(CommonProperties.getString("String_ResultSet"));
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void initComponentState() {
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			datasource.setSelectedItem(datasetGrid.getDatasource());
			sourceDataset.setSelectedItem(datasetGrid);
			parameterSaveDataset.setResultDatasource(datasetGrid.getDatasource());
			parameterSaveDataset.setSelectedItem(datasetGrid.getDatasource().getDatasets().getAvailableDatasetName(getDefaultResultName()));
		} else {
			parameterSaveDataset.setSelectedItem(getDefaultResultName());
		}
	}


	@Override
	public boolean childExecute() {
		boolean isSuccessful = false;
		DatasetGrid datasetGrid;

		if (parameters.getInputs().getData(INPUT_SOURCE_DATASET) != null &&
				parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
			datasetGrid = (DatasetGrid) parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue();
		} else {
			datasetGrid = (DatasetGrid) sourceDataset.getSelectedItem();
		}
		try {
			isSuccessful = doWork(datasetGrid);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		}
		return isSuccessful;
	}

	protected abstract boolean doWork(DatasetGrid datasetGrid);

	protected abstract String getDefaultResultName();

}
