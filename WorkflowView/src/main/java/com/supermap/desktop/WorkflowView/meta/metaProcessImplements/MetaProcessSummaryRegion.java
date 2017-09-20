package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.lbs.params.CommonSettingCombine;
import com.supermap.desktop.lbs.params.JobResultResponse;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.messageBus.NewMessageBus;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.DefaultOpenServerMap;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.CursorUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by caolp on 2017-08-05.
 * 区域汇总分析
 */
public class MetaProcessSummaryRegion extends MetaProcess {
	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	private ParameterInputDataType parameterInputDataType = new ParameterInputDataType();
	private ParameterInputDataType parameterAnalystDataType = new ParameterInputDataType();
	private ParameterComboBox parameterSummaryType = new ParameterComboBox(ProcessProperties.getString("String_summaryType"));
	private ParameterComboBox parameterMeshType = new ParameterComboBox(ProcessProperties.getString("String_MeshType"));
	private ParameterDefaultValueTextField parameterBounds = new ParameterDefaultValueTextField(ProcessProperties.getString("String_AnalystBounds"));
	private ParameterCheckBox parameterStandardFields = new ParameterCheckBox(ProcessProperties.getString("String_standardSummaryFields"));
	private ParameterCheckBox parameterWeightedFields = new ParameterCheckBox(ProcessProperties.getString("String_weightedSummaryFields"));
	private ParameterDefaultValueTextField parameterStatisticMode = new ParameterDefaultValueTextField(ProcessProperties.getString("String_StaticModel"));
	private ParameterTextField parameterFeildName = new ParameterTextField(ProcessProperties.getString("String_FeildName"));
	private ParameterDefaultValueTextField parameterStatisticMode1 = new ParameterDefaultValueTextField(ProcessProperties.getString("String_StaticModel"));
	private ParameterTextField parameterFeildName1 = new ParameterTextField(ProcessProperties.getString("String_FeildName"));
	private ParameterDefaultValueTextField parameterMeshSize = new ParameterDefaultValueTextField(ProcessProperties.getString("String_MeshSize"));
	private ParameterComboBox parameterMeshSizeUnit = new ParameterComboBox(ProcessProperties.getString("String_MeshSizeUnit"));
	private ParameterCheckBox parametersumShape = new ParameterCheckBox(ProcessProperties.getString("String_SumShape"));

	public MetaProcessSummaryRegion() {
		initComponents();
		initComponentState();
		initComponentLayout();
	}

