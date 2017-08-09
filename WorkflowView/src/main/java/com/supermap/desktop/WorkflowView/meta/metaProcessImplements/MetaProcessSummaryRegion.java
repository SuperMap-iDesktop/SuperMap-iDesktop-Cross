package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.lbs.Interface.IServerService;
import com.supermap.desktop.lbs.params.CommonSettingCombine;
import com.supermap.desktop.lbs.params.JobResultResponse;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.messageBus.NewMessageBus;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.DefaultOpenServerMap;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CancellationException;

/**
 * Created by caolp on 2017-08-05.
 * 范围汇总分析
 */
public class MetaProcessSummaryRegion extends MetaProcess {
	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	ParameterInputDataType parameterInputDataType = new ParameterInputDataType();
	private ParameterComboBox parameterSummaryType = new ParameterComboBox(ProcessProperties.getString("String_summaryType"));
	private ParameterComboBox parameterMeshType = new ParameterComboBox(ProcessProperties.getString("String_MeshType"));
	private ParameterTextField parameterBounds = new ParameterTextField(ProcessProperties.getString("String_AnalystBounds"));
	private ParameterCheckBox parameterStandardFields = new ParameterCheckBox(ProcessProperties.getString("String_standardSummaryFields"));
	private ParameterCheckBox parameterWeightedFields = new ParameterCheckBox(ProcessProperties.getString("String_weightedSummaryFields"));
	private ParameterTextField parameterStatisticMode = new ParameterTextField(ProcessProperties.getString("String_StaticModel"));
	private ParameterTextField parameterFeildName = new ParameterTextField(ProcessProperties.getString("String_FeildName"));
	private ParameterTextField parameterStatisticMode1 = new ParameterTextField(ProcessProperties.getString("String_StaticModel"));
	private ParameterTextField parameterFeildName1 = new ParameterTextField(ProcessProperties.getString("String_FeildName"));
	private ParameterTextField parameterMeshSize = new ParameterTextField(ProcessProperties.getString("String_MeshSize"));
	private ParameterComboBox parameterMeshSizeUnit = new ParameterComboBox(ProcessProperties.getString("String_MeshSizeUnit"));
	private ParameterCheckBox parametersumShape = new ParameterCheckBox(ProcessProperties.getString("String_SumShape"));
	private ParameterBigDatasourceDatasource parameterBigDatasourceDatasource = new ParameterBigDatasourceDatasource();
	private ParameterSingleDataset parameterSingleDataset = new ParameterSingleDataset(DatasetType.LINE, DatasetType.REGION);

	public MetaProcessSummaryRegion() {
		initComponents();
		initComponentState();
		initComponentLayout();
		initConstraint();
	}

