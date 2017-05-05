package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;

/**
 * @author XiaJT
 */
public class MetaProcessMeanCenter extends MetaProcessSpatialMeasure {
	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_MeanCenter");
	}


	@Override
	public void run() {

	}

	@Override
	protected void doWork(DatasetVector datasetVector) {

	}

	@Override
	public String getKey() {
		return MetaKeys.MeanCenter;
	}
}
