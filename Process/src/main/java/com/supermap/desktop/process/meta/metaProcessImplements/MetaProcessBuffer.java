package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.BufferAnalyst;
import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import java.util.Vector;

/**
 * @author XiaJT
 */
public class MetaProcessBuffer extends MetaProcess {

	private ParameterComboBox parameterBufferRange;
	private ParameterTextField parameterTextFieldRadius;
	private ParameterTextField parameterTextFieldSemicircleLineSegment;
	private ParameterCheckBox parameterUnionBuffer;
	private ParameterCheckBox parameterRetainAttribute;
	private ParameterSaveDataset parameterSaveDataset;

	private IParameters parameters;
	private SteppedListener steppedListener = new SteppedListener() {
		@Override
		public void stepped(SteppedEvent steppedEvent) {
			fireRunning(new RunningEvent(MetaProcessBuffer.this, steppedEvent.getPercent(), steppedEvent.getMessage()));
		}
	};
	private Vector<ProcessData> processDatas;

	public MetaProcessBuffer() {
		parameters = new DefaultParameters();
		initParameters();
		initComponentState();
		parameters.setParameters(
				parameterBufferRange,
				parameterTextFieldRadius,
				parameterUnionBuffer,
				parameterRetainAttribute,
				parameterTextFieldSemicircleLineSegment,
				parameterSaveDataset
		);

	}

	private void initParameters() {
		ParameterDataNode[] parameterDataNodes = new ParameterDataNode[]{
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Kilometer"), BufferRadiusUnit.KiloMeter),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Meter"), BufferRadiusUnit.Meter),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Decimeter"), BufferRadiusUnit.DeciMeter),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Centimeter"), BufferRadiusUnit.CentiMeter),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Millimeter"), BufferRadiusUnit.MiliMeter),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Foot"), BufferRadiusUnit.Foot),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Inch"), BufferRadiusUnit.Inch),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Mile"), BufferRadiusUnit.Mile),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Yard"), BufferRadiusUnit.Yard),
		};
		parameterBufferRange = new ParameterComboBox(ProcessProperties.getString("Label_BufferRadius"));
		parameterBufferRange.setItems(parameterDataNodes);
		parameterTextFieldRadius = new ParameterTextField(ProcessProperties.getString("Label_Radius"));
		parameterUnionBuffer = new ParameterCheckBox(ProcessProperties.getString("String_UnionBufferItem"));
		parameterRetainAttribute = new ParameterCheckBox(ProcessProperties.getString("String_RetainAttribute"));
		parameterTextFieldSemicircleLineSegment = new ParameterTextField(ProcessProperties.getString("Label_SemicircleLineSegment"));
		parameterSaveDataset = new ParameterSaveDataset();

	}

	private void initComponentState() {
		parameterBufferRange.setSelectedItem(BufferRadiusUnit.Meter);
		parameterTextFieldRadius.setSelectedItem("0");
		parameterTextFieldSemicircleLineSegment.setSelectedItem("50");
		if (Application.getActiveApplication().getActiveDatasources().length > 0) {
			parameterSaveDataset.setResultDatasource(Application.getActiveApplication().getActiveDatasources()[0]);
		} else if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			parameterSaveDataset.setResultDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
		}
		if (parameterSaveDataset.getResultDatasource() != null) {
			parameterSaveDataset.setDatasetName(parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName("dataset"));
		}
	}

	@Override
	public JComponent getComponent() {
		return parameters.getPanel();
	}

	@Override
	public String getTitle() {
		return "缓冲区分析";
	}

	@Override
	public void run() {
		fireRunning(new RunningEvent(this, 0, "start"));
		// fixme 数据集来源
		DatasetVector datasetVector = (DatasetVector) getInputs().getData();

		BufferRadiusUnit radiusUnit = (BufferRadiusUnit) ((ParameterDataNode) parameterBufferRange.getSelectedItem()).getData();
		int radius = Integer.valueOf((String) parameterTextFieldRadius.getSelectedItem());
		boolean isUnion = (boolean) parameterUnionBuffer.getSelectedItem();
		boolean isAttributeRetained = (boolean) parameterRetainAttribute.getSelectedItem();
		int semicircleLineSegment = Integer.valueOf(((String) parameterTextFieldSemicircleLineSegment.getSelectedItem()));
		Datasource resultDatasource = parameterSaveDataset.getResultDatasource();
		String resultName = parameterSaveDataset.getDatasetName();

		DatasetVectorInfo vectorInfo = new DatasetVectorInfo();
		vectorInfo.setName(resultName);
		vectorInfo.setType(DatasetType.REGION);
		DatasetVector result = resultDatasource.getDatasets().create(vectorInfo);
		result.setPrjCoordSys(datasetVector.getPrjCoordSys());

		BufferAnalystParameter parameter = new BufferAnalystParameter();
		parameter.setRadiusUnit(radiusUnit);
		parameter.setLeftDistance(radius);
		parameter.setSemicircleLineSegment(semicircleLineSegment);

		BufferAnalyst.addSteppedListener(this.steppedListener);
		BufferAnalyst.createBuffer(datasetVector, result, parameter, isUnion, isAttributeRetained);
		BufferAnalyst.removeSteppedListener(this.steppedListener);
		ProcessData processData = new ProcessData();
		processData.setData(datasetVector);
		outPuts.add(0, processData);
		fireRunning(new RunningEvent(this, 100, "finished"));
	}

	@Override
	public String getKey() {
		return MetaKeys.BUFFER;
	}
}
