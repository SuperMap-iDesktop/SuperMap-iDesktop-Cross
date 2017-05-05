package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.MeasureParameter;
import com.supermap.analyst.spatialstatistics.SpatialMeasure;
import com.supermap.analyst.spatialstatistics.StatisticsType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;

/**
 * @author XiaJT
 */
public class MetaProcessCentralElement extends MetaProcessSpatialMeasure {
	private final static String OUTPUT_DATASET = "CentralElementResult";
	private ParameterSaveDataset parameterSaveDataset;

	public MetaProcessCentralElement() {
		super();
	}

	protected void initHook() {
		parameterSaveDataset = new ParameterSaveDataset();
		parameterSaveDataset.setDatasetName("CentralElementResult");
		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(parameterSaveDataset);
		parameterCombine.setDescribe(CommonProperties.getString("String_ResultSet"));
		DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
		parameters.addParameters(parameterCombine);
		parameters.addOutputParameters(OUTPUT_DATASET, DatasetTypes.VECTOR, parameterCombine);
	}

	@Override
	protected void doWork(DatasetVector datasetVector) {
		SpatialMeasure.addSteppedListener(steppedListener);
		MeasureParameter measureParameter1 = new MeasureParameter();
		measureParameter1.setStatisticsFieldNames(new String[]{"SmUserID", "SmUserID"});
		measureParameter1.setStatisticsTypes(new StatisticsType[]{StatisticsType.MAX, StatisticsType.FIRST});

		// 调用平均中心方法，并获取结果矢量数据集
		DatasetVector resultMeanCenter = SpatialMeasure.measureMeanCenter(
				datasetVector,
				parameterSaveDataset.getResultDatasource(),
				parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(parameterSaveDataset.getDatasetName()),
				measureParameter1);


		DatasetVector result = SpatialMeasure.measureCentralElement(
				datasetVector,
				parameterSaveDataset.getResultDatasource(),
				parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(parameterSaveDataset.getDatasetName()),
				measureParameter.getMeasureParameter());
		SpatialMeasure.removeSteppedListener(steppedListener);
		this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(result);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_CentralElement");
	}

	@Override
	public String getKey() {
		return MetaKeys.CentralElement;
	}
}
