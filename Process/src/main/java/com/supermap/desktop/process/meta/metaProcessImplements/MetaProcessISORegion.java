package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.data.*;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;

/**
 * Created by xie on 2017/3/7.
 */
public class MetaProcessISORegion extends MetaProcess {
	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "ExtractResult";

	private ParameterDatasource sourceDatasource;
	private ParameterSingleDataset dataset;
	private ParameterSaveDataset targetDataset;
	private ParameterTextField maxGrid;
	private ParameterTextField minGrid;
	private ParameterTextField maxISORegion;
	private ParameterTextField minISORegion;
	private ParameterTextField isoLine;
	private ParameterTextField datumValue;
	private ParameterTextField interval;
	private ParameterTextField resampleTolerance;
	private ParameterComboBox smoothMethod;
	private ParameterTextField smoothNess;
	private SteppedListener stepListener = new SteppedListener() {
		@Override
		public void stepped(SteppedEvent steppedEvent) {
			fireRunning(new RunningEvent(MetaProcessISORegion.this, steppedEvent.getPercent(), AbstractParameter.PROPERTY_VALE));
		}
	};

	public MetaProcessISORegion() {
		this.inputs.addData(INPUT_DATA, DataType.DATASET_GRID);
		this.outputs.addData(OUTPUT_DATA, DataType.DATASET_REGION);
		parameters = new DefaultParameters();
		sourceDatasource = new ParameterDatasource();
		sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		dataset = new ParameterSingleDataset(DatasetType.GRID);
		targetDataset = new ParameterSaveDataset();
		targetDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		targetDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
		targetDataset.setSelectedItem("ISORegion");
		maxGrid = new ParameterTextField(CommonProperties.getString("String_MAXGrid"));
		minGrid = new ParameterTextField(CommonProperties.getString("String_MINGrid"));
		maxISORegion = new ParameterTextField(CommonProperties.getString("String_MAXISORegion"));
		minISORegion = new ParameterTextField(CommonProperties.getString("String_MINISORegion"));
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
		smoothMethod.setItems(selectedNode,
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_BSLine"), SmoothMethod.BSPLINE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_POLISH"), SmoothMethod.POLISH));
		smoothMethod.setSelectedItem(selectedNode);
		smoothNess = new ParameterTextField(CommonProperties.getString("String_SmoothNess"));
		parameters.setParameters(sourceDatasource, dataset, targetDataset, maxGrid, minGrid, maxISORegion, minISORegion, isoLine, datumValue,
				interval, resampleTolerance, smoothMethod, smoothNess);

		DatasourceConstraint.getInstance().constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
	}

	@Override
	public String getTitle() {
		return CommonProperties.getString("String_SurfaceISORegion");
	}

	@Override
	public void run() {
		SurfaceExtractParameter surfaceExtractParameter = new SurfaceExtractParameter();
		surfaceExtractParameter.setDatumValue(Double.valueOf(datumValue.getSelectedItem().toString()));
		surfaceExtractParameter.setInterval(Double.valueOf(interval.getSelectedItem().toString()));
		surfaceExtractParameter.setResampleTolerance(Double.valueOf(resampleTolerance.getSelectedItem().toString()));
		surfaceExtractParameter.setSmoothMethod((SmoothMethod) ((ParameterDataNode) smoothMethod.getSelectedItem()).getData());
		surfaceExtractParameter.setSmoothness(Integer.valueOf(smoothNess.getSelectedItem().toString()));
		DatasetGrid src = null;
		if (this.inputs.getData(INPUT_DATA).getValue() != null) {
			src = (DatasetGrid) this.inputs.getData(INPUT_DATA).getValue();
		} else {
			src = (DatasetGrid) dataset.getSelectedItem();
		}

		SurfaceAnalyst.addSteppedListener(this.stepListener);
		DatasetVector result = SurfaceAnalyst.extractIsoregion(surfaceExtractParameter, src, targetDataset.getResultDatasource(), targetDataset.getDatasetName(), null);
		SurfaceAnalyst.removeSteppedListener(this.stepListener);
		this.outputs.getData(OUTPUT_DATA).setValue(result);
		fireRunning(new RunningEvent(MetaProcessISORegion.this, 100, "finished"));
		setFinished(true);
	}

	@Override
	public String getKey() {
		return MetaKeys.ISOREGION;
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Process/buffer.png");
	}
}
