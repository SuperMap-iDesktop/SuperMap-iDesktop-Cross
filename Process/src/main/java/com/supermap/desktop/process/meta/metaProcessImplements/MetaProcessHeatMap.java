package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.messageBus.NewMessageBus;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterHDFSPath;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.ui.lbs.Interface.IServerService;
import com.supermap.desktop.ui.lbs.impl.IServerServiceImpl;
import com.supermap.desktop.ui.lbs.params.*;
import com.supermap.desktop.utilities.CursorUtilities;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.swing.*;

/**
 * Created by xie on 2017/2/10.
 * 计算热度图
 */
public class MetaProcessHeatMap extends MetaProcess {

    private ParameterHDFSPath parameterHDFSPath;
    private ParameterComboBox parameterCacheType;
    private ParameterTextField parameterBounds;
    private ParameterTextField parameterXYIndex;
    private ParameterTextField parameterCacheLevel;
    private ParameterTextField parameterCacheName;
    private ParameterComboBox parameterDatabaseType;
    private ParameterTextField parameterServiceAddress;
    private ParameterTextField parameterDatabase;
    private ParameterTextField parameterVersion;

    public MetaProcessHeatMap() {
        initMetaInfo();
    }

    private void initMetaInfo() {
        this.parameters = new DefaultParameters();
        parameterHDFSPath = new ParameterHDFSPath();
        parameterHDFSPath.setSelectedItem("hdfs://172.16.14.148:9000/data/newyork_taxi_2013-01_147k.csv");

        parameterCacheType = new ParameterComboBox(ProcessProperties.getString("String_CacheType"));
        ParameterDataNode parameterDataNode1 = new ParameterDataNode("HeatMap", "HeatMap");
        parameterCacheType.setItems(parameterDataNode1);
        parameterCacheType.setSelectedItem(parameterDataNode1);
        //流程图中不支持在地图中绘制范围，范围表示与iServer的表示相同
        parameterBounds = new ParameterTextField().setDescribe(ProcessProperties.getString("String_CacheBounds"));
        parameterBounds.setSelectedItem("-74.050,40.650,-73.850,40.850");
        parameterXYIndex = new ParameterTextField().setDescribe(ProcessProperties.getString("String_XYIndex"));
        parameterXYIndex.setSelectedItem("10,11");
        parameterCacheLevel = new ParameterTextField().setDescribe(ProcessProperties.getString("String_CacheLevel"));
        parameterCacheLevel.setSelectedItem("1");
        parameterCacheName = new ParameterTextField().setDescribe(ProcessProperties.getString("String_CacheName"));
        parameterCacheName.setSelectedItem("test1_heat");
        parameterDatabaseType = new ParameterComboBox(ProcessProperties.getString("String_DatabaseType"));
        ParameterDataNode parameterDataNode = new ParameterDataNode("MongoDB", "MongoDB");
        parameterDatabaseType.setItems(parameterDataNode);
        parameterDatabaseType.setSelectedItem(parameterDataNode);
        parameterServiceAddress = new ParameterTextField().setDescribe(ProcessProperties.getString("String_ServiceAddress"));
        parameterServiceAddress.setSelectedItem("192.168.15.245:27017");
        parameterDatabase = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Database"));
        parameterDatabase.setSelectedItem("test");
        parameterVersion = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Version"));
        parameterVersion.setSelectedItem("V1");
        this.parameters.setParameters(
                parameterHDFSPath,
                parameterCacheType,
                parameterBounds,
                parameterXYIndex,
                parameterCacheLevel,
                parameterCacheName,
                parameterDatabaseType,
                parameterServiceAddress,
                parameterDatabase,
                parameterVersion
        );
        processTask = new ProcessTask(this);
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
    public void run() {
//		JDialogLogin jDialogLogin = new JDialogLogin();
//		if (jDialogLogin.showDialog() != DialogResult.OK) {
//			return;
//		}
        String username = "admin";
        String password = "map123!@#";
        IServerService service = new IServerServiceImpl();
        IServerLoginInfo.ipAddr = "192.168.20.189";
        IServerLoginInfo.port = "8090";
        CloseableHttpClient client = service.login(username, password);
        if (null != client) {
            IServerLoginInfo.client = client;

            fireRunning(new RunningEvent(this, 0, "start"));
            //热度图分析功能
            BuildCacheJobSetting setting = new BuildCacheJobSetting();

            FileInputDataSetting input = new FileInputDataSetting();
            input.filePath = parameterHDFSPath.getSelectedItem().toString();

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
            drawing.xyIndex = parameterXYIndex.getSelectedItem().toString();
            setting.input = input;
            setting.output = output;
            setting.drawing = drawing;
            CursorUtilities.setWaitCursor();
            JobResultResponse response = service.query(setting);
            if (null != response) {
                CursorUtilities.setDefaultCursor();
                NewMessageBus messageBus = new NewMessageBus(response, processTask);
                messageBus.run();
            }
            ProcessData processData = new ProcessData();
            processData.setData("Output");
            outPuts.add(0, processData);
            fireRunning(new RunningEvent(this, 100, "finished"));
        }
    }

    @Override
    public String getKey() {
        return MetaKeys.HEAT_MAP;
    }

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Process/HeatMap.png");
	}
}
