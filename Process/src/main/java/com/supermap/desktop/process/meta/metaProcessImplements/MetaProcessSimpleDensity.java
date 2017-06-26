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
import com.supermap.desktop.ui.lbs.params.JobResultResponse;
import com.supermap.desktop.ui.lbs.params.KernelDensityJobSetting;
import com.supermap.desktop.utilities.CursorUtilities;

/**
 * Created by caolp on 2017-05-26.
 */
public class MetaProcessSimpleDensity extends MetaProcess {
	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	private ParameterTextField parameterTextFieldXIndex = new ParameterTextField(ProcessProperties.getString("String_XIndex"));
	private ParameterTextField parameterTextFieldYIndex = new ParameterTextField(ProcessProperties.getString("String_YIndex"));
	private ParameterTextField parameterTextFieldSeparator = new ParameterTextField(ProcessProperties.getString("String_Separator"));
	private ParameterHDFSPath parameterHDFSPath;
	private ParameterComboBox parameterComboBoxAnalyseType = new ParameterComboBox(ProcessProperties.getString("String_AnalyseType"));
	private ParameterComboBox parameterComboBoxMeshType = new ParameterComboBox(ProcessProperties.getString("String_MeshType"));
	ParameterTextField parameterIndex = new ParameterTextField(ProcessProperties.getString("String_Index"));
	private ParameterTextField parameterBounds;
	private ParameterTextField parameterResolution;
	ParameterTextField parameterRadius;

	ParameterTextArea parameterTextAreaOutPut = new ParameterTextArea();


	public MetaProcessSimpleDensity() {
		initMetaInfo();
	}

	private void initMetaInfo() {
		parameterTextFieldXIndex.setSelectedItem("10");
		parameterTextFieldYIndex.setSelectedItem("11");
		parameterTextFieldSeparator.setSelectedItem(",");
		parameterTextFieldSeparator.setEnabled(false);
		parameterHDFSPath = new ParameterHDFSPath();
		parameterHDFSPath.setSelectedItem("hdfs://192.168.20.189:9000/data/newyork_taxi_2013-01_14k.csv");
		ParameterDataNode parameterDataNode = new ParameterDataNode(ProcessProperties.getString("String_SimplePointDensity"), "0");
		parameterComboBoxAnalyseType.setItems(parameterDataNode);
		parameterComboBoxAnalyseType.setSelectedItem(parameterDataNode);
		parameterComboBoxMeshType.setItems(new ParameterDataNode(ProcessProperties.getString("String_QuadrilateralMesh"), "0"),
				new ParameterDataNode(ProcessProperties.getString("String_HexagonalMesh"), "1"));

		//流程图中不支持在地图中绘制范围，范围表示与iServer的表示相同
		parameterBounds = new ParameterTextField().setDescribe(ProcessProperties.getString("String_AnalystBounds"));
		parameterBounds.setSelectedItem("-74.050,40.550,-73.750,40.950");
		parameterIndex = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Index"));
		parameterIndex.setSelectedItem("10");
		parameterResolution = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Resolution"));
		parameterResolution.setSelectedItem("0.004");
		parameterRadius = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Radius"));
		parameterRadius.setSelectedItem("0.004");

		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_setParameter"));
		parameterCombineSetting.addParameters(
				parameterHDFSPath,
				parameterTextFieldXIndex,
				parameterTextFieldYIndex,
				parameterTextFieldSeparator,
				parameterComboBoxAnalyseType,
				parameterComboBoxMeshType,
				parameterIndex,
				parameterBounds,
				parameterResolution,
				parameterRadius);

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.setDescribe(ProcessProperties.getString("String_result"));
		parameterCombineResult.addParameters(parameterTextAreaOutPut);
		parameters.setParameters(
				parameterIServerLogin,
				parameterCombineSetting
		);
		parameters.getOutputs().addData("SimpleDensityResult", Type.UNKOWN);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_SimpleDensityAnalyst");
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
			//简单核密度分析功能实现
			KernelDensityJobSetting kenelDensityJobSetting = new KernelDensityJobSetting();
			kenelDensityJobSetting.analyst.method = (String) parameterComboBoxAnalyseType.getSelectedData();
			kenelDensityJobSetting.analyst.meshType = (String) parameterComboBoxMeshType.getSelectedData();
			kenelDensityJobSetting.analyst.fields = (String) parameterIndex.getSelectedItem();
			kenelDensityJobSetting.analyst.query = parameterBounds.getSelectedItem().toString();
			kenelDensityJobSetting.analyst.resolution = parameterResolution.getSelectedItem().toString();
			kenelDensityJobSetting.analyst.radius = parameterRadius.getSelectedItem().toString();
			kenelDensityJobSetting.input.filePath = parameterHDFSPath.getSelectedItem().toString();
			kenelDensityJobSetting.input.xIndex = parameterTextFieldXIndex.getSelectedItem().toString();
			kenelDensityJobSetting.input.yIndex = parameterTextFieldYIndex.getSelectedItem().toString();
			kenelDensityJobSetting.input.separator = parameterTextFieldSeparator.getSelectedItem().toString();
			JobResultResponse response = service.query(kenelDensityJobSetting);
			CursorUtilities.setWaitCursor();
			if (null != response) {
				CursorUtilities.setDefaultCursor();
				ProcessTask task = TaskUtil.getTask(this);
				NewMessageBus messageBus = new NewMessageBus(response, task);
				messageBus.run();
			}
			fireRunning(new RunningEvent(this, 100, "finished"));
			parameters.getOutputs().getData("SimpleDensityResult").setValue("");// // TODO: 2017/5/26
			CursorUtilities.setDefaultCursor();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.SIMPLE_DENSITY;
	}
}