	private void initComponents() {
		parameterSummaryType.setItems(new ParameterDataNode(ProcessProperties.getString("String_summaryMesh"), "SUMMARYMESH"), new ParameterDataNode(ProcessProperties.getString("String_summaryRegion"), "SUMMARYREGION"));
		parameterMeshType.setItems(new ParameterDataNode(ProcessProperties.getString("String_QuadrilateralMesh"), "0"), new ParameterDataNode(ProcessProperties.getString("String_HexagonalMesh"), "1"));
		parameterBounds.setSelectedItem("-74.050,40.650,-73.850,40.850");
		parameterStatisticMode.setSelectedItem("max");
		parameterStatisticMode1.setSelectedItem("max");
		parameterMeshSize.setSelectedItem("100");
		parameterMeshSizeUnit.setItems(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Meter"), "Meter"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Kilometer"), "Kilometer"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Yard"), "Yard"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Foot"), "Foot"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Mile"), "Mile")
		);
		parameterStandardFields.setSelectedItem(false);
		parameterWeightedFields.setSelectedItem(false);
		parametersumShape.setSelectedItem(true);
		parameterBigDatasourceDatasource.setDescribe(ControlsProperties.getString("String_Label_ResultDatasource"));
		parameterSingleDataset.setDescribe(ProcessProperties.getString("String_RegionDataset"));
	}

	private void initComponentState() {
		parameterInputDataType.setSupportDatasetType(DatasetType.LINE, DatasetType.REGION);
		Dataset defaultBigDataStoreDataset = DatasetUtilities.getDefaultBigDataStoreDataset();
		if (defaultBigDataStoreDataset != null) {
			parameterBigDatasourceDatasource.setSelectedItem(defaultBigDataStoreDataset.getDatasource());
			parameterSingleDataset.setSelectedItem(defaultBigDataStoreDataset);
		}
	}

	private void initComponentLayout() {
		final ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_AnalystSet"));
		final ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.addParameters(parameterMeshType, parameterBounds, parameterMeshSize, parameterMeshSizeUnit);
		final ParameterCombine parameterCombine1 = new ParameterCombine();
		parameterCombine1.addParameters(parameterBigDatasourceDatasource, parameterSingleDataset, parameterBounds);
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
				if (parameterStandardFields.getSelectedItem().toString().equals("true")) {
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
				if (parameterWeightedFields.getSelectedItem().toString().equals("true")) {
					switchWeightedFields.switchParameter("1");
				} else {
					switchWeightedFields.switchParameter("0");
				}
			}
		});

		parameterCombineSetting.addParameters(parameterSummaryType, parameterSwitch, parameterStandardFields, switchStandardFields,
				parameterWeightedFields, switchWeightedFields, parametersumShape);
		parameters.addParameters(parameterIServerLogin, parameterInputDataType, parameterCombineSetting);
		parameters.getOutputs().addData("SummaryRegionResult", Type.UNKOWN);
	}

	private void initConstraint() {
		EqualDatasourceConstraint equalSourceDatasource = new EqualDatasourceConstraint();
		equalSourceDatasource.constrained(parameterBigDatasourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalSourceDatasource.constrained(parameterSingleDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}


	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_SummaryRegion");
	}

	@Override
	public boolean execute() {
		try {
			if (parameterStandardFields.getSelectedItem().toString().equals("false") && parameterWeightedFields.getSelectedItem().toString().equals("false")) {
				Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_SummaryRegionMessage"));
				return false;
			}
			fireRunning(new RunningEvent(this, 0, "start"));
			IServerService service = parameterIServerLogin.login();
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			parameterInputDataType.initSourceInput(input);
			CommonSettingCombine type = new CommonSettingCombine("type", parameterSummaryType.getSelectedData().toString());
			CommonSettingCombine bounds = new CommonSettingCombine("bounds", parameterBounds.getSelectedItem().toString());
			CommonSettingCombine sumShape = new CommonSettingCombine("sumShape", parametersumShape.getSelectedItem().toString());
			CommonSettingCombine standardSummaryFields = new CommonSettingCombine("standardSummaryFields", parameterStandardFields.getSelectedItem().toString());
			CommonSettingCombine weightedSummaryFields = new CommonSettingCombine("weightedSummaryFields", parameterWeightedFields.getSelectedItem().toString());
			CommonSettingCombine standardFields = new CommonSettingCombine("standardFields", parameterFeildName.getSelectedItem().toString());
			CommonSettingCombine standardStatisticModes = new CommonSettingCombine("standardStatisticModes", parameterStatisticMode.getSelectedItem().toString());
			CommonSettingCombine weightedFields = new CommonSettingCombine("weightedFields", parameterFeildName1.getSelectedItem().toString());
			CommonSettingCombine weightedStatisticModes = new CommonSettingCombine("weightedStatisticModes", parameterStatisticMode1.getSelectedItem().toString());
			if (parameterSummaryType.getSelectedData().toString().equals("SUMMARYMESH")) {
				CommonSettingCombine meshType = new CommonSettingCombine("meshType", parameterMeshType.getSelectedData().toString());
				CommonSettingCombine resolution = new CommonSettingCombine("resolution", parameterMeshSize.getSelectedItem().toString());
				CommonSettingCombine meshSizeUnit = new CommonSettingCombine("meshSizeUnit", parameterMeshSizeUnit.getSelectedData().toString());
				analyst.add(meshType, bounds, standardSummaryFields, weightedSummaryFields, resolution, meshSizeUnit, sumShape);
				if (parameterStandardFields.getSelectedItem().toString().equals("true")) {
					analyst.add(standardFields, standardStatisticModes);
				}
				if (parameterWeightedFields.getSelectedItem().toString().equals("true")) {
					analyst.add(weightedFields,weightedStatisticModes);
				}
			} else {
				Dataset dataset = parameterSingleDataset.getSelectedDataset();
				CommonSettingCombine regionDataset = new CommonSettingCombine("regionDataset", dataset.getName());
				analyst.add(regionDataset, bounds, standardSummaryFields, weightedSummaryFields, sumShape);
				if (parameterStandardFields.getSelectedItem().toString().equals("true")) {
					analyst.add(standardFields, standardStatisticModes);
				}
				if (parameterWeightedFields.getSelectedItem().toString().equals("true")) {
					analyst.add(weightedFields,weightedStatisticModes);
				}
			}
			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input, analyst, type);
			JobResultResponse response = service.queryResult(MetaKeys.SUMMARY_REGION, commonSettingCombine.getFinalJSon());
			CursorUtilities.setWaitCursor();
			if (null != response) {
				NewMessageBus messageBus = new NewMessageBus(response, new IUpdateProgress() {
					@Override
					public boolean isCancel() {
						return false;
					}

					@Override
					public void setCancel(boolean isCancel) {

					}

					@Override
					public void updateProgress(int percent, String remainTime, String message) throws CancellationException {
						fireRunning(new RunningEvent(MetaProcessSummaryRegion.this, percent, message, -1));
					}

					@Override
					public void updateProgress(String message, int percent, String currentMessage) throws CancellationException {

					}

					@Override
					public void updateProgress(int percent, int totalPercent, String remainTime, String message) throws CancellationException {

					}

					@Override
					public void updateProgress(int percent, String recentTask, int totalPercent, String message) throws CancellationException {

					}
				}, DefaultOpenServerMap.INSTANCE);
				messageBus.run();
			}
			fireRunning(new RunningEvent(this, 100, "finished"));
			parameters.getOutputs().getData("OverlayResult").setValue("");
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			return false;
		} finally {
			CursorUtilities.setDefaultCursor();
		}
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.SUMMARY_REGION;
	}

}
