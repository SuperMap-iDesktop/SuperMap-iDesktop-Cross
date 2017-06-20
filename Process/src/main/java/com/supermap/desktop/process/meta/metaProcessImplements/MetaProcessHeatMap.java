package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.messageBus.NewMessageBus;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.BasicTypes;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.process.util.TaskUtil;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.lbs.Interface.IServerService;
import com.supermap.desktop.ui.lbs.impl.IServerServiceImpl;
import com.supermap.desktop.ui.lbs.params.*;
import com.supermap.desktop.utilities.CursorUtilities;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by xie on 2017/2/10.
 * 计算热度图
 */
public class MetaProcessHeatMap extends MetaProcess {
	private ParameterTextField parameterTextFieldUserName = new ParameterTextField();
	private ParameterPassword parameterTextFieldPassword = new ParameterPassword();

	private ParameterHDFSPath parameterHDFSPath;

	private ParameterTextField parameterTextFieldXIndex = new ParameterTextField(ProcessProperties.getString("String_XIndex"));
	private ParameterTextField parameterTextFieldYIndex = new ParameterTextField(ProcessProperties.getString("String_YIndex"));
	private ParameterTextField parameterTextFieldSeparator = new ParameterTextField(ProcessProperties.getString("String_Separator"));

	private ParameterTextField parameterTextFieldAddress = new ParameterTextField(CoreProperties.getString("String_Server"));

	private ParameterTextField parameterTextFieldPort = new ParameterTextField(ProcessProperties.getString("String_port"));

	private ParameterComboBox parameterCacheType;
	private ParameterTextField parameterBounds;
	//	private ParameterTextField parameterXYIndex;
	private ParameterTextField parameterCacheLevel;
	private ParameterTextField parameterCacheName;
	private ParameterComboBox parameterDatabaseType;
	private ParameterTextField parameterServiceAddress;
	private ParameterTextField parameterDatabase;
	private ParameterTextField parameterVersion;

	ParameterTextArea parameterTextAreaOutPut = new ParameterTextArea();

	public MetaProcessHeatMap() {
		initMetaInfo();
	}

	private void initMetaInfo() {
		parameterTextFieldXIndex.setSelectedItem("10");
		parameterTextFieldYIndex.setSelectedItem("11");
		parameterTextFieldSeparator.setSelectedItem(",");
		parameterTextFieldSeparator.setEnabled(false);
		parameterTextFieldAddress.setSelectedItem("192.168.13.161");
		parameterTextFieldPort.setSelectedItem("8090");
		parameterTextFieldUserName.setSelectedItem("admin");
		parameterTextFieldUserName.setDescribe(ProcessProperties.getString("String_UserName"));
		parameterTextFieldPassword.setSelectedItem("iserver123.");
		parameterTextFieldPassword.setDescribe(ProcessProperties.getString("String_PassWord"));

		parameterHDFSPath = new ParameterHDFSPath();
//		parameterHDFSPath.setSelectedItem("newyork14_newyork_taxi_2013-01_14k");
		parameterHDFSPath.setSelectedItem("hdfs://192.168.12.201:9000/data/newyork_taxi_2013-01_14k.csv");

		parameterCacheType = new ParameterComboBox(ProcessProperties.getString("String_CacheType"));
		ParameterDataNode parameterDataNode1 = new ParameterDataNode("heatmap", "heatmap");
		parameterCacheType.setItems(parameterDataNode1);
		parameterCacheType.setSelectedItem(parameterDataNode1);
		//流程图中不支持在地图中绘制范围，范围表示与iServer的表示相同
		parameterBounds = new ParameterTextField().setDescribe(ProcessProperties.getString("String_CacheBounds"));
		parameterBounds.setSelectedItem("-74.050,40.650,-73.850,40.850");
//		parameterXYIndex = new ParameterTextField().setDescribe(ProcessProperties.getString("String_XYIndex"));
//		parameterXYIndex.setSelectedItem("10,11");
		parameterCacheLevel = new ParameterTextField().setDescribe(ProcessProperties.getString("String_CacheLevel"));
		parameterCacheLevel.setSelectedItem("1");
		parameterCacheName = new ParameterTextField().setDescribe(ProcessProperties.getString("String_CacheName"));
		parameterCacheName.setSelectedItem("test1_heat");
		parameterDatabaseType = new ParameterComboBox(ProcessProperties.getString("String_DatabaseType"));
		ParameterDataNode parameterDataNode = new ParameterDataNode("MongoDB", "MongoDB");
		parameterDatabaseType.setItems(parameterDataNode);
		parameterDatabaseType.setSelectedItem(parameterDataNode);
		parameterServiceAddress = new ParameterTextField().setDescribe(ProcessProperties.getString("String_ServiceAddress"));
		parameterServiceAddress.setSelectedItem("192.168.13.161:27017");
		parameterDatabase = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Database"));
		parameterDatabase.setSelectedItem("test");
		parameterVersion = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Version"));
		parameterVersion.setSelectedItem("V1");

		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.setDescribe(ProcessProperties.getString("String_loginInfo"));
		parameterCombine.addParameters(parameterTextFieldAddress, parameterTextFieldPort, parameterTextFieldUserName, parameterTextFieldPassword);

		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_setParameter"));
		parameterCombineSetting.addParameters(parameterHDFSPath,
				parameterCacheType,
				parameterBounds,
				parameterTextFieldXIndex,
				parameterTextFieldYIndex,
				parameterTextFieldSeparator,
//				parameterXYIndex,
				parameterCacheLevel,
				parameterCacheName,
				parameterDatabaseType,
				parameterServiceAddress,
				parameterDatabase,
				parameterVersion);

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.setDescribe(ProcessProperties.getString("String_result"));
		parameterCombineResult.addParameters(parameterTextAreaOutPut);
		parameters.setParameters(
				parameterCombine,
				parameterCombineSetting
//				, parameterCombineResult
		);
		parameters.getOutputs().addData("HeatMapResult", BasicTypes.STRING);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_HeatMap");
	}

