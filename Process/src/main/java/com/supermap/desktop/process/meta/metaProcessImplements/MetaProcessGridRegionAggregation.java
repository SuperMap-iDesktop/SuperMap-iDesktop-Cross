package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.messageBus.NewMessageBus;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.process.util.TaskUtil;
import com.supermap.desktop.ui.lbs.Interface.IServerService;
import com.supermap.desktop.ui.lbs.params.CommonSettingCombine;
import com.supermap.desktop.ui.lbs.params.JobResultResponse;
import com.supermap.desktop.utilities.CursorUtilities;

/**
 * Created by caolp on 2017-05-26.
 * 网格面聚合分析
 */
public class MetaProcessGridRegionAggregation extends MetaProcess {
	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	private ParameterHDFSPath parameterHDFSPath = new ParameterHDFSPath();
	private ParameterTextField parameterTextFieldXIndex = new ParameterTextField(ProcessProperties.getString("String_XIndex"));
	private ParameterTextField parameterTextFieldYIndex = new ParameterTextField(ProcessProperties.getString("String_YIndex"));
	private ParameterTextField parameterTextFieldSeparator = new ParameterTextField(ProcessProperties.getString("String_Separator"));
	private ParameterComboBox parameterAggregationType = new ParameterComboBox().setDescribe(ProcessProperties.getString("String_AggregationType"));
	private ParameterComboBox parameterMeshType = new ParameterComboBox(ProcessProperties.getString("String_MeshType"));
	private ParameterTextField parameterBounds = new ParameterTextField().setDescribe(ProcessProperties.getString("String_AnalystBounds"));
	private ParameterTextField parameterResolution = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Resolution"));
	private ParameterTextField parameterStaticModel = new ParameterTextField().setDescribe(ProcessProperties.getString("String_StaticModel"));
	private ParameterTextField parameterWeightIndex = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Index"));

	public MetaProcessGridRegionAggregation() {
		initParameters();
		initComponentLayout();
	}

	private void initParameters() {
		parameterHDFSPath.setSelectedItem("hdfs://192.168.20.189:9000/data/newyork_taxi_2013-01_14k.csv");
		parameterTextFieldXIndex.setSelectedItem("10");
		parameterTextFieldYIndex.setSelectedItem("11");
		parameterTextFieldSeparator.setSelectedItem(",");
		parameterTextFieldSeparator.setEnabled(false);

		ParameterDataNode parameterDataNode = new ParameterDataNode(ProcessProperties.getString("String_GridRegionAggregationType"), "SUMMARYMESH");
		parameterAggregationType.setItems(parameterDataNode);
		parameterAggregationType.setSelectedItem(parameterDataNode);

		ParameterDataNode[] parameterDataNodes = {new ParameterDataNode(ProcessProperties.getString("String_QuadrilateralMesh"), "0"), new ParameterDataNode(ProcessProperties.getString("String_HexagonalMesh"), "1")};
		parameterMeshType.setSelectedItem(parameterDataNodes[0]);
		parameterMeshType.setItems(parameterDataNodes);
		parameterBounds.setSelectedItem("-74.050,40.650,-73.850,40.850");
		parameterResolution.setSelectedItem("0.001");
		parameterStaticModel.setSelectedItem("max");
		parameterWeightIndex.setSelectedItem("7");
	}

	private void initComponentLayout() {
		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_setParameter"));
		parameterCombineSetting.addParameters(
				parameterHDFSPath,
				parameterTextFieldXIndex,
				parameterTextFieldYIndex,
				parameterTextFieldSeparator,
				parameterAggregationType,
				parameterMeshType,
				parameterBounds,
				parameterResolution,
				parameterStaticModel,
				parameterWeightIndex);
		parameters.setParameters(
				parameterIServerLogin,
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
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			IServerService service = parameterIServerLogin.login();
			CommonSettingCombine filePath = new CommonSettingCombine("filePath",parameterHDFSPath.getSelectedItem().toString());
			CommonSettingCombine xIndex = new CommonSettingCombine("xIndex",parameterTextFieldXIndex.getSelectedItem().toString());
			CommonSettingCombine yIndex = new CommonSettingCombine("yIndex",parameterTextFieldYIndex.getSelectedItem().toString());
			CommonSettingCombine separator = new CommonSettingCombine("separator",parameterTextFieldSeparator.getSelectedItem().toString());
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			input.add(filePath,xIndex,yIndex,separator);

			CommonSettingCombine fields = new CommonSettingCombine("fields",parameterWeightIndex.getSelectedItem().toString());
			CommonSettingCombine statisticModes = new CommonSettingCombine("statisticModes",parameterStaticModel.getSelectedItem().toString());
			CommonSettingCombine query = new CommonSettingCombine("query",parameterBounds.getSelectedItem().toString());
			CommonSettingCombine resolution = new CommonSettingCombine("resolution",parameterResolution.getSelectedItem().toString());
			CommonSettingCombine meshType = new CommonSettingCombine("meshType",parameterMeshType.getSelectedData().toString());
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			analyst.add(fields,statisticModes,query,resolution,meshType);

			CommonSettingCombine type = new CommonSettingCombine("type",parameterAggregationType.getSelectedData().toString());
			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input,analyst,type);
			JobResultResponse response = service.queryResult(MetaKeys.GRIDREGION_AGGREGATION,commonSettingCombine.getFinalJSon());
			CursorUtilities.setWaitCursor();
			if (null != response) {
				ProcessTask task = TaskUtil.getTask(this);
				NewMessageBus messageBus = new NewMessageBus(response, task);
				messageBus.run();
			}
			fireRunning(new RunningEvent(this, 100, "finished"));
			parameters.getOutputs().getData("GridRegionAggregationResult").setValue("");
			CursorUtilities.setDefaultCursor();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			return false;
		}
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.GRIDREGION_AGGREGATION;
	}
}
