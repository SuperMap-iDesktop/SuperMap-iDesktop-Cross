package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.AnalyzingPatterns;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;

/**
 * @author XiaJT
 */
public class MetaProcessHighOrLowClustering extends MetaProcessAnalyzingPatterns {
	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_highOrLowClustering");
	}

	@Override
	protected void doWork(DatasetVector datasetVector) {
		AnalyzingPatterns.highOrLowClustering(datasetVector, parameterPatternsParameter.getPatternParameter());
	}

	@Override
	public String getKey() {
		return MetaKeys.highOrLowClustering;
	}
}
