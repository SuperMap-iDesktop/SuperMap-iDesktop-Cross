package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * @author XiaJT
 */
public abstract class MetaProcessAnalyzingPatterns extends MetaProcess {
	private static final String INPUT_SOURCE_DATASET = "SourceDataset";
	protected ParameterDatasource datasource = new ParameterDatasource();
	protected ParameterSingleDataset dataset = new ParameterSingleDataset(DatasetTypeUtilities.getDatasetTypeVector());
	protected ParameterPatternsParameter parameterPatternsParameter = new ParameterPatternsParameter(getKey());


	public MetaProcessAnalyzingPatterns() {
		initParameters();
		initComponentState();
		initParameterConstraint();
		initHook();
	}

	protected void initHook() {

	}

	private void initParameters() {
		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(datasource, dataset);
		parameters.setParameters(parameterCombine, parameterPatternsParameter);
		parameterCombine.setDescribe(CommonProperties.getString("String_ColumnHeader_SourceData"));
		parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.VECTOR, parameterCombine);
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(dataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(parameterPatternsParameter, ParameterPatternsParameter.DATASET_FIELD_NAME);
	}

	private void initComponentState() {
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
			parameterPatternsParameter.setCurrentDataset(datasetVector);
		}
	}


	@Override
	public void run() {
		DatasetVector datasetVector;
		if (parameters.getInputs().getData(INPUT_SOURCE_DATASET) != null &&
				parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
			datasetVector = (DatasetVector) parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue();
		} else {
			datasetVector = (DatasetVector) dataset.getSelectedItem();
		}
		try {
			doWork(datasetVector);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		}
	}

	protected abstract void doWork(DatasetVector datasetVector);

}
