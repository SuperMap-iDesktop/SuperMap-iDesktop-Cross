package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.AnalyzingPatterns;
import com.supermap.analyst.spatialstatistics.DistanceMethod;
import com.supermap.analyst.spatialstatistics.IncrementalParameter;
import com.supermap.analyst.spatialstatistics.IncrementalResult;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.implement.ParameterFieldComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * @author XiaJT
 */
public class MetaProcessIncrementalAutoCorrelation extends MetaProcess {
	// TODO: 2017/4/27
	private final static String INPUT_SOURCE_DATASET = "SourceDataset";
	private ParameterDatasourceConstrained datasource = new ParameterDatasourceConstrained();
	private ParameterSingleDataset dataset = new ParameterSingleDataset(DatasetTypeUtilities.getDatasetTypeVector());
	private ParameterFieldComboBox parameterFieldComboBox = new ParameterFieldComboBox();
	private ParameterCheckBox parameterCheckBox = new ParameterCheckBox();
	private ParameterTextField parameterTextFieldBeginDistance = new ParameterTextField();
	private ParameterTextField parameterTextFieldIncrementalDistance = new ParameterTextField();
	private ParameterTextField parameterTextFieldIncrementalNumber = new ParameterTextField();
	private ParameterComboBox parameterDistanceMethod = new ParameterComboBox();

	public MetaProcessIncrementalAutoCorrelation() {
		initParameters();
		initParameterState();
		initParameterConstraint();
	}

	private void initParameters() {
		parameterDistanceMethod.addItem(new ParameterDataNode(ProcessProperties.getString("String_EUCLIDEAN"), DistanceMethod.EUCLIDEAN));

		parameterFieldComboBox.setDescribe(ProcessProperties.getString("String_AssessmentField"));
		parameterCheckBox.setDescribe(ProcessProperties.getString("String_Standardization"));
		parameterTextFieldBeginDistance.setDescribe(ProcessProperties.getString("String_BeginDistance"));
		parameterTextFieldIncrementalDistance.setDescribe(ProcessProperties.getString("String_IncrementalDistance"));
		parameterTextFieldIncrementalNumber.setDescribe(ProcessProperties.getString("String_IncrementalNumber"));
		parameterDistanceMethod.setDescribe(ProcessProperties.getString("String_DistanceMethod"));

		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(datasource, dataset);
		parameterCombine.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.addParameters(parameterFieldComboBox, parameterCheckBox, parameterTextFieldBeginDistance,
				parameterTextFieldIncrementalDistance, parameterTextFieldIncrementalNumber, parameterDistanceMethod);
		parameterCombineSetting.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));

		parameters.setParameters(parameterCombine, parameterCombineSetting);
		parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.VECTOR, parameterCombine);
	}

	private void initParameterState() {
		DatasetVector defaultDatasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (defaultDatasetVector != null) {
			datasource.setSelectedItem(defaultDatasetVector.getDatasource());
			dataset.setSelectedItem(defaultDatasetVector);
			parameterFieldComboBox.setDataset(defaultDatasetVector);
		}
		parameterTextFieldBeginDistance.setSelectedItem("0.0");
		parameterTextFieldIncrementalDistance.setSelectedItem("0.0");
		parameterTextFieldIncrementalNumber.setSelectedItem("0");
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(dataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(parameterFieldComboBox, ParameterFieldComboBox.DATASET_FIELD_NAME);

	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_incrementalAutoCorrelation");
	}

	@Override
	public String getKey() {
		return MetaKeys.incrementalAutoCorrelation;
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;

		DatasetVector datasetVector;
		Object value = parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue();
		if (value != null && value instanceof DatasetVector) {
			datasetVector = (DatasetVector) value;
		} else {
			datasetVector = (DatasetVector) dataset.getSelectedItem();
		}

		IncrementalParameter incrementalParameter = new IncrementalParameter();
		incrementalParameter.setAssessmentFieldName(parameterFieldComboBox.getFieldName());
		incrementalParameter.setStandardization(Boolean.valueOf((String) parameterCheckBox.getSelectedItem()));
		incrementalParameter.setBeginDistance(Double.valueOf((String) parameterTextFieldBeginDistance.getSelectedItem()));
		incrementalParameter.setIncrementalNumber(Integer.valueOf((String) parameterTextFieldIncrementalNumber.getSelectedItem()));
		incrementalParameter.setIncrementalDistance(Double.valueOf((String) parameterTextFieldIncrementalDistance.getSelectedItem()));
		incrementalParameter.setDistanceMethod((DistanceMethod) ((ParameterDataNode) parameterDistanceMethod.getSelectedItem()).getData());
		try {
			AnalyzingPatterns.addSteppedListener(steppedListener);
			IncrementalResult[] incrementalResults = AnalyzingPatterns.incrementalAutoCorrelation(datasetVector, incrementalParameter);
			isSuccessful = incrementalResults != null && incrementalResults.length > 0;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			AnalyzingPatterns.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}
}
