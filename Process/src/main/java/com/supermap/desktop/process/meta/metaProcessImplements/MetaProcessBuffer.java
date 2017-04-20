package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.BufferAnalyst;
import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.util.EnumParser;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class MetaProcessBuffer extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = "SourceDataset";
	private final static String OUTPUT_DATASET = "BufferResult";

	private ParameterDatasource datasource;
	private ParameterSingleDataset dataset;
	private ParameterEnum parameterBufferRange;
	private ParameterTextField parameterTextFieldRadius;
	private ParameterTextField parameterTextFieldSemicircleLineSegment;
	private ParameterCheckBox parameterUnionBuffer;
	private ParameterCheckBox parameterRetainAttribute;
	private ParameterSaveDataset parameterSaveDataset;

	private SteppedListener steppedListener = new SteppedListener() {
		@Override
		public void stepped(SteppedEvent steppedEvent) {
			RunningEvent event = new RunningEvent(MetaProcessBuffer.this, steppedEvent.getPercent(), steppedEvent.getMessage());
			fireRunning(event);

			if (event.isCancel()) {
				steppedEvent.setCancel(true);
			}
		}
	};
//    private Vector<ProcessData> processDatas;

	public MetaProcessBuffer() {
		parameters = new DefaultParameters();
		initParameters();
		initComponentState();
		initParameterConstraint();

	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParameters() {
		this.inputs.addData(INPUT_SOURCE_DATASET, DatasetTypes.SIMPLE_VECTOR);
		this.outputs.addData(OUTPUT_DATASET, DatasetTypes.SIMPLE_VECTOR);
		String[] parameterDataNodes = new String[]{CommonProperties.getString("String_DistanceUnit_Kilometer"),
				CommonProperties.getString("String_DistanceUnit_Meter"),
				CommonProperties.getString("String_DistanceUnit_Decimeter"),
				CommonProperties.getString("String_DistanceUnit_Centimeter"),
				CommonProperties.getString("String_DistanceUnit_Millimeter"),
				CommonProperties.getString("String_DistanceUnit_Foot"),
				CommonProperties.getString("String_DistanceUnit_Inch"),
				CommonProperties.getString("String_DistanceUnit_Mile"),
				CommonProperties.getString("String_DistanceUnit_Yard"),
		};
		String[] values = new String[]{"KiloMeter", "Meter", "DeciMeter", "CentiMeter", "MiliMeter", "Foot", "Inch", "Mile", "Yard"};
		datasource = new ParameterDatasource();
		dataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		parameterBufferRange = new ParameterEnum(new EnumParser(BufferRadiusUnit.class, values, parameterDataNodes)).setDescribe(ProcessProperties.getString("Label_BufferRadius"));
		parameterTextFieldRadius = new ParameterTextField(ProcessProperties.getString("Label_Radius"));
		parameterUnionBuffer = new ParameterCheckBox(ProcessProperties.getString("String_UnionBufferItem"));
		parameterRetainAttribute = new ParameterCheckBox(ProcessProperties.getString("String_RetainAttribute"));
		parameterTextFieldSemicircleLineSegment = new ParameterTextField(ProcessProperties.getString("Label_SemicircleLineSegment"));
		parameterSaveDataset = new ParameterSaveDataset();
		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(datasource, dataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(parameterBufferRange, parameterTextFieldRadius, parameterUnionBuffer
				, parameterRetainAttribute, parameterTextFieldSemicircleLineSegment);

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.addParameters(parameterSaveDataset);
		parameterCombineResult.setDescribe(CommonProperties.getString("String_ResultSet"));

		parameters.setParameters(
				parameterCombineSourceData,
				parameterCombineParameter,
				parameterCombineResult
		);
	}

	private void initComponentState() {
		parameterBufferRange.setSelectedItem(BufferRadiusUnit.Meter);
		parameterTextFieldRadius.setSelectedItem("10");
		parameterTextFieldSemicircleLineSegment.setSelectedItem("50");
		if (Application.getActiveApplication().getActiveDatasources().length > 0) {
			parameterSaveDataset.setResultDatasource(Application.getActiveApplication().getActiveDatasources()[0]);
		} else if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			parameterSaveDataset.setResultDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
		}
//		if (parameterSaveDataset.getResultDatasource() != null) {
		parameterSaveDataset.setDatasetName("RoadBuffer");
//		}
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_BufferAnalyse");
	}

	@Override
	public void run() {
		fireRunning(new RunningEvent(this, 0, "start"));
		// fixme 数据集来源
		DatasetVector datasetVector = null;
		if (this.inputs.getData(INPUT_SOURCE_DATASET) != null
				&& this.inputs.getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
			datasetVector = (DatasetVector) this.inputs.getData(INPUT_SOURCE_DATASET).getValue();
		} else {
			datasetVector = (DatasetVector) dataset.getSelectedItem();
		}

		BufferRadiusUnit radiusUnit = (BufferRadiusUnit) parameterBufferRange.getSelectedItem();
		int radius = Integer.valueOf((String) parameterTextFieldRadius.getSelectedItem());
		boolean isUnion = "true".equalsIgnoreCase((String) parameterUnionBuffer.getSelectedItem());
		boolean isAttributeRetained = "true".equalsIgnoreCase((String) parameterRetainAttribute.getSelectedItem());
		int semicircleLineSegment = Integer.valueOf(((String) parameterTextFieldSemicircleLineSegment.getSelectedItem()));
		Datasource resultDatasource = parameterSaveDataset.getResultDatasource();
		String resultName = parameterSaveDataset.getDatasetName();

		DatasetVectorInfo vectorInfo = new DatasetVectorInfo();
		vectorInfo.setName(resultDatasource.getDatasets().getAvailableDatasetName(resultName));
		vectorInfo.setType(DatasetType.REGION);
		DatasetVector result = resultDatasource.getDatasets().create(vectorInfo);
		result.setPrjCoordSys(datasetVector.getPrjCoordSys());

		BufferAnalystParameter parameter = new BufferAnalystParameter();
		parameter.setRadiusUnit(radiusUnit);
		parameter.setLeftDistance(radius);
		parameter.setRightDistance(radius);
		parameter.setSemicircleLineSegment(semicircleLineSegment);

		BufferAnalyst.addSteppedListener(this.steppedListener);
		BufferAnalyst.createBuffer(datasetVector, result, parameter, isUnion, isAttributeRetained);
		BufferAnalyst.removeSteppedListener(this.steppedListener);

		this.outputs.getData(OUTPUT_DATASET).setValue(result);

		fireRunning(new RunningEvent(this, 100, "finished"));
		setFinished(true);
	}

	@Override
	public String getKey() {
		return MetaKeys.BUFFER;
	}

	public static void main(String[] args) {
		new MetaProcessBuffer();
	}

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Tree_Node3.png");
	}
}
