package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.messageBus.NewMessageBus;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterHDFSPath;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.ui.lbs.Interface.IServerService;
import com.supermap.desktop.ui.lbs.impl.IServerServiceImpl;
import com.supermap.desktop.ui.lbs.params.IServerLoginInfo;
import com.supermap.desktop.ui.lbs.params.JobResultResponse;
import com.supermap.desktop.ui.lbs.params.KernelDensityJobSetting;
import com.supermap.desktop.utilities.CursorUtilities;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.swing.*;

/**
 * Created by xie on 2017/2/10.
 */
public class MetaProcessKernelDensity extends MetaProcess {
    ParameterHDFSPath parameterHDFSPath;
    ParameterTextField parameterBounds;
    ParameterTextField parameterIndex;
    ParameterTextField parameterSeperator;
    ParameterTextField parameterResolution;
    ParameterTextField parameterRadius;


    public MetaProcessKernelDensity() {
        initMetaInfo();
    }

    private void initMetaInfo() {
        parameters = new DefaultParameters();
        //TODO 封装数据管理调用控件，此处先用ParameterTextField控件替换
        parameterHDFSPath = new ParameterHDFSPath();
        parameterHDFSPath.setSelectedItem("hdfs://172.16.14.148:9000/data/newyork_taxi_2013-01_147k.csv");
        //流程图中不支持在地图中绘制范围，范围表示与iServer的表示相同
        parameterBounds = new ParameterTextField().setDescribe(ProcessProperties.getString("String_AnalystBounds"));
        parameterBounds.setSelectedItem("-74.050,40.550,-73.750,40.950");
        parameterIndex = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Index"));
        parameterIndex.setSelectedItem("10");
        parameterSeperator = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Seperator"));
        parameterSeperator.setSelectedItem(",");
        parameterResolution = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Resolution"));
        parameterResolution.setSelectedItem("0.004");
        parameterRadius = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Radius"));
        parameterRadius.setSelectedItem("0.004");
        parameters.setParameters(
                parameterHDFSPath,
                parameterBounds,
                parameterIndex,
                parameterSeperator,
                parameterResolution,
                parameterRadius
        );
        processTask = new ProcessTask(this);
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
	    String username = "admin";
	    String password = "map123!@#";
	    IServerService service = new IServerServiceImpl();
	    IServerLoginInfo.ipAddr = "192.168.20.189";
	    IServerLoginInfo.port = "8090";
	    CloseableHttpClient client = service.login(username, password);
	    if (null!=client) {
            IServerLoginInfo.client = client;
            fireRunning(new RunningEvent(this, 0, "start"));
            //核密度分析功能实现
            KernelDensityJobSetting kenelDensityJobSetting = new KernelDensityJobSetting();
            kenelDensityJobSetting.analyst.query = parameterBounds.getSelectedItem().toString();
            kenelDensityJobSetting.analyst.geoidx = parameterIndex.getSelectedItem().toString();
            kenelDensityJobSetting.analyst.separator = parameterSeperator.getSelectedItem().toString();
            kenelDensityJobSetting.analyst.resolution = parameterResolution.getSelectedItem().toString();
            kenelDensityJobSetting.analyst.radius = parameterRadius.getSelectedItem().toString();
            kenelDensityJobSetting.input.filePath = parameterHDFSPath.getSelectedItem().toString();
            CursorUtilities.setWaitCursor();
            JobResultResponse response = service.query(kenelDensityJobSetting);
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
        return MetaKeys.KERNEL_DENSITY;
    }

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Process/KernelDensity.png");
	}
}
