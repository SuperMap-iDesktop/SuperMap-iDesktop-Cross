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
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.IParameter;
import com.supermap.desktop.process.parameter.IParameters;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class MetaProcessBuffer extends MetaProcess {

	private IParameters parameters;
	private SteppedListener steppedListener = new SteppedListener() {
		@Override
		public void stepped(SteppedEvent steppedEvent) {
			fireRunning(new RunningEvent(MetaProcessBuffer.this, steppedEvent.getPercent(), steppedEvent.getMessage()));
		}
	};
	public MetaProcessBuffer() {
		parameters = new DefaultParameters();
		ParameterDataNode defaultSelectedNode = new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Meter"), BufferRadiusUnit.Meter);
		ParameterDataNode[] parameterDataNodes = new ParameterDataNode[]{
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Kilometer"), BufferRadiusUnit.KiloMeter),
				defaultSelectedNode,
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Decimeter"), BufferRadiusUnit.DeciMeter),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Centimeter"), BufferRadiusUnit.CentiMeter),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Millimeter"), BufferRadiusUnit.MiliMeter),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Foot"), BufferRadiusUnit.Foot),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Inch"), BufferRadiusUnit.Inch),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Mile"), BufferRadiusUnit.Mile),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Yard"), BufferRadiusUnit.Yard),
		};
		ParameterComboBox parameterBufferRange = new ParameterComboBox().setDescribe(ProcessProperties.getString("Label_BufferRadius")).setItems(parameterDataNodes);
		parameterBufferRange.setSelectedItem(defaultSelectedNode);

		ParameterTextField parameterTextField = new ParameterTextField().setDescribe(ProcessProperties.getString("Label_Radius"));
		parameterTextField.setSelectedItem("0");

		ParameterCheckBox parameterUnionBuffer = new ParameterCheckBox().setDescribe(ProcessProperties.getString("String_UnionBufferItem"));
		ParameterCheckBox parameterRetainAttribute = new ParameterCheckBox().setDescribe(ProcessProperties.getString("String_RetainAttribute"));
		ParameterTextField parameterTextFieldSemicircleLineSegment = new ParameterTextField().setDescribe(ProcessProperties.getString("Label_SemicircleLineSegment"));
		parameterTextFieldSemicircleLineSegment.setSelectedItem("50");

		ParameterSaveDataset parameterSaveDataset = new ParameterSaveDataset();
		if (Application.getActiveApplication().getActiveDatasources().length > 0) {
			parameterSaveDataset.setResultDatasource(Application.getActiveApplication().getActiveDatasources()[0]);
		} else if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			parameterSaveDataset.setResultDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
		}
		if (parameterSaveDataset.getResultDatasource() != null) {
			parameterSaveDataset.setDatasetName(parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName("dataset"));
		}
		parameters.setParameters(new IParameter[]{
				parameterBufferRange,
				parameterTextField,
				parameterUnionBuffer,
				parameterRetainAttribute,
				parameterTextFieldSemicircleLineSegment,
				parameterSaveDataset
		});

	}

	@Override
	public JComponent getComponent() {
		return parameters.getPanel();
	}

	@Override
	public void run() {
		// fixme 数据集来源
		DatasetVector datasetVector = null;

		BufferRadiusUnit radiusUnit = (BufferRadiusUnit) ((ParameterDataNode) parameters.getParameter(0).getSelectedItem()).getData();
		int radius = Integer.valueOf((String) parameters.getParameter(1).getSelectedItem());
		boolean isUnion = (boolean) parameters.getParameter(2).getSelectedItem();
		boolean isAttributeRetained = (boolean) parameters.getParameter(3).getSelectedItem();
		int semicircleLineSegment = Integer.valueOf(((String) parameters.getParameter(4).getSelectedItem()));
		Datasource resultDatasource = ((ParameterSaveDataset) parameters.getParameter(5)).getResultDatasource();
		String resultName = ((ParameterSaveDataset) parameters.getParameter(5)).getDatasetName();

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
	}
}
