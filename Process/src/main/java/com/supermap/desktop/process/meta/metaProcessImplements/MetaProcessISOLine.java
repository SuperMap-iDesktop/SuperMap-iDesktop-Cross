package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.data.*;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;

/**
 * Created by xie on 2017/3/6.
 */
public class MetaProcessISOLine extends MetaProcess {
	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "ExtractResult";

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
			RunningEvent event = new RunningEvent(MetaProcessISOLine.this, steppedEvent.getPercent(), AbstractParameter.PROPERTY_VALE);
			fireRunning(event);

			if (event.isCancel()) {
				steppedEvent.setCancel(true);
			}
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

		DatasourceConstraint.getInstance().constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(saveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
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
		this.smoothMethod.setItems(selectedNode,
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_BSLine"), SmoothMethod.BSPLINE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_POLISH"), SmoothMethod.POLISH));
		this.smoothMethod.setSelectedItem(selectedNode);
	}

	private void initParameters() {
		this.inputs.addData(INPUT_DATA, DatasetTypes.GRID);
		this.outputs.addData(OUTPUT_DATA, DatasetTypes.LINE);
		this.parameters = new DefaultParameters();
		this.sourceDatasource = new ParameterDatasource();
		this.dataset = new ParameterSingleDataset(DatasetType.GRID);
		this.saveDataset = new ParameterSaveDataset();
		this.maxGrid = new ParameterTextField(CommonProperties.getString("String_MAXGrid"));
		this.minGrid = new ParameterTextField(CommonProperties.getString("String_MINGrid"));
		this.maxISOLine = new ParameterTextField(CommonProperties.getString("String_MAXISOLine"));
		this.minISOLine = new ParameterTextField(CommonProperties.getString("String_MINISOLine"));
		this.isoLine = new ParameterTextField(CommonProperties.getString("String_ISOData"));
		this.datumValue = new ParameterTextField(CommonProperties.getString("String_DatumValue"));
		this.interval = new ParameterTextField(CommonProperties.getString("String_Interval"));
		this.resampleTolerance = new ParameterTextField(CommonProperties.getString("String_ResampleTolerance"));
		this.smoothMethod = new ParameterComboBox().setDescribe(CommonProperties.getString("String_SmoothMethod"));
		this.smoothNess = new ParameterTextField(CommonProperties.getString("String_SmoothNess"));

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, dataset);
		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_TargetData"));
		targetData.addParameters(saveDataset);
		ParameterCombine resultInfo = new ParameterCombine();
		resultInfo.setDescribe(CommonProperties.getString("String_ResultInfo"));
		resultInfo.addParameters(maxGrid, minGrid, maxISOLine, minISOLine, isoLine);
		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(datumValue, interval, resampleTolerance, smoothMethod, smoothNess);
		this.parameters.setParameters(sourceData, targetData, resultInfo, paramSet);
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

		DatasetGrid src = null;
		if (this.inputs.getData(INPUT_DATA).getValue() != null) {
			src = (DatasetGrid) this.inputs.getData(INPUT_DATA).getValue();
		} else {
			src = (DatasetGrid) dataset.getSelectedItem();
		}
		DatasetVector result = SurfaceAnalyst.extractIsoline(surfaceExtractParameter, src, saveDataset.getResultDatasource(), saveDataset.getDatasetName());
		SurfaceAnalyst.removeSteppedListener(this.stepListener);
		this.outputs.getData(OUTPUT_DATA).setValue(result);
		fireRunning(new RunningEvent(MetaProcessISOLine.this, 100, "finished"));
		setFinished(true);
	}

	@Override
	public String getKey() {
		return MetaKeys.ISOLINE;
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Tree_Node3.png");
	}
}
