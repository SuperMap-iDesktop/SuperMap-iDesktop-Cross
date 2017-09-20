package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.lbs.params.CommonSettingCombine;
import com.supermap.desktop.lbs.params.JobResultResponse;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.messageBus.NewMessageBus;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.DefaultOpenServerMap;
import com.supermap.desktop.utilities.CursorUtilities;

/**
 * Created by caolp on 2017-05-26.
 * 网格面聚合分析
 */
public class MetaProcessGridRegionAggregation extends MetaProcess {
	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	ParameterInputDataType parameterInputDataType = new ParameterInputDataType();
	private ParameterComboBox parameterAggregationType = new ParameterComboBox().setDescribe(ProcessProperties.getString("String_AggregationType"));
	private ParameterComboBox parameterMeshType = new ParameterComboBox(ProcessProperties.getString("String_MeshType"));
	private ParameterDefaultValueTextField parameterBounds = new ParameterDefaultValueTextField().setDescribe(ProcessProperties.getString("String_AnalystBounds"));
	private ParameterDefaultValueTextField parameterResolution = new ParameterDefaultValueTextField().setDescribe(ProcessProperties.getString("String_MeshSize"));
	private ParameterDefaultValueTextField parameterStaticModel = new ParameterDefaultValueTextField().setDescribe(ProcessProperties.getString("String_StaticModel"));
	private ParameterDefaultValueTextField parameterWeightIndex = new ParameterDefaultValueTextField().setDescribe(ProcessProperties.getString("String_Index"));

	public MetaProcessGridRegionAggregation() {
		initParameters();
		initComponentLayout();
	}

	private void initParameters() {
		parameterInputDataType.setDescribe(ProcessProperties.getString("String_FileInputPath"));
		parameterIServerLogin.setInputDataType(this.parameterInputDataType);
		parameterInputDataType.setSupportDatasetType(DatasetType.POINT);
		parameterIServerLogin.setDataType(parameterInputDataType.supportDatasetType);
		ParameterDataNode parameterDataNode = new ParameterDataNode(ProcessProperties.getString("String_GridRegionAggregationType"), "SUMMARYMESH");
		parameterAggregationType.setRequisite(true);
		parameterAggregationType.setItems(parameterDataNode);
		parameterAggregationType.setSelectedItem(parameterDataNode);

		ParameterDataNode[] parameterDataNodes = {new ParameterDataNode(ProcessProperties.getString("String_QuadrilateralMesh"), "0"), new ParameterDataNode(ProcessProperties.getString("String_HexagonalMesh"), "1")};
		parameterMeshType.setSelectedItem(parameterDataNodes[0]);
		parameterMeshType.setRequisite(true);
		parameterMeshType.setItems(parameterDataNodes);
		parameterBounds.setDefaultWarningValue("-74.050,40.650,-73.850,40.850");
		parameterResolution.setDefaultWarningValue("100");
		parameterResolution.setRequisite(true);
		parameterStaticModel.setTipButtonMessage(ProcessProperties.getString("String_StatisticsModeTip"));
		parameterWeightIndex.setTipButtonMessage(ProcessProperties.getString("String_WeightIndexTip"));
		parameterInputDataType.setSupportDatasetType(DatasetType.POINT);
	}

	private void initComponentLayout() {
		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_AnalystSet"));
		parameterCombineSetting.addParameters(
				parameterAggregationType,
				parameterMeshType,
				parameterBounds,
				parameterResolution,
				parameterWeightIndex,
				parameterStaticModel);
		parameters.setParameters(
				parameterIServerLogin,
				parameterInputDataType,
				parameterCombineSetting
		);
		parameters.getOutputs().addData("GridRegionAggregationResult", ProcessOutputResultProperties.getString("String_NetworkRegionAnalysisResult"), Type.UNKOWN);
	}


	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_GridRegionAggregation");
	}

	@Override
	public IParameterPanel getComponent() {
		return this.parameters.getPanel();
	}

	@Override
	public boolean execute() {
		boolean isSuccessful;
		try {
			fireRunning(new RunningEvent(this, ProcessProperties.getString("String_Running")));
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			parameterInputDataType.initSourceInput(input);
			CommonSettingCombine fields = new CommonSettingCombine("fields", parameterWeightIndex.getSelectedItem());
			CommonSettingCombine statisticModes = new CommonSettingCombine("statisticModes", parameterStaticModel.getSelectedItem());
			CommonSettingCombine query = new CommonSettingCombine("query", parameterBounds.getSelectedItem());
			CommonSettingCombine resolution = new CommonSettingCombine("resolution", parameterResolution.getSelectedItem());
			CommonSettingCombine meshType = new CommonSettingCombine("meshType", parameterMeshType.getSelectedData().toString());
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			analyst.add(fields, statisticModes, query, resolution, meshType);

			CommonSettingCombine type = new CommonSettingCombine("type", parameterAggregationType.getSelectedData().toString());
			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input, analyst, type);
			JobResultResponse response = parameterIServerLogin.getService().queryResult(MetaKeys.GRIDREGION_AGGREGATION, commonSettingCombine.getFinalJSon());
			CursorUtilities.setWaitCursor();
			if (null != response) {
				NewMessageBus messageBus = new NewMessageBus(response, DefaultOpenServerMap.INSTANCE);
				isSuccessful = messageBus.run();
			} else {
				isSuccessful = false;
			}

			parameters.getOutputs().getData("GridRegionAggregationResult").setValue("");
			CursorUtilities.setDefaultCursor();
		} catch (Exception e) {
			isSuccessful = false;
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
		}

		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.GRIDREGION_AGGREGATION;
	}
}
