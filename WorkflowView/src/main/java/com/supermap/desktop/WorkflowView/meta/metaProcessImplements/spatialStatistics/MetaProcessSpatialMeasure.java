package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.parameters.implement.ParameterCombine;
import com.supermap.desktop.process.parameters.implement.ParameterDatasource;
import com.supermap.desktop.process.parameters.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameters.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by hanyz on 2017/5/2.
 * 度量地理分布 父类
 */
public abstract class MetaProcessSpatialMeasure extends MetaProcess {
	private static final String INPUT_SOURCE_DATASET = "SourceDataset";
	protected ParameterDatasource datasource = new ParameterDatasource();
	protected ParameterSaveDataset parameterSaveDataset;
	protected String OUTPUT_DATASET = "SpatialMeasureResult";
	protected ParameterSingleDataset dataset;
	protected SpatialMeasureMeasureParameter measureParameter = new SpatialMeasureMeasureParameter(getKey());

	public MetaProcessSpatialMeasure() {
		initHook();
		initDatasetComboBox();
		initParameters();
		initComponentState();
		initParameterConstraint();
	}

	protected void initHook() {

	}

	/**
	 * 根据不同的功能给予相应的DatasetComboBox
	 * 例如：线性方向平均值只能处理线类型数据集-yuanR
	 */
	private void initDatasetComboBox() {
		if (getKey().equals(MetaKeys.LinearDirectionalMean)) {
			dataset = new ParameterSingleDataset(DatasetType.LINE);
		} else {
			dataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		}
	}

	private void initParameters() {
		ParameterCombine parameterCombineSource = new ParameterCombine();
		parameterCombineSource.addParameters(datasource, dataset);
		parameterCombineSource.setDescribe(CommonProperties.getString("String_ColumnHeader_SourceData"));

		parameterSaveDataset = new ParameterSaveDataset();
		parameterSaveDataset.setDatasetName("result_spatialMeasure");
		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.addParameters(parameterSaveDataset);
		parameterCombineResult.setDescribe(CommonProperties.getString("String_ResultSet"));

		parameters.setParameters(parameterCombineSource, measureParameter, parameterCombineResult);
		parameters.addInputParameters(INPUT_SOURCE_DATASET,getKey().equals(MetaKeys.LinearDirectionalMean)? DatasetTypes.LINE:DatasetTypes.SIMPLE_VECTOR, parameterCombineSource);
		parameters.addOutputParameters(OUTPUT_DATASET, DatasetTypes.VECTOR, parameterCombineResult);
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(dataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(measureParameter, ParameterPatternsParameter.DATASET_FIELD_NAME);
	}

	private void initComponentState() {
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
			measureParameter.setCurrentDataset(datasetVector);
		}
	}


	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		DatasetVector datasetVector;

		if (parameters.getInputs().getData(INPUT_SOURCE_DATASET) != null &&
				parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
			datasetVector = (DatasetVector) parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue();
		} else {
			datasetVector = (DatasetVector) dataset.getSelectedItem();
		}
		try {
			isSuccessful = doWork(datasetVector);
		} catch (Exception e) {
			e.printStackTrace();
			Application.getActiveApplication().getOutput().output(e.getMessage());
		}
		return isSuccessful;
	}

	protected abstract boolean doWork(DatasetVector datasetVector);
}