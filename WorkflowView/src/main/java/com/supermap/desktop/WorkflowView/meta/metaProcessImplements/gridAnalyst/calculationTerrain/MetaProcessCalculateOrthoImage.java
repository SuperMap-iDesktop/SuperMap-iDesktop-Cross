package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridAnalyst.calculationTerrain;

import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterColor;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.properties.CommonProperties;

import java.awt.*;

/**
 * Created by yuanR on 2017/8/29 0029.
 * 生成正射三维影像
 * OrthoImage
 */
public class MetaProcessCalculateOrthoImage extends MetaProcessCalTerrain {
	private final static String OUTPUT_DATASET = "CalculateHillShadeResult";

	private ParameterColor parameterColorNoColor;

	@Override
	protected void initHook() {

		// 参数设置
		parameterColorNoColor = new ParameterColor(ProcessProperties.getString("String_Label_NoColor_Color"));
		parameterColorNoColor.setSelectedItem(Color.WHITE);
		parameterColorNoColor.setRequisite(true);

		ParameterCombine parameterCombineSet = new ParameterCombine();
		parameterCombineSet.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineSet.addParameters(parameterColorNoColor);

		// 结果设置
		parameterSaveDataset.setSelectedItem("result_calculateHillShade");
		parameters.addParameters(parameterCombineSet, parameterCombineResultDataset);
		parameters.addOutputParameters(OUTPUT_DATASET, ProcessOutputResultProperties.getString("String_CalculateOrthoImageResult"), DatasetTypes.IMAGE, parameterCombineResultDataset);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_CalculateOrthoImage");
	}

	@Override
	public String getKey() {
		return MetaKeys.CALCULATE_ORTHOIMAGE;
	}

	@Override
	protected boolean doWork(DatasetGrid datasetGrid) {
		boolean isSuccessful = false;
		DatasetImage datasetImageResult = null;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			// 这个进度监听有问题，无法生效，先用fireRunning代替-yuanR存疑2017.8.30
//			CalculationTerrain.addSteppedListener(steppedListener);
//			datasetImageResult = CalculationTerrain.calculateOrthoImage(datasetGrid,
//
//					(Color) parameterColorNoColor.getSelectedItem(),
//					parameterSaveDataset.getResultDatasource(),
//					parameterSaveDataset.getDatasetName());
			this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(datasetImageResult);
			isSuccessful = datasetImageResult != null;

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			fireRunning(new RunningEvent(this, 100, "finish"));
//			CalculationTerrain.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}
}
