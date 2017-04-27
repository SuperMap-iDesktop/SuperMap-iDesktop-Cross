package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.AnalyzingPatterns;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;

/**
 * @author XiaJT
 */
public class MetaProcessAutoCorrelation extends MetaProcessAnalyzingPatterns {

	public MetaProcessAutoCorrelation() {
		super();
	}


	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_AutoCorrelation");
	}


	protected void doWork(DatasetVector datasetVector) {
		AnalyzingPatterns.autoCorrelation(datasetVector, parameterPatternsParameter.getPatternParameter());
	}

	@Override
	public String getKey() {
		return MetaKeys.autoCorrelation;
	}
}
