package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.SpatialMeasure;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;

/**
 * @author XiaJT
 */
public class MetaProcessDirectional extends MetaProcessSpatialMeasure {

	public MetaProcessDirectional() {
		super();
	}

	protected void initHook() {
		OUTPUT_DATASET = "DirectionalResult";
	}

	@Override
	protected void doWork(DatasetVector datasetVector) {
		SpatialMeasure.addSteppedListener(steppedListener);

		// 调用方向分布方法，并获取结果矢量数据集
		DatasetVector result = SpatialMeasure.measureDirectional(
				datasetVector,
				parameterSaveDataset.getResultDatasource(),
				parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(parameterSaveDataset.getDatasetName()),
				measureParameter.getMeasureParameter());
		SpatialMeasure.removeSteppedListener(steppedListener);
		this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(result);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Directional");
	}

	@Override
	public String getKey() {
		return MetaKeys.Directional;
	}
}
