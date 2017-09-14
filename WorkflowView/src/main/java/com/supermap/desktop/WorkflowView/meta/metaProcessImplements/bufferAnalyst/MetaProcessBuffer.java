package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.bufferAnalyst;

import com.supermap.analyst.spatialanalyst.BufferAnalyst;
import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferEndType;
import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 * 重构界面-yuanR20174.9.6
 */
public class MetaProcessBuffer extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATASET = "BufferResult";

	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;
	private ParameterComboBox parameterBufferRange;
	private ParameterRadioButton radioButtonFlatOrRound;
	private ParameterCheckBox checkBoxBufferLeft;//左缓冲
	private ParameterCheckBox checkBoxBufferRight;//右缓冲
	private ParameterRadioButton radioButtonNumOrField;
	private ParameterTextField parameterTextFieldLeftRadius;
	private ParameterTextField parameterTextFieldRightRadius;
	private ParameterFieldComboBox comboBoxFieldLeft;
	private ParameterFieldComboBox comboBoxFieldRight;
	private ParameterNumber parameterTextFieldSemicircleLineSegment;
	private ParameterCheckBox parameterUnionBuffer;
	private ParameterCheckBox parameterRetainAttribute;
	private ParameterSaveDataset parameterSaveDataset;

	private final static String BUFFER_ROUND = ProcessProperties.getString("String_CheckBox_BufferRound");
	private final static String BUFFER_FLAT = ProcessProperties.getString("String_CheckBox_BufferFlat");

	private final static String VALUE_RELY = ProcessProperties.getString("String_Value_Rely");
	private final static String FIELD_RELY = ProcessProperties.getString("String_Field_Rely");


	private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			setComponentEnable();
		}
	};

	public MetaProcessBuffer() {
		initParameters();
		initComponentState();
		initParameterConstraint();
		registerListener();
		setComponentEnable();
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(dataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(comboBoxFieldLeft, ParameterFieldComboBox.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(comboBoxFieldRight, ParameterFieldComboBox.DATASET_FIELD_NAME);
	}

	private void initParameters() {
		parameterBufferRange = new ParameterComboBox(ProcessProperties.getString("Label_BufferRadius"));
		parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Kilometer"), BufferRadiusUnit.KiloMeter));
		parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Meter"), BufferRadiusUnit.Meter));
		parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Decimeter"), BufferRadiusUnit.DeciMeter));
		parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Centimeter"), BufferRadiusUnit.CentiMeter));
		parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Millimeter"), BufferRadiusUnit.MiliMeter));
		parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Foot"), BufferRadiusUnit.Foot));
		parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Inch"), BufferRadiusUnit.Inch));
		parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Mile"), BufferRadiusUnit.Mile));
		parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Yard"), BufferRadiusUnit.Yard));

		datasource = new ParameterDatasourceConstrained();
		dataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.NETWORK);
		datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));

		radioButtonFlatOrRound = new ParameterRadioButton();
		ParameterDataNode gound = new ParameterDataNode(ProcessProperties.getString("String_CheckBox_BufferRound"), BUFFER_ROUND);
		ParameterDataNode flat = new ParameterDataNode(ProcessProperties.getString("String_CheckBox_BufferFlat"), BUFFER_FLAT);
		radioButtonFlatOrRound.setItems(new ParameterDataNode[]{gound, flat});

		checkBoxBufferLeft = new ParameterCheckBox(ProcessProperties.getString("String_CheckBox_Left"));
		checkBoxBufferRight = new ParameterCheckBox(ProcessProperties.getString("String_CheckBox_Right"));

		radioButtonNumOrField = new ParameterRadioButton();
		ParameterDataNode num = new ParameterDataNode(ProcessProperties.getString("String_Value_Rely"), VALUE_RELY);
		ParameterDataNode field = new ParameterDataNode(ProcessProperties.getString("String_Field_Rely"), FIELD_RELY);
		radioButtonNumOrField.setItems(new ParameterDataNode[]{num, field});

		parameterTextFieldLeftRadius = new ParameterTextField(ProcessProperties.getString("String_leftRadius"));
		parameterTextFieldRightRadius = new ParameterTextField(ProcessProperties.getString("String_rightRadius"));
		comboBoxFieldLeft = new ParameterFieldComboBox(ProcessProperties.getString("String_leftRadius"));
		comboBoxFieldRight = new ParameterFieldComboBox(ProcessProperties.getString("String_rightRadius"));
		parameterUnionBuffer = new ParameterCheckBox(ProcessProperties.getString("String_UnionBufferItem"));
		parameterRetainAttribute = new ParameterCheckBox(ProcessProperties.getString("String_RetainAttribute"));
		parameterTextFieldSemicircleLineSegment = new ParameterNumber(ProcessProperties.getString("Label_SemicircleLineSegment"));
		parameterTextFieldSemicircleLineSegment.setMaxBit(0);
		parameterTextFieldSemicircleLineSegment.setMinValue(4);
		parameterTextFieldSemicircleLineSegment.setMaxValue(200);
		// 设置是否为必要参数-yuanR
		//ParameterLabel labelSplit = new ParameterLabel();
		//labelSplit.setDescribe("-----------------------------------------------------");

		parameterSaveDataset = new ParameterSaveDataset();
		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(datasource, dataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		ParameterCombine parameterCombineBufferType = new ParameterCombine(ParameterCombine.HORIZONTAL);
		parameterCombineBufferType.addParameters(radioButtonFlatOrRound, checkBoxBufferLeft, checkBoxBufferRight);

		ParameterCombine parameterCombineBufferRadio = new ParameterCombine();
		parameterCombineBufferRadio.addParameters(parameterBufferRange,
				parameterCombineBufferType,
				radioButtonNumOrField,
				parameterTextFieldLeftRadius, parameterTextFieldRightRadius,
				comboBoxFieldLeft, comboBoxFieldRight);
		parameterCombineBufferRadio.setDescribe(ControlsProperties.getString("String_BufferRadius"));

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(
				parameterUnionBuffer, parameterRetainAttribute, parameterTextFieldSemicircleLineSegment);

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.addParameters(parameterSaveDataset);
		parameterCombineResult.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));

		parameters.setParameters(
				parameterCombineSourceData,
				parameterCombineBufferRadio,
				parameterCombineParameter,
				parameterCombineResult
		);
		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.SIMPLE_VECTOR, parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATASET, ProcessOutputResultProperties.getString("String_BufferResult"), DatasetTypes.REGION, parameterCombineResult);
	}

	private void setComponentEnable() {
		Boolean isNotNullDataset = dataset.getSelectedDataset() != null;
		Boolean isLineType = false;
		if (isNotNullDataset) {
			isLineType = dataset.getSelectedDataset().getType().equals(DatasetType.LINE) || dataset.getSelectedDataset().getType().equals(DatasetType.NETWORK);
		}
		Boolean isBufferFlat = ((ParameterDataNode) radioButtonFlatOrRound.getSelectedItem()).getData().equals(BUFFER_FLAT);
		Boolean isValueRely = ((ParameterDataNode) radioButtonNumOrField.getSelectedItem()).getData().equals(VALUE_RELY);

		radioButtonFlatOrRound.setEnabled(isNotNullDataset && isLineType);
		checkBoxBufferLeft.setEnabled(isNotNullDataset && isBufferFlat && radioButtonFlatOrRound.isEnabled());
		checkBoxBufferRight.setEnabled(isNotNullDataset && isBufferFlat && radioButtonFlatOrRound.isEnabled());
		radioButtonNumOrField.setEnabled(isNotNullDataset);

		parameterTextFieldLeftRadius.setEnabled(
				isNotNullDataset && isValueRely && (!isBufferFlat || Boolean.valueOf(checkBoxBufferLeft.getSelectedItem())));
		comboBoxFieldLeft.setEnabled(
				isNotNullDataset && !isValueRely && (!isBufferFlat || Boolean.valueOf(checkBoxBufferLeft.getSelectedItem())));
		parameterTextFieldRightRadius.setEnabled(
				isNotNullDataset && isLineType && isValueRely && isBufferFlat && Boolean.valueOf(checkBoxBufferRight.getSelectedItem()));
		comboBoxFieldRight.setEnabled(
				isNotNullDataset && isLineType && !isValueRely && isBufferFlat && Boolean.valueOf(checkBoxBufferRight.getSelectedItem()));
	}

	private void initComponentState() {
		parameterBufferRange.setSelectedItem(BufferRadiusUnit.Meter);
		parameterTextFieldLeftRadius.setSelectedItem("10");
		parameterTextFieldRightRadius.setSelectedItem("10");
		parameterTextFieldSemicircleLineSegment.setSelectedItem("100");
		parameterBufferRange.setRequisite(true);
		parameterTextFieldLeftRadius.setRequisite(true);
		parameterTextFieldSemicircleLineSegment.setRequisite(true);

		radioButtonFlatOrRound.setSelectedItem(radioButtonFlatOrRound.getItemAt(0));
		checkBoxBufferLeft.setSelectedItem(true);
		checkBoxBufferRight.setSelectedItem(true);
		radioButtonNumOrField.setSelectedItem(radioButtonNumOrField.getItemAt(0));
		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
			comboBoxFieldLeft.setFieldName((DatasetVector) datasetVector);
			comboBoxFieldRight.setFieldName((DatasetVector) datasetVector);
		}
		parameterSaveDataset.setDefaultDatasetName("result_buffer");
		FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
		comboBoxFieldLeft.setFieldType(fieldType);
		comboBoxFieldRight.setFieldType(fieldType);
		parameterRetainAttribute.setSelectedItem(true);
	}

	private void registerListener() {
		datasource.addPropertyListener(propertyChangeListener);
		dataset.addPropertyListener(propertyChangeListener);
		radioButtonFlatOrRound.addPropertyListener(propertyChangeListener);
		checkBoxBufferLeft.addPropertyListener(propertyChangeListener);
		checkBoxBufferRight.addPropertyListener(propertyChangeListener);
		radioButtonNumOrField.addPropertyListener(propertyChangeListener);

		parameterUnionBuffer.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterCheckBox.PARAMETER_CHECK_BOX_VALUE)) {
					parameterRetainAttribute.setEnabled(!(boolean) evt.getNewValue());
				}
			}
		});
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_BufferAnalyst");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;

		DatasetVector datasetVector;
		if (this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET) != null
				&& this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
			datasetVector = (DatasetVector) this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue();
		} else {
			datasetVector = (DatasetVector) dataset.getSelectedItem();
		}

		boolean isUnion = "true".equalsIgnoreCase(parameterUnionBuffer.getSelectedItem());
		boolean isAttributeRetained = "true".equalsIgnoreCase(parameterRetainAttribute.getSelectedItem());
		int semicircleLineSegment = Integer.valueOf((parameterTextFieldSemicircleLineSegment.getSelectedItem()));
		Object radiusLeft = null;
		Object radiusRight = null;
		if (parameterTextFieldLeftRadius.isEnabled() && !StringUtilities.isNullOrEmpty(parameterTextFieldLeftRadius.getSelectedItem())
				|| (comboBoxFieldLeft.isEnabled() && !StringUtilities.isNullOrEmpty((String) comboBoxFieldLeft.getSelectedItem()))) {
			radiusLeft = ((ParameterDataNode) radioButtonNumOrField.getSelectedItem()).getData().equals(VALUE_RELY) ? Double.valueOf(parameterTextFieldLeftRadius.getSelectedItem()) : comboBoxFieldLeft.getFieldName();
		}
		if (parameterTextFieldRightRadius.isEnabled() && !StringUtilities.isNullOrEmpty(parameterTextFieldRightRadius.getSelectedItem())
				|| (comboBoxFieldRight.isEnabled() && !StringUtilities.isNullOrEmpty((String) comboBoxFieldRight.getSelectedItem()))) {
			radiusRight = ((ParameterDataNode) radioButtonNumOrField.getSelectedItem()).getData().equals(VALUE_RELY) ? Double.valueOf(parameterTextFieldRightRadius.getSelectedItem()) : comboBoxFieldRight.getFieldName();
		}

		if (radiusLeft == null && radiusRight == null) {
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_NullRadius_Error"));
			return false;
		}
		// 当选择的是根据值生成，进行绝对值处理
		if (((ParameterDataNode) radioButtonNumOrField.getSelectedItem()).getData().equals(VALUE_RELY)) {
			if (radiusRight != null) {
				radiusRight = Math.abs((Double) radiusRight);
			}
			if (radiusLeft != null) {
				radiusLeft = Math.abs((Double) radiusLeft);
			}
		}

		Datasource resultDatasource = parameterSaveDataset.getResultDatasource();
		String resultName = parameterSaveDataset.getDatasetName();

		DatasetVectorInfo vectorInfo = new DatasetVectorInfo();
		vectorInfo.setName(resultDatasource.getDatasets().getAvailableDatasetName(resultName));
		vectorInfo.setType(DatasetType.REGION);
		DatasetVector result = resultDatasource.getDatasets().create(vectorInfo);
		result.setPrjCoordSys(datasetVector.getPrjCoordSys());

		BufferAnalystParameter parameter = new BufferAnalystParameter();
		BufferEndType bufferEndType = ((ParameterDataNode) radioButtonFlatOrRound.getSelectedItem()).getData().equals(BUFFER_FLAT) ? BufferEndType.FLAT : BufferEndType.ROUND;
		parameter.setEndType(bufferEndType);
		BufferRadiusUnit radiusUnit = (BufferRadiusUnit) parameterBufferRange.getSelectedData();
		parameter.setRadiusUnit(radiusUnit);
		if (radiusLeft != null) {
			parameter.setLeftDistance(radiusLeft);
		}
		if (radiusRight != null) {
			parameter.setRightDistance(radiusRight);
		}
		parameter.setSemicircleLineSegment(semicircleLineSegment);

		BufferAnalyst.addSteppedListener(this.steppedListener);
		try {
			isSuccessful = BufferAnalyst.createBuffer(datasetVector, result, parameter, isUnion, isAttributeRetained);
			this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(result);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			// 删除新建的数据集
			// 如果失败了，删除新建的数据集
			if (resultDatasource != null && resultName != null) {
				resultDatasource.getDatasets().delete(resultName);
			}
		} finally {
			BufferAnalyst.removeSteppedListener(this.steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.BUFFER;
	}

//	public static void main(String[] args) {
//		new MetaProcessBuffer();
//	}

}
