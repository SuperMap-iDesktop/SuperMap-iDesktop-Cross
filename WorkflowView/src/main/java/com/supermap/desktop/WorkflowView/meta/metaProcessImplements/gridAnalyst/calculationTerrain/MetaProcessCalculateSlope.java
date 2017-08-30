package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridAnalyst.calculationTerrain;

import com.supermap.analyst.spatialanalyst.CalculationTerrain;
import com.supermap.analyst.spatialanalyst.SlopeType;
import com.supermap.data.DatasetGrid;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterNumber;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created by yuanR on 2017/8/30 .
 * 坡度
 */
public class MetaProcessCalculateSlope extends MetaProcessCalTerrain {
	private final static String OUTPUT_DATASET = "CalculateSlopeResult";

	private ParameterComboBox parameterComboBoxSlopeType;
	// 高程缩放系数
	private ParameterNumber parameterZFactor;

	@Override
	protected void initHook() {

		// 参数设置
		parameterComboBoxSlopeType = new ParameterComboBox(ProcessProperties.getString("String_CalculateSlope_SlopeType"));
		parameterComboBoxSlopeType.setItems(
				new ParameterDataNode(ProcessProperties.getString("String_SlopeType_Degree"), SlopeType.DEGREE),
				new ParameterDataNode(ProcessProperties.getString("String_SlopeType_Percent"), SlopeType.PERCENT),
				new ParameterDataNode(ProcessProperties.getString("String_SlopeType_Radian"), SlopeType.RADIAN)
		);
		parameterComboBoxSlopeType.setSelectedItem(SlopeType.DEGREE);

		parameterZFactor = new ParameterNumber(ProcessProperties.getString("String_CalculateSlope_zFactor"));
		parameterZFactor.setMinValue(0);
		parameterZFactor.setIsIncludeMin(false);
		parameterZFactor.setMaxBit(22);
		parameterZFactor.setSelectedItem(1);

		ParameterCombine parameterCombineSet = new ParameterCombine();
		parameterCombineSet.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineSet.addParameters(parameterComboBoxSlopeType, parameterZFactor);

		// 结果设置
		parameterSaveDataset.setSelectedItem("result_calculateSlope");
		parameters.addParameters(parameterCombineSet, parameterCombineResultDataset);
		parameters.addOutputParameters(OUTPUT_DATASET, ProcessOutputResultProperties.getString("String_CalculateSlopeResult"), DatasetTypes.GRID, parameterCombineResultDataset);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_CalculateSlope");
	}

	@Override
	public String getKey() {
		return MetaKeys.CALCULATE_SLOPE;
	}

	@Override
	protected boolean doWork(DatasetGrid datasetGrid) {
		boolean isSuccessful = false;
		DatasetGrid datasetGridResult = null;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			// 这个进度监听有问题，无法生效，先用fireRunning代替-yuanR存疑2017.8.30
//			CalculationTerrain.addSteppedListener(steppedListener);
			Double zFactor = StringUtilities.getNumber(parameterZFactor.getSelectedItem());
			datasetGridResult = CalculationTerrain.calculateSlope(datasetGrid, (SlopeType) parameterComboBoxSlopeType.getSelectedData(),
					zFactor, parameterSaveDataset.getResultDatasource(), parameterSaveDataset.getDatasetName());
			this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(datasetGridResult);
			isSuccessful = datasetGridResult != null;

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			fireRunning(new RunningEvent(this, 100, "finish"));
//			CalculationTerrain.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}
}
