package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.lbs.Interface.IServerService;
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
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.utilities.CursorUtilities;

import java.util.concurrent.CancellationException;

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
		ParameterDataNode parameterDataNode = new ParameterDataNode(ProcessProperties.getString("String_GridRegionAggregationType"), "SUMMARYMESH");
		parameterAggregationType.setItems(parameterDataNode);
		parameterAggregationType.setSelectedItem(parameterDataNode);

		ParameterDataNode[] parameterDataNodes = {new ParameterDataNode(ProcessProperties.getString("String_QuadrilateralMesh"), "0"), new ParameterDataNode(ProcessProperties.getString("String_HexagonalMesh"), "1")};
		parameterMeshType.setSelectedItem(parameterDataNodes[0]);
		parameterMeshType.setItems(parameterDataNodes);
		parameterBounds.setDefaultWarningValue("-74.050,40.650,-73.850,40.850");
		parameterResolution.setDefaultWarningValue("100");
		parameterStaticModel.setToolTip(ProcessProperties.getString("String_StatisticsModeTip"));
		parameterWeightIndex.setToolTip(ProcessProperties.getString("String_WeightIndexTip"));
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
		parameters.getOutputs().addData("GridRegionAggregationResult", Type.UNKOWN);
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
		boolean isSuccess;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			IServerService service = parameterIServerLogin.login();
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			parameterInputDataType.initSourceInput(input);
			CommonSettingCombine fields = new CommonSettingCombine("fields", parameterWeightIndex.getSelectedItem().toString());
			CommonSettingCombine statisticModes = new CommonSettingCombine("statisticModes", parameterStaticModel.getSelectedItem().toString());
			CommonSettingCombine query = new CommonSettingCombine("query", parameterBounds.getSelectedItem().toString());
			CommonSettingCombine resolution = new CommonSettingCombine("resolution", parameterResolution.getSelectedItem().toString());
			CommonSettingCombine meshType = new CommonSettingCombine("meshType", parameterMeshType.getSelectedData().toString());
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			analyst.add(fields, statisticModes, query, resolution, meshType);

			CommonSettingCombine type = new CommonSettingCombine("type", parameterAggregationType.getSelectedData().toString());
			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input, analyst, type);
			JobResultResponse response = service.queryResult(MetaKeys.GRIDREGION_AGGREGATION, commonSettingCombine.getFinalJSon());
			CursorUtilities.setWaitCursor();
			if (null != response) {
				NewMessageBus messageBus = new NewMessageBus(response, new IUpdateProgress() {
					@Override
					public boolean isCancel() {
						return false;
					}

					@Override
					public void setCancel(boolean isCancel) {

					}

					@Override
					public void updateProgress(int percent, String remainTime, String message) throws CancellationException {
						fireRunning(new RunningEvent(MetaProcessGridRegionAggregation.this, percent, message, -1));
					}

					@Override
					public void updateProgress(String message, int percent, String currentMessage) throws CancellationException {

					}

					@Override
					public void updateProgress(int percent, int totalPercent, String remainTime, String message) throws CancellationException {

					}

					@Override
					public void updateProgress(int percent, String recentTask, int totalPercent, String message) throws CancellationException {

					}
				}, DefaultOpenServerMap.INSTANCE);
				isSuccess = messageBus.run();
			} else {
				fireRunning(new RunningEvent(this, 100, "Failed"));
				isSuccess = false;
			}
			parameters.getOutputs().getData("GridRegionAggregationResult").setValue("");
			CursorUtilities.setDefaultCursor();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			return false;
		}
		return isSuccess;
	}

	@Override
	public String getKey() {
		return MetaKeys.GRIDREGION_AGGREGATION;
	}
}
