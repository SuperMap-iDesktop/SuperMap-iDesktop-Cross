package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridAnalyst.calculationTerrain;

import com.supermap.analyst.spatialanalyst.CalculationTerrain;
import com.supermap.analyst.spatialanalyst.ShadowMode;
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
 * Created by yuanR on 2017/8/29 0029.
 * 三维晕眩图
 */
public class MetaProcessCalculateHillShade extends MetaProcessCalTerrain {
	private final static String OUTPUT_DATASET = "CalculateHillShadeResult";

	private ParameterComboBox parameterComboBoxShadowType;
	// 方位角
	private ParameterNumber parameterAzimuth;
	// 高度角
	private ParameterNumber parameterAltitudeAngle;
	// 高程缩放系数
	private ParameterNumber parameterZFactor;

	@Override
	protected void initHook() {

		// 参数设置
		parameterComboBoxShadowType = new ParameterComboBox(ProcessProperties.getString("String_Label_ShadowType"));
		parameterComboBoxShadowType.setItems(
				new ParameterDataNode(ProcessProperties.getString("String_ShadowMode_Illuminatton"), ShadowMode.IllUMINATION),
				new ParameterDataNode(ProcessProperties.getString("String_ShadowMode_Shadow"), ShadowMode.SHADOW),
				new ParameterDataNode(ProcessProperties.getString("String_ShadowMode_IlluminattonAndShadow"), ShadowMode.IllUMINATION_AND_SHADOW)
		);
		parameterComboBoxShadowType.setSelectedItem(ShadowMode.IllUMINATION);

		parameterAzimuth = new ParameterNumber(ProcessProperties.getString("String_Label_LightAzimuth"));
		parameterAltitudeAngle = new ParameterNumber(ProcessProperties.getString("String_Label_LightAltitudeAngle"));
		parameterZFactor = new ParameterNumber(ProcessProperties.getString("String_CalculateSlope_zFactor"));

		parameterAzimuth.setMinValue(0);
		parameterAzimuth.setIsIncludeMin(true);
		parameterAzimuth.setMaxValue(360);
		parameterAzimuth.setMaxBit(22);
		parameterAzimuth.setSelectedItem(315);

		parameterAltitudeAngle.setMinValue(0);
		parameterAltitudeAngle.setIsIncludeMin(true);
		parameterAltitudeAngle.setMaxValue(360);
		parameterAltitudeAngle.setMaxBit(22);
		parameterAltitudeAngle.setSelectedItem(45);

		parameterZFactor.setMinValue(0);
		parameterZFactor.setIsIncludeMin(false);
		parameterZFactor.setMaxBit(22);
		parameterZFactor.setSelectedItem(1);

		ParameterCombine parameterCombineSet = new ParameterCombine();
		parameterCombineSet.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineSet.addParameters(parameterAzimuth, parameterAltitudeAngle, parameterComboBoxShadowType, parameterZFactor);

		// 结果设置
		parameters.addParameters(parameterCombineSet, parameterCombineResultDataset);
		parameters.addOutputParameters(OUTPUT_DATASET, ProcessOutputResultProperties.getString("String_CalculateHillShadeResult"), DatasetTypes.GRID, parameterCombineResultDataset);
	}



	@Override
	protected String getDefaultResultName() {
		return "result_calculateHillShade";
	}


	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_CalculateHillShade");
	}

	@Override
	public String getKey() {
		return MetaKeys.CALCULATE_HILLSHADE;
	}

	@Override
	protected boolean doWork(DatasetGrid datasetGrid) {
		boolean isSuccessful = false;
		DatasetGrid datasetGridResult = null;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			// 这个进度监听有问题，无法生效，先用fireRunning代替-yuanR存疑2017.8.30
//			CalculationTerrain.addSteppedListener(steppedListener);
			Double azimuth = StringUtilities.getNumber(parameterAzimuth.getSelectedItem());
			Double altitudeAngle = StringUtilities.getNumber(parameterAltitudeAngle.getSelectedItem());
			Double zFactor = StringUtilities.getNumber(parameterZFactor.getSelectedItem());
			datasetGridResult = CalculationTerrain.calculateHillShade(datasetGrid,
					(ShadowMode) parameterComboBoxShadowType.getSelectedData(),
					azimuth, altitudeAngle, zFactor,
					parameterSaveDataset.getResultDatasource(),
					parameterSaveDataset.getDatasetName());
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
