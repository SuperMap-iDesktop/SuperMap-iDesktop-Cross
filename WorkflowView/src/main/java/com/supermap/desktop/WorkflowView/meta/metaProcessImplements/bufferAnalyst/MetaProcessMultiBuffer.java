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
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.MultiBufferRadioList.ParameterMultiBufferRadioList;
import com.supermap.desktop.process.util.EnumParser;
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

	public static final int BUFFERTYPE_ROUND = 0;
	public static final int BUFFERTYPE_FLAT = 1;

	// 缓冲数据
	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;
	// 缓冲类型
	private ParameterRadioButton radioButtonFlatOrRound;
	private ParameterComboBox comboBoxBufferLeftOrRight;
	// 缓冲半径列表
	private ParameterMultiBufferRadioList parameterMultiBufferRadioList;
	private ParameterEnum parameterRadiusUnit;
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
		initRequisite();
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

		// 源数据
		datasource = new ParameterDatasourceConstrained();
		dataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		datasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));

		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(datasource, dataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		// 类型-只对线数据集有效
		radioButtonFlatOrRound = new ParameterRadioButton();
		ParameterDataNode round = new ParameterDataNode(ProcessProperties.getString("String_CheckBox_BufferRound"), BUFFERTYPE_ROUND);
		ParameterDataNode flat = new ParameterDataNode(ProcessProperties.getString("String_CheckBox_BufferFlat"), BUFFERTYPE_FLAT);
		radioButtonFlatOrRound.setItems(new ParameterDataNode[]{round, flat});
		comboBoxBufferLeftOrRight = new ParameterComboBox(ProcessProperties.getString("Label_LineBufferDirection"));
		comboBoxBufferLeftOrRight.addItem(new ParameterDataNode(ProcessProperties.getString("String_CheckBox_Left"), true));
		comboBoxBufferLeftOrRight.addItem(new ParameterDataNode(ProcessProperties.getString("String_CheckBox_Right"), false));

		ParameterCombine parameterCombineBufferType = new ParameterCombine();
		parameterCombineBufferType.addParameters(radioButtonFlatOrRound, comboBoxBufferLeftOrRight);
		parameterCombineBufferType.setDescribe(ProcessProperties.getString("String_Title_BufferType"));

		// 半径列表
		parameterMultiBufferRadioList = new ParameterMultiBufferRadioList();
		parameterRadiusUnit = new ParameterEnum(new EnumParser(BufferRadiusUnit.class, values, parameterDataNodes)).setDescribe(ProcessProperties.getString("Label_BufferRadius"));

		ParameterCombine parameterCombineRadioList = new ParameterCombine();
		parameterCombineRadioList.setDescribe(ProcessProperties.getString("String_Title_BufferRadioList"));
		parameterCombineRadioList.addParameters(parameterMultiBufferRadioList, parameterRadiusUnit);

		// 参数
		parameterUnionBuffer = new ParameterCheckBox(ProcessProperties.getString("String_UnionBufferItem"));
		parameterRingBuffer = new ParameterCheckBox(ProcessProperties.getString("String_CreateRingBuffer"));
		parameterRetainAttribute = new ParameterCheckBox(ProcessProperties.getString("String_RetainAttribute"));
		parameterTextFieldSemicircleLineSegment = new ParameterNumber(ProcessProperties.getString("Label_SemicircleLineSegment"));
		parameterTextFieldSemicircleLineSegment.setMaxBit(0);
		parameterTextFieldSemicircleLineSegment.setMinValue(4);
		parameterTextFieldSemicircleLineSegment.setMaxValue(200);

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(parameterUnionBuffer, parameterRingBuffer, parameterRetainAttribute, parameterTextFieldSemicircleLineSegment);

		// 结果
		parameterSaveDataset = new ParameterSaveDataset();

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.addParameters(parameterSaveDataset);
		parameterCombineResult.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));

		parameters.setParameters(
				parameterCombineSourceData,
				parameterCombineBufferType,
				parameterCombineRadioList,
				parameterCombineParameter,
				parameterCombineResult
		);
		parameterCombineSourceData.setRequisite(true);
		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.SIMPLE_VECTOR, parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATASET, ProcessOutputResultProperties.getString("String_MultiBufferResult"), DatasetTypes.REGION, parameterCombineResult);
	}

	private void initComponentState() {
		parameterRadiusUnit.setSelectedItem(BufferRadiusUnit.Meter);
		parameterTextFieldSemicircleLineSegment.setSelectedItem("100");
		radioButtonFlatOrRound.setSelectedItem(radioButtonFlatOrRound.getItemAt(0));
		comboBoxBufferLeftOrRight.setSelectedItem(comboBoxBufferLeftOrRight.getItemAt(0));
		comboBoxBufferLeftOrRight.setEnabled(false);
		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
			parameterSaveDataset.setSelectedItem(datasource.getSelectedItem().getDatasets().getAvailableDatasetName("result_multiBuffer"));
		} else {
			parameterSaveDataset.setSelectedItem("result_multiBuffer");
		}
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void registerListener() {
		dataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (dataset.getSelectedDataset() != null) {
					setBufferTypePanelEnabled(dataset.getSelectedDataset().getType().equals(DatasetType.LINE));
				}
			}
		});

		radioButtonFlatOrRound.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterRadioButton.RADIO_BUTTON_VALUE)) {
					comboBoxBufferLeftOrRight.setEnabled(radioButtonFlatOrRound.getSelectedItem() == radioButtonFlatOrRound.getItemAt(1));
				}
			}
		});
		parameterUnionBuffer.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterCheckBox.PARAMETER_CHECK_BOX_VALUE)) {
					parameterRetainAttribute.setEnabled(!(boolean) evt.getNewValue());
				}
			}
		});
	}

	private void initRequisite() {
		parameterRadiusUnit.setRequisite(true);
		parameterUnionBuffer.setRequisite(true);
		parameterRingBuffer.setRequisite(true);
		parameterRetainAttribute.setRequisite(true);
		parameterTextFieldSemicircleLineSegment.setRequisite(true);
	}

	/**
	 * 缓冲类型面板是否可用
	 * 只对线数据集有效
	 *
	 * @param bufferTypePanelEnabled
	 */
	public void setBufferTypePanelEnabled(Boolean bufferTypePanelEnabled) {
		comboBoxBufferLeftOrRight.setEnabled(bufferTypePanelEnabled);
		radioButtonFlatOrRound.setEnabled(bufferTypePanelEnabled);
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
			fireRunning(new RunningEvent(this, 0, "start"));

			// 源数据
			DatasetVector sourceDatasetVector = null;
			if (this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET) != null
					&& this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
				sourceDatasetVector = (DatasetVector) this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue();
			} else {
				sourceDatasetVector = (DatasetVector) dataset.getSelectedItem();
			}
			// 线缓冲类型
			int bufferType = (Integer) ((ParameterDataNode) radioButtonFlatOrRound.getSelectedItem()).getData();
			boolean isLeft = false;
			if (bufferType == BUFFERTYPE_FLAT) {
				isLeft = (Boolean) comboBoxBufferLeftOrRight.getSelectedData();
			}
			//缓冲半径列表
			ArrayList<Double> radioLists = parameterMultiBufferRadioList.getRadioLists();
			double[] radioListResult = null;
			if (radioLists != null && radioLists.size() > 0) {
				// 还有待优化-yuanR存疑2017.8.31
				// 不支持负数，因此进行取绝对值处理-yuanR2017.8.31
				for (int i = 0; i < radioLists.size(); i++) {
					radioLists.set(i, Math.abs(radioLists.get(i)));
				}

				// 去重
				Set<Double> set = new HashSet<>();
				for (int i = 0; i < radioLists.size(); i++) {
					set.add(radioLists.get(i));
				}
				Double[] radioListDouble = (Double[]) set.toArray(new Double[set.size()]);
				radioListResult = new double[radioListDouble.length];
				for (int i = 0; i < radioListDouble.length; i++) {
					radioListResult[i] = radioListDouble[i];
				}
			}

			BufferRadiusUnit radiusUnit = (BufferRadiusUnit) parameterRadiusUnit.getSelectedData();
			// 参数面板属性
			boolean isUnion = "true".equalsIgnoreCase((String) parameterUnionBuffer.getSelectedItem());
			boolean isRing = "true".equalsIgnoreCase((String) parameterRingBuffer.getSelectedItem());
			boolean isAttributeRetained = "true".equalsIgnoreCase((String) parameterRetainAttribute.getSelectedItem());
			int semicircleLineSegment = Integer.valueOf(((String) parameterTextFieldSemicircleLineSegment.getSelectedItem()));
			// 结果面板属性
			resultDatasource = parameterSaveDataset.getResultDatasource();
			resultName = resultDatasource.getDatasets().getAvailableDatasetName(parameterSaveDataset.getDatasetName());

			// 创建一个新的数据集用于接受多重缓冲分析结果
//			BufferAnalyst.addSteppedListener(this.steppedListener);
			DatasetVectorInfo vectorInfo = new DatasetVectorInfo();
			vectorInfo.setName(resultName);
			vectorInfo.setType(DatasetType.REGION);
			DatasetVector resultDataset = resultDatasource.getDatasets().create(vectorInfo);
			resultDataset.setPrjCoordSys(sourceDatasetVector.getPrjCoordSys());

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
			fireRunning(new RunningEvent(this, 100, "finished"));
			this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(resultDataset);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			// 如果失败了，删除新建的数据集
			if (resultDatasource != null && resultName != null) {
				resultDatasource.getDatasets().delete(resultName);
			}
		} finally {
//			BufferAnalyst.removeSteppedListener(this.steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.MULTIBUFFER;
	}
}