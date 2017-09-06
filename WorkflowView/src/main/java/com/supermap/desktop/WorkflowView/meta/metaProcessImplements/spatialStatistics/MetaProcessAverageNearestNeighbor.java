package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.AnalyzingPatterns;
import com.supermap.analyst.spatialstatistics.AnalyzingPatternsResult;
import com.supermap.analyst.spatialstatistics.DistanceMethod;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.OutputFrame;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * @author XiaJT
 * 平均最近邻分析
 */
public class MetaProcessAverageNearestNeighbor extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = "SourceDataset";
	private ParameterDatasourceConstrained parameterDatasourceConstrained = new ParameterDatasourceConstrained();
	private ParameterSingleDataset parameterSingleDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);

	private ParameterNumber parameterTextFieldArea = new ParameterNumber();
	private ParameterComboBox parameterComboBox = new ParameterComboBox();
	// 添加展示结果的textArea--yuanR
	//去除展示textArea，分析执行过后会关闭窗口，将分析结果展示在output中-yuanR2017.9.6
//	private ParameterTextArea parameterResult = new ParameterTextArea();

	public MetaProcessAverageNearestNeighbor() {
		initParameters();
		initParameterStates();
		initParameterConstraint();
	}

	private void initParameters() {
		parameterTextFieldArea.setDescribe(ProcessProperties.getString("String_SearchArea"));
		parameterTextFieldArea.setUnit(ProcessProperties.getString("String_SquareMeter"));
		parameterComboBox.setDescribe(ProcessProperties.getString("String_DistanceMethod"));
		parameterComboBox.setItems(new ParameterDataNode(ProcessProperties.getString("String_EUCLIDEAN"), DistanceMethod.EUCLIDEAN),
				new ParameterDataNode(ProcessProperties.getString("String_MANHATTAN"), DistanceMethod.MANHATTAN));
		// 源数据
		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(parameterDatasourceConstrained, parameterSingleDataset);
		parameterCombine.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));
		// 参数设置
		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.addParameters(parameterTextFieldArea, parameterComboBox);
		parameterCombineSetting.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
//		// 结果展示
//		ParameterCombine parameterCombineResult = new ParameterCombine();
//		parameterCombineResult.setDescribe(ProcessProperties.getString("String_result"));
//		parameterCombineResult.addParameters(parameterResult);

		parameters.addParameters(parameterCombine, parameterCombineSetting);
		parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.VECTOR, parameterCombine);
//		parameters.addParameters(parameterCombineResult);

	}

	private void initParameterStates() {
		DatasetVector defaultDatasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (defaultDatasetVector != null) {
			parameterDatasourceConstrained.setSelectedItem(defaultDatasetVector.getDatasource());
			parameterSingleDataset.setSelectedItem(defaultDatasetVector);
		}
		parameterTextFieldArea.setSelectedItem("0.0");
		parameterTextFieldArea.setMinValue(0);
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
	public boolean execute() {
		boolean isSuccessful = false;
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
			isSuccessful = analyzingPatternsResult != null;
			// 如果分析成功，进行结果数据的展示--yuanR
			if (isSuccessful) {
				String result = "";
				result += ProcessProperties.getString("String_Nearest_Neighbor_Ratio") + " " + analyzingPatternsResult.getIndex() + "\n";
				result += ProcessProperties.getString("String_Expected_Mean_Distance") + " " + analyzingPatternsResult.getExpectation() + "\n";
				result += ProcessProperties.getString("String_Observed_Mean_Distance") + " " + analyzingPatternsResult.getVariance() + "\n";
				result += ProcessProperties.getString("String_ZScor") + " " + analyzingPatternsResult.getZScore() + "\n";
				result += ProcessProperties.getString("String_PValue") + " " + analyzingPatternsResult.getPValue() + "\n";
				// 不显示时间-yuanR2017.9.6
				((OutputFrame) Application.getActiveApplication().getOutput()).setShowTime(false);
				Application.getActiveApplication().getOutput().output(result);
				((OutputFrame) Application.getActiveApplication().getOutput()).setShowTime(true);
//				parameterResult.setSelectedItem(result);
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			AnalyzingPatterns.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.AVERAGE_NEAREST_NEIGHBOR;
	}
}
