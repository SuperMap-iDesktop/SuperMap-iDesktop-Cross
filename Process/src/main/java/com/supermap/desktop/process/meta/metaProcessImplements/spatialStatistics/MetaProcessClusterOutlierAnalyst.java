package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.ClusteringDistributions;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;

/**
 * @author XiaJT
 * 聚类和异常值分析
 */
public class MetaProcessClusterOutlierAnalyst extends MetaProcessAnalyzingPatterns {

	private final static String OUTPUT_DATASET = "ClusterOutlierResult";
	private ParameterSaveDataset parameterSaveDataset;

	@Override
	protected void initHook() {
		parameterSaveDataset = new ParameterSaveDataset();
		parameterSaveDataset.setDatasetName("result_clusterOutlier");
		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(parameterSaveDataset);
		parameterCombine.setDescribe(CommonProperties.getString("String_ResultSet"));
		DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
		parameters.addParameters(parameterCombine);
		parameters.addOutputParameters(OUTPUT_DATASET, DatasetTypes.VECTOR, parameterCombine);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_clusterOutlierAnalyst");
	}

	@Override
	public String getKey() {
		return MetaKeys.CLUSTER_OUTLIER_ANALYST;
	}

	@Override
	protected boolean doWork(DatasetVector datasetVector) {
		boolean isSuccessful = false;

		try {
			ClusteringDistributions.addSteppedListener(steppedListener);
			DatasetVector result = ClusteringDistributions.clusterOutlierAnalyst(datasetVector, parameterSaveDataset.getResultDatasource(), parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(parameterSaveDataset.getDatasetName()), parameterPatternsParameter.getPatternParameter());
			this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(result);
			isSuccessful = result != null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			ClusteringDistributions.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}
}
