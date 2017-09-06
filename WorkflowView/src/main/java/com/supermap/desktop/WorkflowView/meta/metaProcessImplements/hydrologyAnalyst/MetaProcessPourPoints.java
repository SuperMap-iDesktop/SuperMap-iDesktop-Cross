package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.hydrologyAnalyst;

import com.supermap.analyst.terrainanalyst.HydrologyAnalyst;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created By Chens on 2017/8/29 0029
 */
public class MetaProcessPourPoints extends MetaProcessHydrology {
	private static final String FLOW_DATA = ProcessProperties.getString("String_GroupBox_FlowData");

	private ParameterDatasourceConstrained flowDatasource;
	private ParameterSingleDataset flowDataset;
	private ParameterNumber numberThreshold;

	@Override
	protected void initField() {
		INPUT_DATA = ProcessProperties.getString("String_GroupBox_FlowDirectionData");
		OUTPUT_DATA = "PourPointsResult";
	}

	@Override
	protected void initParaComponent() {
		flowDatasource = new ParameterDatasourceConstrained();
		flowDataset = new ParameterSingleDataset(DatasetType.GRID);
		numberThreshold = new ParameterNumber(ProcessProperties.getString("String_Label_Threshold"));

		EqualDatasourceConstraint constraintSource1 = new EqualDatasourceConstraint();
		constraintSource1.constrained(flowDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource1.constrained(flowDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		ParameterCombine flowCombine = new ParameterCombine();
		flowCombine.setDescribe(FLOW_DATA);
		flowCombine.addParameters(flowDatasource, flowDataset);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(ProcessProperties.getString("String_setParameter"));
		settingCombine.addParameters(numberThreshold);

		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			flowDatasource.setSelectedItem(datasetGrid.getDatasource());
			flowDataset.setSelectedItem(datasetGrid);
		}
		numberThreshold.setSelectedItem(0);
		numberThreshold.setMaxBit(-1);

		parameters.setParameters(sourceCombine, flowCombine, settingCombine, resultCombine);
		parameters.addInputParameters(FLOW_DATA, DatasetTypes.GRID, flowCombine);
	}

	@Override
	protected String getResultName() {
		return "result_pourPoints";
	}

	@Override
	protected String getOutputText() {
		return ProcessOutputResultProperties.getString("String_Result_PourPoints");
	}

	@Override
	protected Dataset doWork(DatasetGrid src) {
		DatasetGrid srcFlow = null;
		if (parameters.getInputs().getData(FLOW_DATA).getValue() != null) {
			srcFlow = (DatasetGrid) parameters.getInputs().getData(FLOW_DATA).getValue();
		} else {
			srcFlow = (DatasetGrid) flowDataset.getSelectedItem();
		}
		int areaLimit = Integer.parseInt(numberThreshold.getSelectedItem().toString());
		DatasetGrid result = HydrologyAnalyst.pourPoints(src,srcFlow,areaLimit, resultDataset.getResultDatasource(),
				resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getSelectedItem().toString()));

		return result;
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public String getKey() {
		return MetaKeys.POUR_POINTS;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_PourPoints");
	}
}
