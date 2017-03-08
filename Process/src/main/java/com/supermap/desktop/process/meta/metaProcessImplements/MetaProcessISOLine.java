package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.data.*;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;

/**
 * Created by xie on 2017/3/6.
 */
public class MetaProcessISOLine extends MetaProcess {
    private IParameters parameters;

    private ParameterDatasource sourceDatasource;
    private ParameterSingleDataset dataset;
    private ParameterDatasource targetDatasource;
    private ParameterTextField datasetName;
    private ParameterTextField maxGrid;
    private ParameterTextField minGrid;
    private ParameterTextField maxISOLine;
    private ParameterTextField minISOLine;
    private ParameterTextField isoLine;
    private ParameterTextField datumValue;
    private ParameterTextField interval;
    private ParameterTextField resampleTolerance;
    private ParameterComboBox smoothMethod;
    private ParameterTextField smoothNess;
    private SteppedListener stepListener = new SteppedListener() {
        @Override
        public void stepped(SteppedEvent steppedEvent) {
            fireRunning(new RunningEvent(MetaProcessISOLine.this, steppedEvent.getPercent(), AbstractParameter.PROPERTY_VALE));
        }
    };

    public MetaProcessISOLine() {
        parameters = new DefaultParameters();
        sourceDatasource = new ParameterDatasource();
        sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
        dataset = new ParameterSingleDataset(new DatasetType[]{DatasetType.GRID});
        targetDatasource = new ParameterDatasource();
        targetDatasource.setDescribe(CommonProperties.getString("String_TargetDatasource"));
        datasetName = new ParameterTextField();
        datasetName.setDescribe(CommonProperties.getString("String_TargetDataset"));
        datasetName.setSelectedItem("ISOLine");
        maxGrid = new ParameterTextField(CommonProperties.getString("String_MAXGrid"));
        minGrid = new ParameterTextField(CommonProperties.getString("String_MINGrid"));
        maxISOLine = new ParameterTextField(CommonProperties.getString("String_MAXISOLine"));
        minISOLine = new ParameterTextField(CommonProperties.getString("String_MINISOLine"));
        isoLine = new ParameterTextField(CommonProperties.getString("String_ISOLine"));
        if (null != dataset.getSelectedItem() && dataset.getSelectedItem() instanceof DatasetGrid) {
            maxGrid.setSelectedItem(((DatasetGrid) dataset.getSelectedItem()).getMaxValue());
            minGrid.setSelectedItem(((DatasetGrid) dataset.getSelectedItem()).getMinValue());
        }
        datumValue = new ParameterTextField(CommonProperties.getString("String_DatumValue"));
        interval = new ParameterTextField(CommonProperties.getString("String_Interval"));
        resampleTolerance = new ParameterTextField(CommonProperties.getString("String_ResampleTolerance"));
        ParameterDataNode selectedNode = new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_NONE"), SmoothMethod.NONE);
        smoothMethod = new ParameterComboBox().setDescribe(CommonProperties.getString("String_SmoothMethod"));
        smoothMethod.setItems(new ParameterDataNode[]{selectedNode,
                new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_BSLine"), SmoothMethod.BSPLINE),
                new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_POLISH"), SmoothMethod.POLISH)});
        smoothMethod.setSelectedItem(selectedNode);
        smoothNess = new ParameterTextField(CommonProperties.getString("String_SmoothNess"));
        parameters.setParameters(sourceDatasource, dataset, targetDatasource, datasetName, maxGrid, minGrid, maxISOLine, minISOLine, isoLine, datumValue,
                interval, resampleTolerance, smoothMethod, smoothNess);
    }

    @Override
    public String getTitle() {
        return CommonProperties.getString("String_SurfaceISOLine");
    }

    @Override
    public void run() {
        SurfaceExtractParameter surfaceExtractParameter = new SurfaceExtractParameter();
        surfaceExtractParameter.setDatumValue(Double.valueOf(datumValue.getSelectedItem().toString()));
        surfaceExtractParameter.setInterval(Double.valueOf(interval.getSelectedItem().toString()));
        surfaceExtractParameter.setResampleTolerance(Double.valueOf(resampleTolerance.getSelectedItem().toString()));
        surfaceExtractParameter.setSmoothMethod((SmoothMethod) ((ParameterDataNode) smoothMethod.getSelectedItem()).getData());
        surfaceExtractParameter.setSmoothness(Integer.valueOf(smoothNess.getSelectedItem().toString()));
        SurfaceAnalyst.addSteppedListener(this.stepListener);
        SurfaceAnalyst.extractIsoline(surfaceExtractParameter, (DatasetGrid) dataset.getSelectedItem(), (Datasource) targetDatasource.getSelectedItem(), datasetName.getSelectedItem().toString());
        SurfaceAnalyst.removeSteppedListener(this.stepListener);
        fireRunning(new RunningEvent(MetaProcessISOLine.this, 100, "finished"));
    }

    @Override
    public String getKey() {
        return MetaKeys.ISOLINE;
    }

    @Override
    public JComponent getComponent() {
        return parameters.getPanel();
    }
}