	private void initComponents() {
		//设置输入数据
		parameterInputDataType.setDescribe(ProcessProperties.getString("String_FileInputPath"));
		parameterIServerLogin.setInputDataType(this.parameterInputDataType);
		//设置分析数据
		parameterIServerLogin.setAnalystDataType(this.parameterAnalystDataType);
		parameterSummaryType.setRequisite(true);
		parameterSummaryType.setItems(new ParameterDataNode(ProcessProperties.getString("String_summaryMesh"), "SUMMARYMESH"), new ParameterDataNode(ProcessProperties.getString("String_summaryRegion"), "SUMMARYREGION"));
		parameterMeshType.setRequisite(true);
		parameterMeshType.setItems(new ParameterDataNode(ProcessProperties.getString("String_QuadrilateralMesh"), "0"), new ParameterDataNode(ProcessProperties.getString("String_HexagonalMesh"), "1"));
		parameterBounds.setDefaultWarningValue("-74.050,40.650,-73.850,40.850");
		parameterStatisticMode.setTipButtonMessage(ProcessProperties.getString("String_StatisticsModeTip"));
		parameterMeshSize.setDefaultWarningValue("100");
		parameterMeshSizeUnit.setItems(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Meter"), "Meter"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Kilometer"), "Kilometer"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Yard"), "Yard"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Foot"), "Foot"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Mile"), "Mile")
		);
		parameterStandardFields.setSelectedItem(false);
		parameterWeightedFields.setSelectedItem(false);
		parametersumShape.setSelectedItem(true);
		parameterFeildName.setRequisite(true);
		parameterStatisticMode.setRequisite(true);
		parameterFeildName1.setRequisite(true);
		parameterStatisticMode1.setRequisite(true);
	}

	private void initComponentState() {
		parameterInputDataType.parameterDataInputWay.removeAllItems();
		parameterInputDataType.parameterDataInputWay.setItems(new ParameterDataNode(ProcessProperties.getString("String_BigDataStore"), "3"),
				new ParameterDataNode(ProcessProperties.getString("String_UDBFile"), "1"), new ParameterDataNode(ProcessProperties.getString("String_PG"), "2"));
		parameterInputDataType.parameterSwitch.switchParameter("3");
		parameterAnalystDataType.parameterDataInputWay.removeAllItems();
		parameterAnalystDataType.parameterDataInputWay.setItems(new ParameterDataNode(ProcessProperties.getString("String_BigDataStore"), "3"),
				new ParameterDataNode(ProcessProperties.getString("String_UDBFile"), "1"), new ParameterDataNode(ProcessProperties.getString("String_PG"), "2"));
		parameterAnalystDataType.parameterSwitch.switchParameter("3");
		parameterAnalystDataType.setBool(true);
		parameterInputDataType.setSupportDatasetType(DatasetType.LINE, DatasetType.REGION);
		parameterAnalystDataType.setSupportDatasetType(DatasetType.REGION);
		parameterIServerLogin.setDataType(parameterInputDataType.supportDatasetType);
		parameterIServerLogin.setAnalystDatasetTypes(parameterAnalystDataType.supportDatasetType);
	}

	private void initComponentLayout() {
		final ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_AnalystSet"));
		final ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(parameterMeshType, parameterBounds, parameterMeshSize, parameterMeshSizeUnit);
		final ParameterCombine parameterCombine1 = new ParameterCombine();
		parameterCombine1.addParameters(parameterAnalystDataType,
				parameterBounds);
		final ParameterSwitch parameterSwitch = new ParameterSwitch();
		parameterSwitch.add("0", parameterCombine);
		parameterSwitch.add("1", parameterCombine1);
		parameterSummaryType.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					if (parameterSummaryType.getSelectedData().toString().equals("SUMMARYMESH")) {
						parameterSwitch.switchParameter("0");
					} else {
						parameterSwitch.switchParameter("1");
					}
				}
			}
		});

		final ParameterCombine combineCheckBox = new ParameterCombine();
		combineCheckBox.addParameters(parameterFeildName, parameterStatisticMode);
		final ParameterSwitch switchStandardFields = new ParameterSwitch();
		switchStandardFields.add("0", new ParameterCombine());
		switchStandardFields.add("1", combineCheckBox);
		parameterStandardFields.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (parameterStandardFields.getSelectedItem().equals("true")) {
					switchStandardFields.switchParameter("1");
				} else {
					switchStandardFields.switchParameter("0");
				}
			}
		});

		final ParameterCombine combineCheckBox1 = new ParameterCombine();
		combineCheckBox1.addParameters(parameterFeildName1, parameterStatisticMode1);
		final ParameterSwitch switchWeightedFields = new ParameterSwitch();
		switchWeightedFields.add("0", new ParameterCombine());
		switchWeightedFields.add("1", combineCheckBox1);
		parameterWeightedFields.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (parameterWeightedFields.getSelectedItem().equals("true")) {
					switchWeightedFields.switchParameter("1");
				} else {
					switchWeightedFields.switchParameter("0");
				}
			}
		});

		parameterCombineSetting.addParameters(parameterSummaryType, parameterSwitch, parameterStandardFields, switchStandardFields,
				parameterWeightedFields, switchWeightedFields, parametersumShape);
		parameters.addParameters(parameterIServerLogin, parameterInputDataType, parameterCombineSetting);
		parameters.getOutputs().addData("SummaryRegionResult", ProcessOutputResultProperties.getString("String_BoundsAnalysisResult"), Type.UNKOWN);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_SummaryRegion");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful;
		try {
			if (parameterStandardFields.getSelectedItem().equals("false") && parameterWeightedFields.getSelectedItem().toString().equals("false")) {
				Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_SummaryRegionMessage"));
				return false;
			}
			fireRunning(new RunningEvent(this, ProcessProperties.getString("String_Running")));
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			parameterInputDataType.initSourceInput(input);
			CommonSettingCombine type = new CommonSettingCombine("type", parameterSummaryType.getSelectedData().toString());
			CommonSettingCombine bounds = new CommonSettingCombine("bounds", parameterBounds.getSelectedItem());
			CommonSettingCombine sumShape = new CommonSettingCombine("sumShape", parametersumShape.getSelectedItem());
			CommonSettingCombine standardSummaryFields = new CommonSettingCombine("standardSummaryFields", parameterStandardFields.getSelectedItem());
			CommonSettingCombine weightedSummaryFields = new CommonSettingCombine("weightedSummaryFields", parameterWeightedFields.getSelectedItem());
			CommonSettingCombine standardFields = new CommonSettingCombine("standardFields", parameterFeildName.getSelectedItem());
			CommonSettingCombine standardStatisticModes = new CommonSettingCombine("standardStatisticModes", parameterStatisticMode.getSelectedItem());
			CommonSettingCombine weightedFields = new CommonSettingCombine("weightedFields", parameterFeildName1.getSelectedItem());
			CommonSettingCombine weightedStatisticModes = new CommonSettingCombine("weightedStatisticModes", parameterStatisticMode1.getSelectedItem());
			if (parameterSummaryType.getSelectedData().toString().equals("SUMMARYMESH")) {
				CommonSettingCombine meshType = new CommonSettingCombine("meshType", parameterMeshType.getSelectedData().toString());
				CommonSettingCombine resolution = new CommonSettingCombine("resolution", parameterMeshSize.getSelectedItem());
				CommonSettingCombine meshSizeUnit = new CommonSettingCombine("meshSizeUnit", parameterMeshSizeUnit.getSelectedData().toString());
				analyst.add(meshType, bounds, standardSummaryFields, weightedSummaryFields, resolution, meshSizeUnit, sumShape);
				if (parameterStandardFields.getSelectedItem().equals("true")) {
					analyst.add(standardFields, standardStatisticModes);
				}
				if (parameterWeightedFields.getSelectedItem().equals("true")) {
					analyst.add(weightedFields, weightedStatisticModes);
				}
			} else {
				parameterAnalystDataType.initAnalystInput(analyst, 3);
				analyst.add(bounds, standardSummaryFields, weightedSummaryFields, sumShape);
				if (parameterStandardFields.getSelectedItem().equals("true")) {
					analyst.add(standardFields, standardStatisticModes);
				}
				if (parameterWeightedFields.getSelectedItem().equals("true")) {
					analyst.add(weightedFields, weightedStatisticModes);
				}
			}
			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input, analyst, type);
			JobResultResponse response = parameterIServerLogin.getService().queryResult(MetaKeys.SUMMARY_REGION, commonSettingCombine.getFinalJSon());
			CursorUtilities.setWaitCursor();
			if (null != response) {
				NewMessageBus messageBus = new NewMessageBus(response, DefaultOpenServerMap.INSTANCE);
				isSuccessful = messageBus.run();
			} else {
				isSuccessful = false;
			}

			parameters.getOutputs().getData("SummaryRegionResult").setValue("");
		} catch (Exception e) {
			isSuccessful = false;
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
		} finally {
			CursorUtilities.setDefaultCursor();
		}

		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.SUMMARY_REGION;
	}

}
