package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;

/**
 * @author XiaJT
 */
public class MetaProcessIncrementalAutoCorrelation extends MetaProcessAnalyzingPatterns {
	// TODO: 2017/4/27
	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_incrementalAutoCorrelation");
	}

	@Override
	public String getKey() {
		return MetaKeys.incrementalAutoCorrelation;
	}

	@Override
	protected void doWork(DatasetVector datasetVector) {
//		return AnalyzingPatterns.incrementalAutoCorrelation(datasetVector,);
	}
}
