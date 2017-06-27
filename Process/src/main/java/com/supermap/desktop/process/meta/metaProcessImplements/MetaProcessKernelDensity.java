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
 * Created by xie on 2017/2/10.
 */
public class MetaProcessKernelDensity extends MetaProcess {
	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	private ParameterTextField parameterTextFieldXIndex = new ParameterTextField(ProcessProperties.getString("String_XIndex"));
	private ParameterTextField parameterTextFieldYIndex = new ParameterTextField(ProcessProperties.getString("String_YIndex"));
	private ParameterTextField parameterTextFieldSeparator = new ParameterTextField(ProcessProperties.getString("String_Separator"));
	private ParameterHDFSPath parameterHDFSPath;
	private ParameterComboBox parameterComboBoxAnalyseType = new ParameterComboBox(ProcessProperties.getString("String_AnalyseType"));
	private ParameterComboBox parameterComboBoxMeshType = new ParameterComboBox(ProcessProperties.getString("String_MeshType"));
	private ParameterTextField parameterIndex = new ParameterTextField();
	private ParameterTextField parameterBounds;
	private ParameterTextField parameterResolution;
	ParameterTextField parameterRadius;
	ParameterTextArea parameterTextAreaOutPut = new ParameterTextArea();


	public MetaProcessKernelDensity() {
		initMetaInfo();
	}

	private void initMetaInfo() {
		parameterTextFieldXIndex.setSelectedItem("10");
		parameterTextFieldYIndex.setSelectedItem("11");
		parameterTextFieldSeparator.setSelectedItem(",");
		parameterTextFieldSeparator.setEnabled(false);

		parameterHDFSPath = new ParameterHDFSPath();
		parameterHDFSPath.setSelectedItem("hdfs://192.168.20.189:9000/data/newyork_taxi_2013-01_14k.csv");
		ParameterDataNode parameterDataNode = new ParameterDataNode(ProcessProperties.getString("String_KernelDensity"), "1");
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
		parameters.getOutputs().addData("KernelDensityResult", Type.UNKOWN);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_KernelDensityAnalyst");
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
			//核密度分析功能实现
			CommonSettingCombine filePath = new CommonSettingCombine("filePath",parameterHDFSPath.getSelectedItem().toString());
			CommonSettingCombine xIndex = new CommonSettingCombine("xIndex",parameterTextFieldXIndex.getSelectedItem().toString());
			CommonSettingCombine yIndex = new CommonSettingCombine("yIndex",parameterTextFieldYIndex.getSelectedItem().toString());
			CommonSettingCombine separator = new CommonSettingCombine("separator",parameterTextFieldSeparator.getSelectedItem().toString());
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			input.add(filePath,xIndex,yIndex,separator);


			CommonSettingCombine method = new CommonSettingCombine("method",(String) parameterComboBoxAnalyseType.getSelectedData());
			CommonSettingCombine meshType = new CommonSettingCombine("meshType",(String) parameterComboBoxMeshType.getSelectedData());
			CommonSettingCombine fields = new CommonSettingCombine("fields",(String) parameterIndex.getSelectedItem());
			CommonSettingCombine query = new CommonSettingCombine("query",parameterBounds.getSelectedItem().toString());
			CommonSettingCombine resolution = new CommonSettingCombine("resolution",parameterResolution.getSelectedItem().toString());
			CommonSettingCombine radius = new CommonSettingCombine("radius",parameterRadius.getSelectedItem().toString());

			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			analyst.add(query,resolution,radius,method,meshType,fields);

			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input,analyst);
			JobResultResponse response = service.queryResult(MetaKeys.KERNEL_DENSITY,commonSettingCombine.getFinalJSon());
			CursorUtilities.setWaitCursor();
			if (null != response) {
				CursorUtilities.setDefaultCursor();
				ProcessTask task = TaskUtil.getTask(this);
				NewMessageBus messageBus = new NewMessageBus(response, task);
				messageBus.run();
			}
			fireRunning(new RunningEvent(this, 100, "finished"));
			parameters.getOutputs().getData("KernelDensityResult").setValue("");// // TODO: 2017/5/26
			CursorUtilities.setDefaultCursor();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.KERNEL_DENSITY;
	}

}
