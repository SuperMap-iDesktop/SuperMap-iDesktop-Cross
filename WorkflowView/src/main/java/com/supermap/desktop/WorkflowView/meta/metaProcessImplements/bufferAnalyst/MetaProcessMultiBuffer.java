package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.bufferAnalyst;

import com.supermap.analyst.spatialanalyst.BufferAnalyst;
import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.MultiBufferRadioList.ParameterMultiBufferRadioList;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yuanR on 2017/8/22 0022.
 * 多重缓冲区分析
 */
public class MetaProcessMultiBuffer extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATASET = "MultiBufferResult";

	private static final int BUFFERTYPE_ROUND = 0;
	private static final int BUFFERTYPE_FLAT = 1;

	// 缓冲数据
	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;
	// 缓冲类型
	private ParameterRadioButton radioButtonFlatOrRound;
	private ParameterComboBox comboBoxBufferLeftOrRight;
	// 缓冲半径列表
	private ParameterMultiBufferRadioList parameterMultiBufferRadioList;
	private ParameterComboBox parameterRadiusUnit;
	// 参数设置
	private ParameterCheckBox parameterUnionBuffer;
	private ParameterCheckBox parameterRingBuffer;
	private ParameterCheckBox parameterRetainAttribute;
	private ParameterNumber parameterTextFieldSemicircleLineSegment;
	// 结果
	private ParameterSaveDataset parameterSaveDataset;

	public MetaProcessMultiBuffer() {
		initParameters();
		initParameterConstraint();
		initComponentState();
		registerListener();
	}

	private void initParameters() {

		// 源数据
		this.datasource = new ParameterDatasourceConstrained();
		this.dataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		this.datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));

		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(this.datasource, this.dataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		// 类型-只对线数据集有效
		this.radioButtonFlatOrRound = new ParameterRadioButton();
		ParameterDataNode round = new ParameterDataNode(ProcessProperties.getString("String_CheckBox_BufferRound"), BUFFERTYPE_ROUND);
		ParameterDataNode flat = new ParameterDataNode(ProcessProperties.getString("String_CheckBox_BufferFlat"), BUFFERTYPE_FLAT);
		this.radioButtonFlatOrRound.setItems(new ParameterDataNode[]{round, flat});
		this.comboBoxBufferLeftOrRight = new ParameterComboBox(ProcessProperties.getString("Label_LineBufferDirection"));
		this.comboBoxBufferLeftOrRight.addItem(new ParameterDataNode(ProcessProperties.getString("String_CheckBox_Left"), true));
		this.comboBoxBufferLeftOrRight.addItem(new ParameterDataNode(ProcessProperties.getString("String_CheckBox_Right"), false));

		ParameterCombine parameterCombineBufferType = new ParameterCombine();
		parameterCombineBufferType.addParameters(this.radioButtonFlatOrRound, this.comboBoxBufferLeftOrRight);
		parameterCombineBufferType.setDescribe(ProcessProperties.getString("String_Title_BufferType"));

		// 半径列表
		this.parameterMultiBufferRadioList = new ParameterMultiBufferRadioList();

		this.parameterRadiusUnit = new ParameterComboBox(ProcessProperties.getString("Label_BufferRadius"));
		this.parameterRadiusUnit.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Kilometer"), BufferRadiusUnit.KiloMeter));
		this.parameterRadiusUnit.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Meter"), BufferRadiusUnit.Meter));
		this.parameterRadiusUnit.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Decimeter"), BufferRadiusUnit.DeciMeter));
		this.parameterRadiusUnit.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Centimeter"), BufferRadiusUnit.CentiMeter));
		this.parameterRadiusUnit.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Millimeter"), BufferRadiusUnit.MiliMeter));
		this.parameterRadiusUnit.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Foot"), BufferRadiusUnit.Foot));
		this.parameterRadiusUnit.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Inch"), BufferRadiusUnit.Inch));
		this.parameterRadiusUnit.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Mile"), BufferRadiusUnit.Mile));
		this.parameterRadiusUnit.addItem(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Yard"), BufferRadiusUnit.Yard));
		this.parameterRadiusUnit.setRequisite(true);

		ParameterCombine parameterCombineRadioList = new ParameterCombine();
		parameterCombineRadioList.setDescribe(ProcessProperties.getString("String_Title_BufferRadioList"));
		parameterCombineRadioList.addParameters(this.parameterMultiBufferRadioList, this.parameterRadiusUnit);

		// 参数
		this.parameterUnionBuffer = new ParameterCheckBox(ProcessProperties.getString("String_UnionBufferItem"));
		this.parameterRingBuffer = new ParameterCheckBox(ProcessProperties.getString("String_CreateRingBuffer"));
		this.parameterRetainAttribute = new ParameterCheckBox(ProcessProperties.getString("String_RetainAttribute"));

		this.parameterTextFieldSemicircleLineSegment = new ParameterNumber(ProcessProperties.getString("Label_SemicircleLineSegment"));
		this.parameterTextFieldSemicircleLineSegment.setMaxBit(0);
		this.parameterTextFieldSemicircleLineSegment.setMinValue(4);
		this.parameterTextFieldSemicircleLineSegment.setMaxValue(200);
		this.parameterTextFieldSemicircleLineSegment.setRequisite(true);

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(this.parameterUnionBuffer, this.parameterRingBuffer, this.parameterRetainAttribute, this.parameterTextFieldSemicircleLineSegment);

		// 结果
		this.parameterSaveDataset = new ParameterSaveDataset();

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.addParameters(this.parameterSaveDataset);
		parameterCombineResult.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));

		parameters.setParameters(
				parameterCombineSourceData,
				parameterCombineBufferType,
				parameterCombineRadioList,
				parameterCombineParameter,
				parameterCombineResult
		);

		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.SIMPLE_VECTOR, parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATASET, ProcessOutputResultProperties.getString("String_MultiBufferResult"), DatasetTypes.REGION, parameterCombineResult);
	}

	private void initComponentState() {
		this.parameterRadiusUnit.setSelectedItem(BufferRadiusUnit.Meter);
		this.parameterRetainAttribute.setSelectedItem(true);
		this.parameterTextFieldSemicircleLineSegment.setSelectedItem("100");
		this.radioButtonFlatOrRound.setSelectedItem(this.radioButtonFlatOrRound.getItemAt(0));
		this.comboBoxBufferLeftOrRight.setSelectedItem(this.comboBoxBufferLeftOrRight.getItemAt(0));
		this.comboBoxBufferLeftOrRight.setEnabled(false);
		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		if (datasetVector != null) {
			this.datasource.setSelectedItem(datasetVector.getDatasource());
			this.dataset.setSelectedItem(datasetVector);
		}
		this.parameterSaveDataset.setDefaultDatasetName("result_multiBuffer");
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(this.parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void registerListener() {
		this.dataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (dataset.getSelectedDataset() != null) {
					setBufferTypePanelEnabled(dataset.getSelectedDataset().getType().equals(DatasetType.LINE));
				}
			}
		});

		this.radioButtonFlatOrRound.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterRadioButton.RADIO_BUTTON_VALUE)) {
					comboBoxBufferLeftOrRight.setEnabled(radioButtonFlatOrRound.getSelectedItem() == radioButtonFlatOrRound.getItemAt(1));
				}
			}
		});
		this.parameterUnionBuffer.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterCheckBox.PARAMETER_CHECK_BOX_VALUE)) {
					parameterRetainAttribute.setEnabled(!(boolean) evt.getNewValue());
				}
			}
		});
	}

	/**
	 * 缓冲类型面板是否可用
	 * 只对线数据集有效
	 *
	 * @param bufferTypePanelEnabled
	 */
	private void setBufferTypePanelEnabled(Boolean bufferTypePanelEnabled) {
		this.comboBoxBufferLeftOrRight.setEnabled(bufferTypePanelEnabled);
		this.radioButtonFlatOrRound.setEnabled(bufferTypePanelEnabled);
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_MultiBufferAnalyst");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		Datasource resultDatasource = null;
		String resultName = null;
		try {

			// 源数据
			DatasetVector sourceDatasetVector;
			if (this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET) != null
					&& this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
				sourceDatasetVector = (DatasetVector) this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue();
			} else {
				sourceDatasetVector = (DatasetVector) this.dataset.getSelectedItem();
			}
			// 线缓冲类型
			int bufferType = (Integer) ((ParameterDataNode) this.radioButtonFlatOrRound.getSelectedItem()).getData();
			boolean isLeft = false;
			if (bufferType == BUFFERTYPE_FLAT) {
				isLeft = (Boolean) this.comboBoxBufferLeftOrRight.getSelectedData();
			}
			//缓冲半径列表
			ArrayList<Double> radioLists = this.parameterMultiBufferRadioList.getRadioLists();
			if (radioLists.size() == 0) {
				Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_BufferRadiusListNull"));
				return false;
			}
			double[] radioListResult = null;
			if (radioLists != null && radioLists.size() > 0) {
				// 还有待优化-yuanR存疑2017.8.31
				// 不支持负数，因此进行取绝对值处理-yuanR2017.8.31
				for (int i = 0; i < radioLists.size(); i++) {
					radioLists.set(i, Math.abs(radioLists.get(i)));
				}

				// 去重
				Set<Double> set = new HashSet<>();
				set.addAll(radioLists);
				Double[] radioListDouble = set.toArray(new Double[set.size()]);
				radioListResult = new double[radioListDouble.length];
				for (int i = 0; i < radioListDouble.length; i++) {
					radioListResult[i] = radioListDouble[i];
				}
			}

			BufferRadiusUnit radiusUnit = (BufferRadiusUnit) this.parameterRadiusUnit.getSelectedData();
			// 参数面板属性
			boolean isUnion = "true".equalsIgnoreCase(this.parameterUnionBuffer.getSelectedItem());
			boolean isRing = "true".equalsIgnoreCase(this.parameterRingBuffer.getSelectedItem());
			boolean isAttributeRetained = "true".equalsIgnoreCase(this.parameterRetainAttribute.getSelectedItem());
			int semicircleLineSegment = Integer.valueOf(this.parameterTextFieldSemicircleLineSegment.getSelectedItem());
			// 结果面板属性
			resultDatasource = this.parameterSaveDataset.getResultDatasource();
			resultName = resultDatasource.getDatasets().getAvailableDatasetName(this.parameterSaveDataset.getDatasetName());

			// 创建一个新的数据集用于接受多重缓冲分析结果
			DatasetVectorInfo vectorInfo = new DatasetVectorInfo();
			vectorInfo.setName(resultName);
			vectorInfo.setType(DatasetType.REGION);
			DatasetVector resultDataset = resultDatasource.getDatasets().create(vectorInfo);
			resultDataset.setPrjCoordSys(sourceDatasetVector.getPrjCoordSys());

			BufferAnalyst.addSteppedListener(this.steppedListener);
			if (sourceDatasetVector.getType().equals(DatasetType.LINE) && bufferType == BUFFERTYPE_FLAT) {
				isSuccessful = BufferAnalyst.createLineOneSideMultiBuffer(
						sourceDatasetVector, resultDataset,
						radioListResult, radiusUnit, semicircleLineSegment,
						isLeft, isUnion, isAttributeRetained, isRing);
			} else {
				isSuccessful = BufferAnalyst.createMultiBuffer(
						sourceDatasetVector, resultDataset,
						radioListResult, radiusUnit, semicircleLineSegment,
						isUnion, isAttributeRetained, isRing);
			}
			this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(resultDataset);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
		} finally {
			BufferAnalyst.removeSteppedListener(this.steppedListener);
			// 如果失败了，删除新建的数据集
			if (!isSuccessful && resultDatasource != null && resultName != null) {
				resultDatasource.getDatasets().delete(resultName);
			}
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.MULTIBUFFER;
	}
}