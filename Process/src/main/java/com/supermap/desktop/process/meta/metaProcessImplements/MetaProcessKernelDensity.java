package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.messageBus.NewMessageBus;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterHDFSPath;
import com.supermap.desktop.process.parameter.implement.ParameterPassword;
import com.supermap.desktop.process.parameter.implement.ParameterTextArea;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.process.util.TaskUtil;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.lbs.Interface.IServerService;
import com.supermap.desktop.ui.lbs.impl.IServerServiceImpl;
import com.supermap.desktop.ui.lbs.params.IServerLoginInfo;
import com.supermap.desktop.ui.lbs.params.JobResultResponse;
import com.supermap.desktop.ui.lbs.params.KernelDensityJobSetting;
import com.supermap.desktop.utilities.CursorUtilities;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by xie on 2017/2/10.
 */
public class MetaProcessKernelDensity extends MetaProcess {

	private ParameterTextField parameterTextFieldAddress = new ParameterTextField(CoreProperties.getString("String_Server"));
	private ParameterTextField parameterTextFieldPort = new ParameterTextField(ProcessProperties.getString("String_port"));

	private ParameterTextField parameterTextFieldUserName = new ParameterTextField();
	private ParameterPassword parameterTextFieldPassword = new ParameterPassword();

	private ParameterHDFSPath parameterHDFSPath;
	private ParameterComboBox parameterComboBoxAnalyseType = new ParameterComboBox(ProcessProperties.getString("String_AnalyseType"));
	private ParameterComboBox parameterComboBoxMeshType = new ParameterComboBox(ProcessProperties.getString("String_MeshType"));
	ParameterTextField parameterIndex = new ParameterTextField(ProcessProperties.getString("String_Index"));
	private ParameterTextField parameterBounds;
	private ParameterTextField parameterResolution;
	ParameterTextField parameterRadius;

	ParameterTextArea parameterTextAreaOutPut = new ParameterTextArea();


	public MetaProcessKernelDensity() {
		initMetaInfo();
	}

	private void initMetaInfo() {
		//TODO 封装数据管理调用控件，此处先用ParameterTextField控件替换

		parameterTextFieldUserName.setSelectedItem("");
		parameterTextFieldUserName.setDescribe(ProcessProperties.getString("String_UserName"));
		parameterTextFieldPassword.setSelectedItem("");
		parameterTextFieldPassword.setDescribe(ProcessProperties.getString("String_PassWord"));


		parameterHDFSPath = new ParameterHDFSPath();
		parameterHDFSPath.setSelectedItem("hdfs://172.16.14.148:9000/data/newyork_taxi_2013-01_147k.csv");
		parameterComboBoxAnalyseType.setItems(new ParameterDataNode(ProcessProperties.getString("String_SimplePointDensity"), "0"),
				new ParameterDataNode(ProcessProperties.getString("String_KernelDensity"), "1"));

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

		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.setDescribe(ProcessProperties.getString("String_loginInfo"));
		parameterCombine.addParameters(parameterTextFieldAddress, parameterTextFieldPort, parameterTextFieldUserName, parameterTextFieldPassword);

		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_setParameter"));
		parameterCombineSetting.addParameters(
				parameterHDFSPath,
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
				parameterCombine,
				parameterCombineSetting,
				parameterCombineResult
		);
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
	public void run() {
		String username = (String) parameterTextFieldUserName.getSelectedItem();
		String password = (String) parameterTextFieldPassword.getSelectedItem();
		IServerService service = new IServerServiceImpl();
		IServerLoginInfo.ipAddr = (String) parameterTextFieldAddress.getSelectedItem();
		IServerLoginInfo.port = (String) parameterTextFieldPort.getSelectedItem();
		CloseableHttpClient client = service.login(username, password);
		if (null != client) {
			IServerLoginInfo.client = client;
			fireRunning(new RunningEvent(this, 0, "start"));
			//核密度分析功能实现
			KernelDensityJobSetting kenelDensityJobSetting = new KernelDensityJobSetting();
			kenelDensityJobSetting.analyst.method = (String) parameterComboBoxAnalyseType.getSelectedData();
			kenelDensityJobSetting.analyst.meshType = (String) parameterComboBoxMeshType.getSelectedData();
			kenelDensityJobSetting.analyst.fields = (String) parameterIndex.getSelectedItem();
			kenelDensityJobSetting.analyst.query = parameterBounds.getSelectedItem().toString();
			kenelDensityJobSetting.analyst.resolution = parameterResolution.getSelectedItem().toString();
			kenelDensityJobSetting.analyst.radius = parameterRadius.getSelectedItem().toString();
			kenelDensityJobSetting.input.datasetName = parameterHDFSPath.getSelectedItem().toString();

			CursorUtilities.setWaitCursor();
			JobResultResponse response = service.query(kenelDensityJobSetting);
			if (null != response) {
				CursorUtilities.setDefaultCursor();
				ProcessTask task = TaskUtil.getTask(this);
				NewMessageBus messageBus = new NewMessageBus(response, task);
				messageBus.run();
			}
//            ProcessData processData = new ProcessData();
//            processData.setData("Output");
//            outPuts.add(0, processData);
			fireRunning(new RunningEvent(this, 100, "finished"));
			setFinished(true);
			CursorUtilities.setDefaultCursor();
		}
	}

	@Override
	public String getKey() {
		return MetaKeys.KERNEL_DENSITY;
	}

}
