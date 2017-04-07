package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.analyst.spatialanalyst.TerrainInterpolateType;
import com.supermap.data.*;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.FieldTypeUtilities;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by xie on 2017/3/10.
 */
public class MetaProcessISOPoint extends MetaProcess {
	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "ExtractResult";

	private ParameterDatasource sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterComboBox fields;
	private ParameterSaveDataset targetDataset;
	private ParameterTextField maxISOLine;
	private ParameterTextField minISOLine;
	private ParameterTextField isoLine;
	private ParameterComboBox terrainInterpolateType;
	private ParameterTextField resolution;
	private ParameterTextField datumValue;
	private ParameterTextField interval;
	private ParameterTextField resampleTolerance;
	private ParameterComboBox smoothMethod;
	private ParameterTextField smoothNess;
	private SteppedListener stepListener = new SteppedListener() {
		@Override
		public void stepped(SteppedEvent steppedEvent) {
			fireRunning(new RunningEvent(MetaProcessISOPoint.this, steppedEvent.getPercent(), AbstractParameter.PROPERTY_VALE));
		}
	};

	public MetaProcessISOPoint() {
		initParameters();
		initParameterConstraint();
		initParametersState();
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		if (null != sourceDataset.getSelectedItem()) {
			FieldInfos fieldInfos = ((DatasetVector) sourceDataset.getSelectedItem()).getFieldInfos();
			int fieldCount = fieldInfos.getCount();
			ArrayList<ParameterDataNode> nodes = new ArrayList<>();
			for (int i = 0; i < fieldCount; i++) {
				FieldInfo fieldInfo = fieldInfos.get(i);
				if (FieldTypeUtilities.isNumber(fieldInfo.getType())) {
					nodes.add(new ParameterDataNode(fieldInfo.getName(), fieldInfo.getName()));
				}
			}
			this.fields.setItems(nodes.toArray(new ParameterDataNode[nodes.size()]));
		}
		this.targetDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.targetDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
		this.targetDataset.setSelectedItem("ISOLine");
		ParameterDataNode selectedInterpolateType = new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_IDW"), TerrainInterpolateType.IDW);
		this.terrainInterpolateType.setItems(selectedInterpolateType,
				new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_Kriging"), TerrainInterpolateType.KRIGING),
				new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_TIN"), TerrainInterpolateType.TIN));
		ParameterDataNode selectedSmoothNode = new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_NONE"), SmoothMethod.NONE);
		this.smoothMethod.setItems(selectedSmoothNode,
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_BSLine"), SmoothMethod.BSPLINE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_POLISH"), SmoothMethod.POLISH));
		this.smoothMethod.setSelectedItem(selectedSmoothNode);
	}

	private void initParameters() {
		this.inputs.addData(INPUT_DATA, DataType.DATASET_POINT);
		this.outputs.addData(OUTPUT_DATA, DataType.DATASET_LINE);
		this.parameters = new DefaultParameters();
		this.sourceDatasource = new ParameterDatasource();
		this.sourceDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.POINT3D);
		this.fields = new ParameterComboBox(CommonProperties.getString("String_FieldsName"));
		this.targetDataset = new ParameterSaveDataset();
		this.maxISOLine = new ParameterTextField(CommonProperties.getString("String_MAXISOLine"));
		this.minISOLine = new ParameterTextField(CommonProperties.getString("String_MINISOLine"));
		this.isoLine = new ParameterTextField(CommonProperties.getString("String_ISOLine"));
		this.terrainInterpolateType = new ParameterComboBox(CommonProperties.getString("String_InterpolateType"));
		this.resolution = new ParameterTextField(ProcessProperties.getString("String_Resolution"));
		this.datumValue = new ParameterTextField(CommonProperties.getString("String_DatumValue"));
		this.interval = new ParameterTextField(CommonProperties.getString("String_Interval"));
		this.resampleTolerance = new ParameterTextField(CommonProperties.getString("String_ResampleTolerance"));
		this.smoothMethod = new ParameterComboBox().setDescribe(CommonProperties.getString("String_SmoothMethod"));
		this.smoothNess = new ParameterTextField(CommonProperties.getString("String_SmoothNess"));
		this.parameters.setParameters(sourceDatasource, sourceDataset, fields, targetDataset,
				maxISOLine, minISOLine, isoLine, terrainInterpolateType, resolution, datumValue, interval,
				resampleTolerance, smoothMethod, smoothNess);
	}

	@Override
	public String getTitle() {
		return CommonProperties.getString("String_SurfaceISOPoint");
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

		DatasetVector src = null;
		if (this.inputs.getData(INPUT_DATA).getValue() != null) {
			src = (DatasetVector) this.inputs.getData(INPUT_DATA).getValue();
		} else {
			src = (DatasetVector) sourceDataset.getSelectedItem();
		}
		SurfaceAnalyst.extractIsoline(surfaceExtractParameter, src, ((ParameterDataNode) fields.getSelectedItem()).getDescribe(), ((TerrainInterpolateType) ((ParameterDataNode) terrainInterpolateType.getSelectedItem()).getData()), (Double) resolution.getSelectedItem(), null);
		SurfaceAnalyst.removeSteppedListener(this.stepListener);
//		this.outputs.getData(OUTPUT_DATA).setValue();
		fireRunning(new RunningEvent(MetaProcessISOPoint.this, 100, "finished"));
		setFinished(true);
	}

	@Override
	public String getKey() {
		return MetaKeys.ISOPOINT;
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
