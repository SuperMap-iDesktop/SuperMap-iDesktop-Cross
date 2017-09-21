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
		DatasourceConstraint.getInstance().constrained(this.parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(this.dataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(this.comboBoxFieldLeft, ParameterFieldComboBox.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(this.comboBoxFieldRight, ParameterFieldComboBox.DATASET_FIELD_NAME);
	}

	private void initParameters() {
		this.parameterBufferRange = new ParameterComboBox(ProcessProperties.getString("Label_BufferRadius"));
		this.parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Kilometer"), BufferRadiusUnit.KiloMeter));
		this.parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Meter"), BufferRadiusUnit.Meter));
		this.parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Decimeter"), BufferRadiusUnit.DeciMeter));
		this.parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Centimeter"), BufferRadiusUnit.CentiMeter));
		this.parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Millimeter"), BufferRadiusUnit.MiliMeter));
		this.parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Foot"), BufferRadiusUnit.Foot));
		this.parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Inch"), BufferRadiusUnit.Inch));
		this.parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Mile"), BufferRadiusUnit.Mile));
		this.parameterBufferRange.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Yard"), BufferRadiusUnit.Yard));

		this.datasource = new ParameterDatasourceConstrained();
		this.dataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.NETWORK);
		this.datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));

		this.radioButtonFlatOrRound = new ParameterRadioButton();
		ParameterDataNode gound = new ParameterDataNode(ProcessProperties.getString("String_CheckBox_BufferRound"), BUFFER_ROUND);
		ParameterDataNode flat = new ParameterDataNode(ProcessProperties.getString("String_CheckBox_BufferFlat"), BUFFER_FLAT);
		this.radioButtonFlatOrRound.setItems(new ParameterDataNode[]{gound, flat});

		this.checkBoxBufferLeft = new ParameterCheckBox(ProcessProperties.getString("String_CheckBox_Left"));
		this.checkBoxBufferRight = new ParameterCheckBox(ProcessProperties.getString("String_CheckBox_Right"));

		this.radioButtonNumOrField = new ParameterRadioButton();
		ParameterDataNode num = new ParameterDataNode(ProcessProperties.getString("String_Value_Rely"), VALUE_RELY);
		ParameterDataNode field = new ParameterDataNode(ProcessProperties.getString("String_Field_Rely"), FIELD_RELY);
		this.radioButtonNumOrField.setItems(new ParameterDataNode[]{num, field});

		this.parameterTextFieldLeftRadius = new ParameterTextField(ProcessProperties.getString("String_leftRadius"));
		this.parameterTextFieldRightRadius = new ParameterTextField(ProcessProperties.getString("String_rightRadius"));
		this.comboBoxFieldLeft = new ParameterFieldComboBox(ProcessProperties.getString("String_leftRadius"));
		this.comboBoxFieldLeft.setShowNullValue(true);
		this.comboBoxFieldRight = new ParameterFieldComboBox(ProcessProperties.getString("String_rightRadius"));
		this.comboBoxFieldRight.setShowNullValue(true);
		this.parameterUnionBuffer = new ParameterCheckBox(ProcessProperties.getString("String_UnionBufferItem"));
		this.parameterRetainAttribute = new ParameterCheckBox(ProcessProperties.getString("String_RetainAttribute"));
		this.parameterTextFieldSemicircleLineSegment = new ParameterNumber(ProcessProperties.getString("Label_SemicircleLineSegment"));
		this.parameterTextFieldSemicircleLineSegment.setMaxBit(0);
		this.parameterTextFieldSemicircleLineSegment.setMinValue(4);
		this.parameterTextFieldSemicircleLineSegment.setMaxValue(200);
		// 设置是否为必要参数-yuanR
		//ParameterLabel labelSplit = new ParameterLabel();
		//labelSplit.setDescribe("-----------------------------------------------------");

		this.parameterSaveDataset = new ParameterSaveDataset();
		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(this.datasource, this.dataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		ParameterCombine parameterCombineBufferType = new ParameterCombine(ParameterCombine.HORIZONTAL);
		parameterCombineBufferType.addParameters(this.radioButtonFlatOrRound, this.checkBoxBufferLeft, this.checkBoxBufferRight);

		ParameterCombine parameterCombineBufferRadio = new ParameterCombine();
		parameterCombineBufferRadio.addParameters(this.parameterBufferRange,
				parameterCombineBufferType,
				this.radioButtonNumOrField,
				this.parameterTextFieldLeftRadius, this.parameterTextFieldRightRadius,
				this.comboBoxFieldLeft, this.comboBoxFieldRight);
		parameterCombineBufferRadio.setDescribe(ControlsProperties.getString("String_BufferRadius"));

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(new ParameterCombine().addParameters(this.parameterUnionBuffer, this.parameterRetainAttribute),
				this.parameterTextFieldSemicircleLineSegment);

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.addParameters(this.parameterSaveDataset);
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
		Boolean isNotNullDataset = this.dataset.getSelectedDataset() != null;
		Boolean isLineType = false;
		if (isNotNullDataset) {
			isLineType = this.dataset.getSelectedDataset().getType().equals(DatasetType.LINE) || this.dataset.getSelectedDataset().getType().equals(DatasetType.NETWORK);
		}
		Boolean isBufferFlat = ((ParameterDataNode) this.radioButtonFlatOrRound.getSelectedItem()).getData().equals(BUFFER_FLAT);
		Boolean isValueRely = ((ParameterDataNode) this.radioButtonNumOrField.getSelectedItem()).getData().equals(VALUE_RELY);

		this.radioButtonFlatOrRound.setEnabled(isNotNullDataset && isLineType);
		this.checkBoxBufferLeft.setEnabled(isNotNullDataset && isBufferFlat && this.radioButtonFlatOrRound.isEnabled());
		this.checkBoxBufferRight.setEnabled(isNotNullDataset && isBufferFlat && this.radioButtonFlatOrRound.isEnabled());
		this.radioButtonNumOrField.setEnabled(isNotNullDataset);

		this.parameterTextFieldLeftRadius.setEnabled(
				isNotNullDataset && isValueRely && (!isBufferFlat || Boolean.valueOf(this.checkBoxBufferLeft.getSelectedItem())));
		this.comboBoxFieldLeft.setEnabled(
				isNotNullDataset && !isValueRely && (!isBufferFlat || Boolean.valueOf(this.checkBoxBufferLeft.getSelectedItem())));
		this.parameterTextFieldRightRadius.setEnabled(
				isNotNullDataset && isLineType && isValueRely && isBufferFlat && Boolean.valueOf(this.checkBoxBufferRight.getSelectedItem()));
		this.comboBoxFieldRight.setEnabled(
				isNotNullDataset && isLineType && !isValueRely && isBufferFlat && Boolean.valueOf(this.checkBoxBufferRight.getSelectedItem()));
	}

	private void initComponentState() {
		this.parameterBufferRange.setSelectedItem(BufferRadiusUnit.Meter);
		this.parameterTextFieldLeftRadius.setSelectedItem("10");
		this.parameterTextFieldRightRadius.setSelectedItem("10");
		this.parameterTextFieldSemicircleLineSegment.setSelectedItem("100");
		this.parameterBufferRange.setRequisite(true);
		this.parameterTextFieldLeftRadius.setRequisite(true);
		this.parameterTextFieldSemicircleLineSegment.setRequisite(true);

		this.radioButtonFlatOrRound.setSelectedItem(this.radioButtonFlatOrRound.getItemAt(0));
		this.checkBoxBufferLeft.setSelectedItem(true);
		this.checkBoxBufferRight.setSelectedItem(true);
		this.radioButtonNumOrField.setSelectedItem(this.radioButtonNumOrField.getItemAt(0));
		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		if (datasetVector != null) {
			this.datasource.setSelectedItem(datasetVector.getDatasource());
			this.dataset.setSelectedItem(datasetVector);
			this.comboBoxFieldLeft.setFieldName((DatasetVector) datasetVector);
			this.comboBoxFieldRight.setFieldName((DatasetVector) datasetVector);
		}
		this.parameterSaveDataset.setDefaultDatasetName("result_buffer");
		FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
		this.comboBoxFieldLeft.setFieldType(fieldType);
		this.comboBoxFieldRight.setFieldType(fieldType);
		this.parameterRetainAttribute.setSelectedItem(true);
	}

	private void registerListener() {
		this.datasource.addPropertyListener(propertyChangeListener);
		this.dataset.addPropertyListener(propertyChangeListener);
		this.radioButtonFlatOrRound.addPropertyListener(propertyChangeListener);
		this.checkBoxBufferLeft.addPropertyListener(propertyChangeListener);
		this.checkBoxBufferRight.addPropertyListener(propertyChangeListener);
		this.radioButtonNumOrField.addPropertyListener(propertyChangeListener);

		this.parameterUnionBuffer.addPropertyListener(new PropertyChangeListener() {
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
			datasetVector = (DatasetVector) this.dataset.getSelectedItem();
		}

		boolean isUnion = "true".equalsIgnoreCase(this.parameterUnionBuffer.getSelectedItem());
		boolean isAttributeRetained = "true".equalsIgnoreCase(this.parameterRetainAttribute.getSelectedItem());
		int semicircleLineSegment = Integer.valueOf((this.parameterTextFieldSemicircleLineSegment.getSelectedItem()));
		Object radiusLeft = null;
		Object radiusRight = null;
		if (this.parameterTextFieldLeftRadius.isEnabled() && !StringUtilities.isNullOrEmpty(this.parameterTextFieldLeftRadius.getSelectedItem())
				|| (this.comboBoxFieldLeft.isEnabled() && !StringUtilities.isNullOrEmpty((String) this.comboBoxFieldLeft.getSelectedItem()))) {
			radiusLeft = ((ParameterDataNode) this.radioButtonNumOrField.getSelectedItem()).getData().equals(VALUE_RELY) ? Double.valueOf(this.parameterTextFieldLeftRadius.getSelectedItem()) : this.comboBoxFieldLeft.getFieldName();
		}
		if (this.parameterTextFieldRightRadius.isEnabled() && !StringUtilities.isNullOrEmpty(this.parameterTextFieldRightRadius.getSelectedItem())
				|| (this.comboBoxFieldRight.isEnabled() && !StringUtilities.isNullOrEmpty((String) this.comboBoxFieldRight.getSelectedItem()))) {
			radiusRight = ((ParameterDataNode) this.radioButtonNumOrField.getSelectedItem()).getData().equals(VALUE_RELY) ? Double.valueOf(this.parameterTextFieldRightRadius.getSelectedItem()) : this.comboBoxFieldRight.getFieldName();
		}

		if (radiusLeft == null && radiusRight == null) {
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_NullRadius_Error"));
			return false;
		}
		// 当选择的是根据值生成，进行绝对值处理
		if (((ParameterDataNode) this.radioButtonNumOrField.getSelectedItem()).getData().equals(VALUE_RELY) && !datasetVector.getType().equals(DatasetType.REGION)) {
			if (radiusRight != null) {
				radiusRight = Math.abs((Double) radiusRight);
			}
			if (radiusLeft != null) {
				radiusLeft = Math.abs((Double) radiusLeft);
			}
		}

		Datasource resultDatasource = this.parameterSaveDataset.getResultDatasource();
		String resultName = this.parameterSaveDataset.getDatasetName();

		DatasetVectorInfo vectorInfo = new DatasetVectorInfo();
		vectorInfo.setName(resultDatasource.getDatasets().getAvailableDatasetName(resultName));
		vectorInfo.setType(DatasetType.REGION);
		DatasetVector result = resultDatasource.getDatasets().create(vectorInfo);
		result.setPrjCoordSys(datasetVector.getPrjCoordSys());

		BufferAnalystParameter parameter = new BufferAnalystParameter();
		BufferEndType bufferEndType = ((ParameterDataNode) this.radioButtonFlatOrRound.getSelectedItem()).getData().equals(BUFFER_FLAT) ? BufferEndType.FLAT : BufferEndType.ROUND;
		parameter.setEndType(bufferEndType);
		BufferRadiusUnit radiusUnit = (BufferRadiusUnit) this.parameterBufferRange.getSelectedData();
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
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
		} finally {
			BufferAnalyst.removeSteppedListener(this.steppedListener);
			// 如果失败了，删除新建的数据集
			if (!isSuccessful && resultName != null) {
				resultDatasource.getDatasets().delete(resultName);
			}
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.BUFFER;
	}
}
