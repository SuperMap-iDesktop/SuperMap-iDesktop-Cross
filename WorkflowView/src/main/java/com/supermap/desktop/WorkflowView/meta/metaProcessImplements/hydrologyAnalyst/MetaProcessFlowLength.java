package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.hydrologyAnalyst;

import com.supermap.analyst.terrainanalyst.HydrologyAnalyst;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created By Chens on 2017/8/29 0029
 */
public class MetaProcessFlowLength extends MetaProcessHydrology {
	protected static final String WEIGHT_DATA = ProcessProperties.getString("String_GroupBox_WeightData");

	private ParameterComboBox comboBoxMethod;
	private ParameterDatasourceConstrained weightDatasource;
	private ParameterSingleDataset weightDataset;

	@Override
	protected void initField() {
		INPUT_DATA = ProcessProperties.getString("String_GroupBox_FlowDirectionData");
		OUTPUT_DATA = "FlowLengthResult";
	}

	@Override
	protected void initParaComponent() {
		weightDatasource = new ParameterDatasourceConstrained();
		weightDataset = new ParameterSingleDataset(DatasetType.GRID);
		comboBoxMethod = new ParameterComboBox(ProcessProperties.getString("String_Label_ComputeMethod"));

		EqualDatasourceConstraint constraintSource1 = new EqualDatasourceConstraint();
		constraintSource1.constrained(weightDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource1.constrained(weightDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		ParameterCombine weightCombine = new ParameterCombine();
		weightCombine.setDescribe(WEIGHT_DATA);
		weightCombine.addParameters(weightDatasource, weightDataset);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(ProcessProperties.getString("String_setParameter"));
		settingCombine.addParameters(comboBoxMethod);

		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			weightDatasource.setSelectedItem(datasetGrid.getDatasource());
			weightDataset.setDatasource(datasetGrid.getDatasource());
			weightDataset.setShowNullValue(true);
		}
		comboBoxMethod.setItems(new ParameterDataNode(ProcessProperties.getString("String_Item_DownStream"),0),
				new ParameterDataNode(ProcessProperties.getString("String_Item_UpStream"),1));

		parameters.setParameters(sourceCombine, weightCombine, settingCombine, resultCombine);
		parameters.addInputParameters(WEIGHT_DATA, DatasetTypes.GRID, settingCombine);
	}

	@Override
	protected String getResultName() {
		return "result_flowLength";
	}

	@Override
	protected String getOutputText() {
		return ProcessOutputResultProperties.getString("String_Result_FlowLength");
	}

	@Override
	protected Dataset doWork(DatasetGrid src) {
		DatasetGrid srcWeight = null;
		if (parameters.getInputs().getData(WEIGHT_DATA).getValue() != null) {
			srcWeight = (DatasetGrid) parameters.getInputs().getData(WEIGHT_DATA).getValue();
		} else if (weightDataset.getSelectedItem() != null) {
			srcWeight = (DatasetGrid) weightDataset.getSelectedItem();
		}
		boolean upStream = comboBoxMethod.getItemAt(1).equals(comboBoxMethod.getSelectedItem());
		DatasetGrid result = HydrologyAnalyst.flowLength(src,srcWeight,upStream,resultDataset.getResultDatasource(),
				resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(resultDataset.getSelectedItem().toString()));

		return result;
	}

	@Override
	public IParameters getParameters() {
		return super.getParameters();
	}

	@Override
	public String getKey() {
		return MetaKeys.FLOW_LENGTH;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_FlowLength");
	}
}
