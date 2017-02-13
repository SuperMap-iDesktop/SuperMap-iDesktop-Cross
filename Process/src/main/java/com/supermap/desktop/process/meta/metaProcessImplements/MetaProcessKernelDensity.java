package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.lbs.params.KernelDensityJobSetting;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.IParameter;
import com.supermap.desktop.process.parameter.IParameters;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;

import javax.swing.*;

/**
 * Created by xie on 2017/2/10.
 */
public class MetaProcessKernelDensity extends MetaProcess {
    private IParameters parameters;
    private final int FILE_INPUT_PATH = 0;
    private final int BOUNDS = 1;
    private final int INDEX = 2;
    private final int SEPERATOR = 3;
    private final int RESOLUTION = 4;
    private final int RADIUS = 5;

    public MetaProcessKernelDensity() {
        initMetaInfo();
    }

    private void initMetaInfo() {
        parameters = new DefaultParameters();
        //TODO 封装数据管理调用控件，此处先用ParameterTextField控件替换
        ParameterTextField parameterFileInputPath = new ParameterTextField().setDescribe("");
        parameterFileInputPath.setSelectedItem("hdfs://localhost:9000/data/newyork_taxi_2013-01_147k.csv");
        //流程图中不支持在地图中绘制范围，范围表示与iServer的表示相同
        ParameterTextField parameterBounds = new ParameterTextField().setDescribe("");
        parameterBounds.setSelectedItem("-74.050,40.550,-73.750,40.950");
        ParameterTextField parameterIndex = new ParameterTextField().setDescribe("");
        parameterIndex.setSelectedItem("10");
        ParameterTextField parameterSeperator = new ParameterTextField().setDescribe("");
        parameterSeperator.setSelectedItem(",");
        ParameterTextField parameterResolution = new ParameterTextField().setDescribe("");
        parameterResolution.setSelectedItem("0.004");
        ParameterTextField parameterRadius = new ParameterTextField().setDescribe("");
        parameterRadius.setSelectedItem("0.004");
        parameters.setParameters(new IParameter[]{
                parameterFileInputPath,
                parameterBounds,
                parameterIndex,
                parameterSeperator,
                parameterResolution,
                parameterRadius
        });
    }

    @Override
    public String getTitle() {
        return "核密度分析";
    }

    @Override
    public JComponent getComponent() {
        return this.parameters.getPanel();
    }

    @Override
    public void run() {
        //核密度分析功能实现
        KernelDensityJobSetting kenelDensityJobSetting = new KernelDensityJobSetting();
        kenelDensityJobSetting.analyst.query = parameters.getParameter(BOUNDS).getSelectedItem().toString();
        kenelDensityJobSetting.analyst.geoidx = parameters.getParameter(INDEX).getSelectedItem().toString();
        kenelDensityJobSetting.analyst.separator = parameters.getParameter(SEPERATOR).getSelectedItem().toString();
        kenelDensityJobSetting.analyst.resolution = parameters.getParameter(RESOLUTION).getSelectedItem().toString();
        kenelDensityJobSetting.analyst.radius = parameters.getParameter(RADIUS).getSelectedItem().toString();
        kenelDensityJobSetting.input.filePath = parameters.getParameter(FILE_INPUT_PATH).getSelectedItem().toString();
//        IServerService service = new IServerServiceImpl();
//        CursorUtilities.setWaitCursor();
//        JobResultResponse response = service.query(kenelDensityJobSetting);
//        if (null != response) {
//            CursorUtilities.setDefaultCursor();
//            NewMessageBus.producer(response);
//        }
    }
}
