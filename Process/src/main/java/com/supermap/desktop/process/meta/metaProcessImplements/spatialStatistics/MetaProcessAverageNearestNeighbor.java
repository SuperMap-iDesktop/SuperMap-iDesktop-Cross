package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.AnalyzingPatterns;
import com.supermap.analyst.spatialstatistics.AnalyzingPatternsResult;
import com.supermap.analyst.spatialstatistics.DistanceMethod;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * @author XiaJT
 */
public class MetaProcessAverageNearestNeighbor extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = "SourceDataset";
	private ParameterDatasourceConstrained parameterDatasourceConstrained = new ParameterDatasourceConstrained();
	private ParameterSingleDataset parameterSingleDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);

	private ParameterTextField parameterTextFieldArea = new ParameterTextField();
	private ParameterComboBox parameterComboBox = new ParameterComboBox();

	public MetaProcessAverageNearestNeighbor() {
		initParameters();
		initParameterStates();
		initParameterConstraint();
	}

	private void initParameters() {
		parameterTextFieldArea.setDescribe(ProcessProperties.getString("String_SearchArea"));
		parameterComboBox.setDescribe(ProcessProperties.getString("String_DistanceMethod"));
		parameterComboBox.addItem(new ParameterDataNode(ProcessProperties.getString("String_EUCLIDEAN"), DistanceMethod.EUCLIDEAN));

		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(parameterDatasourceConstrained, parameterSingleDataset);
		parameterCombine.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.addParameters(parameterTextFieldArea, parameterComboBox);
		parameterCombineSetting.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));

		parameters.addParameters(parameterCombine, parameterCombineSetting);
		parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.VECTOR, parameterCombine);
	}

	private void initParameterStates() {
		DatasetVector defaultDatasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (defaultDatasetVector != null) {
			parameterDatasourceConstrained.setSelectedItem(defaultDatasetVector.getDatasource());
			parameterSingleDataset.setSelectedItem(defaultDatasetVector);
		}
		parameterTextFieldArea.setSelectedItem("0.0");
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(parameterDatasourceConstrained, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(parameterSingleDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);


	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_AverageNearestNeighbor");
	}

	@Override
	public void run() {
		DatasetVector datasetVector;
		Object value = parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue();
		if (value != null && value instanceof DatasetVector) {
			datasetVector = (DatasetVector) value;
		} else {
			datasetVector = (DatasetVector) parameterSingleDataset.getSelectedItem();
		}
		try {
			AnalyzingPatterns.addSteppedListener(steppedListener);
			AnalyzingPatternsResult analyzingPatternsResult = AnalyzingPatterns.averageNearestNeighbor(datasetVector, Double.valueOf((String) parameterTextFieldArea.getSelectedItem()), (DistanceMethod) ((ParameterDataNode) parameterComboBox.getSelectedItem()).getData());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			AnalyzingPatterns.removeSteppedListener(steppedListener);
		}
	}

	@Override
	public String getKey() {
		return MetaKeys.AverageNearestNeighbor;
	}
}
