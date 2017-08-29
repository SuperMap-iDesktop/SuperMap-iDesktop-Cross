package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridAnalyst.calculationTerrain;

import com.supermap.analyst.spatialanalyst.CalculationTerrain;
import com.supermap.data.DatasetGrid;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;

/**
 * Created by yuanR on 2017/8/29 0029.
 * 坡向分析
 */
public class MetaProcessCalculateAspect extends MetaProcessCalTerrain {
	private final static String OUTPUT_DATASET = "CalculateAspectResult";

	@Override
	protected void initHook() {

		// 参数设置-坡向分析无参数设置

		// 结果设置
		parameterSaveDataset.setSelectedItem("result_calculateAspect");
		parameters.addParameters(parameterCombineResultDataset);
		parameters.addOutputParameters(OUTPUT_DATASET, ProcessOutputResultProperties.getString("String_CalculateAspectResult"), DatasetTypes.GRID, parameterCombineResultDataset);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_CalculateAspect");
	}

	@Override
	public String getKey() {
		return MetaKeys.CALCULATE_ASPECT;
	}

	@Override
	protected boolean doWork(DatasetGrid datasetGrid) {
		boolean isSuccessful = false;
		DatasetGrid datasetGridResult = null;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			// 这个进度监听有问题，无法生效，先用fireRunning代替-yuanR存疑2017.8.29
//			CalculationTerrain.addSteppedListener(steppedListener);
			datasetGridResult = CalculationTerrain.calculateAspect(datasetGrid, parameterSaveDataset.getResultDatasource(), parameterSaveDataset.getDatasetName());
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
