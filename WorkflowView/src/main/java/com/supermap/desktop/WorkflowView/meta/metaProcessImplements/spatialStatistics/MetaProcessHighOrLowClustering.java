package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.AnalyzingPatterns;
import com.supermap.analyst.spatialstatistics.AnalyzingPatternsResult;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.ui.OutputFrame;

/**
 * @author XiaJT
 */
public class MetaProcessHighOrLowClustering extends MetaProcessAnalyzingPatterns {
//	private ParameterTextArea parameterResult;

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_highOrLowClustering");
	}

	@Override
	protected void initHook() {
//		parameterResult = new ParameterTextArea();
//		ParameterCombine parameterCombine = new ParameterCombine();
//		parameterCombine.addParameters(parameterResult);
//		parameterCombine.setDescribe(ProcessProperties.getString("String_result"));
//		parameters.addParameters(parameterCombine);
	}

	@Override
	protected boolean doWork(DatasetVector datasetVector) {
		;
		AnalyzingPatterns.addSteppedListener(steppedListener);
		AnalyzingPatternsResult analyzingPatternsResult = null;
		try {

			analyzingPatternsResult = AnalyzingPatterns.highOrLowClustering(datasetVector, parameterPatternsParameter.getPatternParameter());
			if (analyzingPatternsResult != null) {
				String result = "";
				result += ProcessProperties.getString("String_GeneralG") + " " + analyzingPatternsResult.getIndex() + "\n";
				result += ProcessProperties.getString("String_Expectation") + " " + analyzingPatternsResult.getExpectation() + "\n";
				result += ProcessProperties.getString("String_Variance") + " " + analyzingPatternsResult.getVariance() + "\n";
				result += ProcessProperties.getString("String_ZScor") + " " + analyzingPatternsResult.getZScore() + "\n";
				result += ProcessProperties.getString("String_PValue") + " " + analyzingPatternsResult.getPValue() + "\n";
				// 不显示时间-yuanR2017.9.6
				((OutputFrame) Application.getActiveApplication().getOutput()).timeShowStateChange();
				Application.getActiveApplication().getOutput().output(result);
				((OutputFrame) Application.getActiveApplication().getOutput()).timeShowStateChange();
			}
//			parameterResult.setSelectedItem(result);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			AnalyzingPatterns.removeSteppedListener(steppedListener);
		}
		return analyzingPatternsResult != null;
	}

	@Override
	public String getKey() {
		return MetaKeys.HIGH_OR_LOW_CLUSTERING;
	}
}
