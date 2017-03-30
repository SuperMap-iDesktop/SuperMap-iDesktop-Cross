package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created by xie on 2017/3/6.
 */
public class MetaProcessISOLine extends MetaProcess {

    private ParameterDatasource sourceDatasource;
    private ParameterSingleDataset dataset;

	private ParameterSaveDataset saveDataset;
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
        initParameters();
	    initParameterConstraint();
	    initParametersState();
    }

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint datasourceConstraint = new DatasourceConstraint();
		datasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
	    this.saveDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
	    this.saveDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
	    this.saveDataset.setSelectedItem("ISOLine");
	    if (null != dataset.getSelectedItem() && dataset.getSelectedItem() instanceof DatasetGrid) {
            maxGrid.setSelectedItem(((DatasetGrid) dataset.getSelectedItem()).getMaxValue());
            minGrid.setSelectedItem(((DatasetGrid) dataset.getSelectedItem()).getMinValue());
        }
        ParameterDataNode selectedNode = new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_NONE"), SmoothMethod.NONE);
        this.smoothMethod.setItems(new ParameterDataNode[]{selectedNode,
                new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_BSLine"), SmoothMethod.BSPLINE),
                new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_POLISH"), SmoothMethod.POLISH)});
        this.smoothMethod.setSelectedItem(selectedNode);
    }

    private void initParameters() {
        this.parameters = new DefaultParameters();
        this.sourceDatasource = new ParameterDatasource();
        this.dataset = new ParameterSingleDataset(new DatasetType[]{DatasetType.GRID});
	    this.saveDataset = new ParameterSaveDataset();
	    this.maxGrid = new ParameterTextField(CommonProperties.getString("String_MAXGrid"));
        this.minGrid = new ParameterTextField(CommonProperties.getString("String_MINGrid"));
        this.maxISOLine = new ParameterTextField(CommonProperties.getString("String_MAXISOLine"));
        this.minISOLine = new ParameterTextField(CommonProperties.getString("String_MINISOLine"));
        this.isoLine = new ParameterTextField(CommonProperties.getString("String_ISOLine"));
        this.datumValue = new ParameterTextField(CommonProperties.getString("String_DatumValue"));
        this.interval = new ParameterTextField(CommonProperties.getString("String_Interval"));
        this.resampleTolerance = new ParameterTextField(CommonProperties.getString("String_ResampleTolerance"));
        this.smoothMethod = new ParameterComboBox().setDescribe(CommonProperties.getString("String_SmoothMethod"));
        this.smoothNess = new ParameterTextField(CommonProperties.getString("String_SmoothNess"));
	    this.parameters.setParameters(sourceDatasource, dataset, saveDataset, maxGrid, minGrid, maxISOLine, minISOLine, isoLine, datumValue,
			    interval, resampleTolerance, smoothMethod, smoothNess);
        this.processTask = new ProcessTask(this);
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
	    SurfaceAnalyst.extractIsoline(surfaceExtractParameter, (DatasetGrid) dataset.getSelectedItem(), saveDataset.getResultDatasource(), saveDataset.getDatasetName());
	    SurfaceAnalyst.removeSteppedListener(this.stepListener);
        fireRunning(new RunningEvent(MetaProcessISOLine.this, 100, "finished"));
    }

    @Override
    public String getKey() {
        return MetaKeys.ISOLINE;
    }

    @Override
    public IParameterPanel getComponent() {
        return parameters.getPanel();
    }
}
