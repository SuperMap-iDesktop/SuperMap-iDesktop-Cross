package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.BufferAnalyst;
import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.util.EnumParser;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class MetaProcessBuffer extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = "SourceDataset";
	private final static String OUTPUT_DATASET = "BufferResult";

	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;
	private ParameterEnum parameterBufferRange;
	private ParameterTextField parameterTextFieldLeftRadius;
	private ParameterTextField parameterTextFieldRightRadius;
	private ParameterNumber parameterTextFieldSemicircleLineSegment;
	private ParameterCheckBox parameterUnionBuffer;
	private ParameterCheckBox parameterRetainAttribute;
	private ParameterSaveDataset parameterSaveDataset;


	public MetaProcessBuffer() {
		initParameters();
		initComponentState();
		initParameterConstraint();

	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		parameterUnionBuffer.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterCheckBox.PARAMETER_CHECK_BOX_VALUE)) {
					parameterRetainAttribute.setEnabled(!(boolean) evt.getNewValue());
				}
			}
		});

	}

	private void initParameters() {

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
		datasource = new ParameterDatasourceConstrained();
		dataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		parameterBufferRange = new ParameterEnum(new EnumParser(BufferRadiusUnit.class, values, parameterDataNodes)).setDescribe(ProcessProperties.getString("Label_BufferRadius"));
		parameterTextFieldLeftRadius = new ParameterTextField(ProcessProperties.getString("Label_Radius"));
//		parameterTextFieldRightRadius = new ParameterTextField(ProcessProperties.getString("String_rightRadius"));
		parameterUnionBuffer = new ParameterCheckBox(ProcessProperties.getString("String_UnionBufferItem"));
		parameterRetainAttribute = new ParameterCheckBox(ProcessProperties.getString("String_RetainAttribute"));
		parameterTextFieldSemicircleLineSegment = new ParameterNumber(ProcessProperties.getString("Label_SemicircleLineSegment"));
		parameterTextFieldSemicircleLineSegment.setMaxBit(0);
		parameterTextFieldSemicircleLineSegment.setMinValue(4);
		parameterTextFieldSemicircleLineSegment.setMaxValue(200);
		// 设置是否为必要参数-yuanR
		parameterBufferRange.setRequisite(true);
		parameterTextFieldLeftRadius.setRequisite(true);
		parameterUnionBuffer.setRequisite(true);
		parameterRetainAttribute.setRequisite(true);
		parameterTextFieldSemicircleLineSegment.setRequisite(true);

		parameterSaveDataset = new ParameterSaveDataset();
		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(datasource, dataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(parameterBufferRange, parameterTextFieldLeftRadius, parameterUnionBuffer
				, parameterRetainAttribute, parameterTextFieldSemicircleLineSegment);

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.addParameters(parameterSaveDataset);
		parameterCombineResult.setDescribe(CommonProperties.getString("String_ResultSet"));

		parameters.setParameters(
				parameterCombineSourceData,
				parameterCombineParameter,
				parameterCombineResult
		);

		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.SIMPLE_VECTOR, parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATASET, DatasetTypes.SIMPLE_VECTOR, parameterCombineResult);
	}

	private void initComponentState() {
		parameterBufferRange.setSelectedItem(BufferRadiusUnit.Meter);
		parameterTextFieldLeftRadius.setSelectedItem("10");
		parameterTextFieldSemicircleLineSegment.setSelectedItem("100");
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
		}
//		if (parameterSaveDataset.getResultDatasource() != null) {
		parameterSaveDataset.setDatasetName("result_buffer");
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
	public boolean execute() {
		boolean isSuccessful = false;

		fireRunning(new RunningEvent(this, 0, "start"));
		// fixme 数据集来源
		DatasetVector datasetVector = null;
		if (this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET) != null
				&& this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
			datasetVector = (DatasetVector) this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue();
		} else {
			datasetVector = (DatasetVector) dataset.getSelectedItem();
		}

		BufferRadiusUnit radiusUnit = (BufferRadiusUnit) parameterBufferRange.getSelectedData();
		int radius = Integer.valueOf((String) parameterTextFieldLeftRadius.getSelectedItem());
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
		isSuccessful = BufferAnalyst.createBuffer(datasetVector, result, parameter, isUnion, isAttributeRetained);
		BufferAnalyst.removeSteppedListener(this.steppedListener);

		this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(result);

		fireRunning(new RunningEvent(this, 100, "finished"));
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.BUFFER;
	}

	public static void main(String[] args) {
		new MetaProcessBuffer();
	}

}