	@Override
	public IParameterPanel getComponent() {
		return this.parameters.getPanel();
	}

	@Override
	public boolean execute() {
//		JDialogLogin jDialogLogin = new JDialogLogin();
//		if (jDialogLogin.showDialog() != DialogResult.OK) {
//			return;
//		}
		String username = (String) parameterTextFieldUserName.getSelectedItem();
		String password = (String) parameterTextFieldPassword.getSelectedItem();
		IServerService service = new IServerServiceImpl();
		IServerLoginInfo.ipAddr = (String) parameterTextFieldAddress.getSelectedItem();
		IServerLoginInfo.port = (String) parameterTextFieldPort.getSelectedItem();
		CloseableHttpClient client = service.login(username, password);
		if (null != client) {
			IServerLoginInfo.client = client;

			fireRunning(new RunningEvent(this, 0, "start"));
			//热度图分析功能
			BuildCacheJobSetting setting = new BuildCacheJobSetting();

			FileInputDataSetting input = new FileInputDataSetting();
			input.filePath = parameterHDFSPath.getSelectedItem().toString();
			input.xIndex = parameterTextFieldXIndex.getSelectedItem().toString();
			input.yIndex = parameterTextFieldYIndex.getSelectedItem().toString();
			input.separator = parameterTextFieldSeparator.getSelectedItem().toString();

			MongoDBOutputsetting output = new MongoDBOutputsetting();
			output.cacheName = parameterCacheName.getSelectedItem().toString();
			output.cacheType = ((ParameterDataNode) parameterDatabaseType.getSelectedItem()).getData().toString();
			output.serverAdresses[0] = parameterServiceAddress.getSelectedItem().toString();
			output.database = parameterDatabase.getSelectedItem().toString();
			output.version = parameterVersion.getSelectedItem().toString();

			BuildCacheDrawingSetting drawing = new BuildCacheDrawingSetting();
			drawing.imageType = ((ParameterDataNode) parameterCacheType.getSelectedItem()).getData().toString();
			drawing.bounds = parameterBounds.getSelectedItem().toString();

			drawing.level = parameterCacheLevel.getSelectedItem().toString();
//			drawing.xyIndex = parameterXYIndex.getSelectedItem().toString();
			setting.input = input;
			setting.output = output;
			setting.drawing = drawing;
			CursorUtilities.setWaitCursor();
			JobResultResponse response = service.query(setting);
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
			parameters.getOutputs().getData("HeatMapResult").setValue("");// // TODO: 2017/5/26
			CursorUtilities.setDefaultCursor();
		}
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.HEAT_MAP;
	}

}
