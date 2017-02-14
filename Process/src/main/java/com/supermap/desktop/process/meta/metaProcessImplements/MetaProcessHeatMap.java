package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.lbs.Interface.IServerService;
import com.supermap.desktop.lbs.impl.IServerServiceImpl;
import com.supermap.desktop.lbs.params.*;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.IParameter;
import com.supermap.desktop.process.parameter.IParameters;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.utilities.CursorUtilities;

import javax.swing.*;

/**
 * Created by xie on 2017/2/10.
 * 计算热度图
 */
public class MetaProcessHeatMap extends MetaProcess {
    private IParameters parameters;
    private final int FILE_INPUT_PATH=0;
    private final int CACHE_TYPE =1;
    private final int BOUNDS =2;
    private final int XY_INDEX=3;
    private final int CACHE_LEVEL = 4;
    private final int CACHE_NAME =5;
    private final int DATABASE_TYPE=6;
    private final int SERVICE_ADDRESS = 7;
    private final int DATABASE = 8;
    private final int VERSION =9;
    public MetaProcessHeatMap() {
        initMetaInfo();
    }

    private void initMetaInfo() {
        this.parameters = new DefaultParameters();
        //TODO 封装数据管理调用控件，此处先用ParameterTextField控件替换
        ParameterTextField parameterFileInputPath = new ParameterTextField().setDescribe("");
        parameterFileInputPath.setSelectedItem("hdfs://localhost:9000/data/newyork_taxi_2013-01_147k.csv");
        ParameterComboBox parameterCacheType = new ParameterComboBox().setDescribe(ProcessProperties.getString("String_CacheType")).setItems(new ParameterDataNode[]{new ParameterDataNode("HeatMap", "HeatMap")});
        //流程图中不支持在地图中绘制范围，范围表示与iServer的表示相同
        ParameterTextField parameterBounds = new ParameterTextField().setDescribe(ProcessProperties.getString("String_CacheBounds"));
        parameterBounds.setSelectedItem("-74.050,40.650,-73.850,40.850");
        ParameterTextField parameterXYIndex = new ParameterTextField().setDescribe(ProcessProperties.getString("String_XYIndex"));
        parameterXYIndex.setSelectedItem("10,11");
        ParameterTextField parameterCacheLevel = new ParameterTextField().setDescribe(ProcessProperties.getString("String_CacheLevel"));
        parameterCacheLevel.setSelectedItem("1");
        ParameterTextField parameterCacheName = new ParameterTextField().setDescribe(ProcessProperties.getString("String_CacheName"));
        parameterCacheName.setSelectedItem("test1_heat");
        ParameterComboBox parameterDatabaseType = new ParameterComboBox().setDescribe(ProcessProperties.getString("String_DatabaseType")).setItems(new ParameterDataNode[]{new ParameterDataNode("MongoDB", "MongoDB")});
        ParameterTextField parameterServiceAddress = new ParameterTextField().setDescribe(ProcessProperties.getString("String_ServiceAddress"));
        parameterServiceAddress.setSelectedItem("192.168.15.245:27017");
        ParameterTextField parameterDatabase = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Database"));
        parameterDatabase.setSelectedItem("test");
        ParameterTextField parameterVersion = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Version"));
        parameterVersion.setSelectedItem("V1");
        this.parameters.setParameters(new IParameter[]{
                parameterFileInputPath,
                parameterCacheType,
                parameterBounds,
                parameterXYIndex,
                parameterCacheLevel,
                parameterCacheName,
                parameterDatabaseType,
                parameterServiceAddress,
                parameterDatabase,
                parameterVersion
        });
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_HeatMap");
    }

    @Override
    public JComponent getComponent() {
        return this.parameters.getPanel();
    }

    @Override
    public void run() {
        //热度图分析功能
//        IServerService service = new IServerServiceImpl();
        BuildCacheJobSetting setting = new BuildCacheJobSetting();

        FileInputDataSetting input = new FileInputDataSetting();
        input.filePath = parameters.getParameter(FILE_INPUT_PATH).getSelectedItem().toString();

        MongoDBOutputsetting output = new MongoDBOutputsetting();
        output.cacheName = parameters.getParameter(CACHE_NAME).getSelectedItem().toString();
        output.cacheType =  ((ParameterDataNode)parameters.getParameter(DATABASE_TYPE).getSelectedItem()).getData().toString();
        output.serverAdresses[0] =  parameters.getParameter(SERVICE_ADDRESS).getSelectedItem().toString();
        output.database =  parameters.getParameter(DATABASE).getSelectedItem().toString();
        output.version =  parameters.getParameter(VERSION).getSelectedItem().toString();

        BuildCacheDrawingSetting drawing = new BuildCacheDrawingSetting();
        drawing.imageType =  ((ParameterDataNode)parameters.getParameter(CACHE_TYPE).getSelectedItem()).getData().toString();
        drawing.bounds = parameters.getParameter(BOUNDS).getSelectedItem().toString();;
        drawing.level = parameters.getParameter(CACHE_LEVEL).getSelectedItem().toString();
        drawing.xyIndex = parameters.getParameter(XY_INDEX).getSelectedItem().toString();
        setting.input = input;
        setting.output = output;
        setting.drawing = drawing;
        CursorUtilities.setWaitCursor();
        IServerService service = new IServerServiceImpl();
        JobResultResponse response = service.query(setting);
        if (null != response) {
            CursorUtilities.setDefaultCursor();
//            NewMessageBus.producer(response);
        }
    }
}
