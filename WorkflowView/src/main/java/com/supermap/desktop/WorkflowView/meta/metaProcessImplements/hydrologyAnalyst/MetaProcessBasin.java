package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.hydrologyAnalyst;

import com.supermap.analyst.terrainanalyst.HydrologyAnalyst;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

/**
 * Created By Chens on 2017/8/29 0029
 */
public class MetaProcessBasin extends MetaProcessHydrology {

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	protected void initField() {
		INPUT_DATA = ProcessProperties.getString("String_GroupBox_FlowDirectionData");
		OUTPUT_DATA = "BasinResult";
	}

	@Override
	protected void initParaComponent() {
		parameters.setParameters(sourceCombine,resultCombine);
	}

	@Override
	protected String getResultName() {
		return "result_basin";
	}

	@Override
	protected String getOutputText() {
		return ProcessOutputResultProperties.getString("String_Result_Basin");
	}

	@Override
	protected Dataset doWork(DatasetGrid src) {
		DatasetGrid result = HydrologyAnalyst.basin(src, resultDataset.getResultDatasource(),
				resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getDatasetName()));
		return result;
	}

	@Override
	public String getKey() {
		return MetaKeys.BASIN;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_Basin");
	}
}
