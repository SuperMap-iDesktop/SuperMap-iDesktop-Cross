package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.SpatialMeasure;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;

/**
 * Created by Chen on 2017/7/5 0005.
 */
public class MetaProcessStandardDistance extends MetaProcessSpatialMeasure{
    public MetaProcessStandardDistance() {
        super();
    }

    protected void initHook() {
        resultName = "result_standardDistance";
        OUTPUT_DATASET = "STANDARD_DISTANCE";
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_StandardDistance");
    }

    @Override
    public String getKey() {
        return MetaKeys.STANDARD_DISTANCE;
    }

    @Override
    protected boolean doWork(DatasetVector datasetVector) {
        boolean isSuccessful = false;

        try {
            SpatialMeasure.addSteppedListener(steppedListener);
            // 调用中心要素方法，并获取结果矢量数据集
            DatasetVector result = SpatialMeasure.measureStandardDistance(
                    datasetVector,
                    parameterSaveDataset.getResultDatasource(),
                    parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(parameterSaveDataset.getDatasetName()),
                    measureParameter.getMeasureParameter());
            this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(result);
            isSuccessful = result != null;
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e.getMessage());
        } finally {
            SpatialMeasure.removeSteppedListener(steppedListener);
        }
        return isSuccessful;
    }
}
