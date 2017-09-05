package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
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
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.DefaultOpenServerMap;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.util.concurrent.CancellationException;

/**
 * Created by caolp on 2017-05-26.
 * 多边形聚合分析
 */
public class MetaProcessPolygonAggregation extends MetaProcess {
	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	ParameterInputDataType parameterInputDataType = new ParameterInputDataType();
	private ParameterComboBox parameterAggregationType = new ParameterComboBox().setDescribe(ProcessProperties.getString("String_AggregationType"));
	private ParameterBigDatasourceDatasource parameterBigDatasourceDatasource = new ParameterBigDatasourceDatasource();
	private ParameterSingleDataset parameterSingleDataset = new ParameterSingleDataset(DatasetType.REGION);
	private ParameterDefaultValueTextField parameterStaticModel = new ParameterDefaultValueTextField().setDescribe(ProcessProperties.getString("String_StaticModel"));
	private ParameterDefaultValueTextField parameterWeightIndex = new ParameterDefaultValueTextField().setDescribe(ProcessProperties.getString("String_Index"));
	private ParameterDefaultValueTextField parameterDataBaseName = new ParameterDefaultValueTextField(ProcessProperties.getString("String_DataBaseName"));
	private ParameterDefaultValueTextField parameterTextFieldAddress = new ParameterDefaultValueTextField(CoreProperties.getString("String_Server"));
	private ParameterDefaultValueTextField parameterTextFieldUserName = new ParameterDefaultValueTextField(ProcessProperties.getString("String_UserName"));
	private ParameterPassword parameterTextFieldPassword = new ParameterPassword(ProcessProperties.getString("String_PassWord"));

	public MetaProcessPolygonAggregation() {
		initComponents();
		initComponentLayout();
		initComponentState();
		initConstraint();
	}

	private void initComponents() {
		ParameterDataNode parameterDataNode = new ParameterDataNode(ProcessProperties.getString("String_PolygonAggregationType"), "SUMMARYREGION");
		parameterAggregationType.setRequisite(true);
		parameterAggregationType.setItems(parameterDataNode);
		parameterAggregationType.setSelectedItem(parameterDataNode);
		parameterTextFieldAddress.setDefaultWarningValue("192.168.15.248");
		parameterTextFieldAddress.setRequisite(true);
		parameterDataBaseName.setDefaultWarningValue("supermap");
		parameterDataBaseName.setRequisite(true);
		parameterTextFieldUserName.setDefaultWarningValue("postgres");
		parameterTextFieldUserName.setRequisite(true);
		parameterTextFieldPassword.setSelectedItem("supermap");
		parameterTextFieldPassword.setRequisite(true);
		parameterBigDatasourceDatasource.setRequisite(true);
		parameterBigDatasourceDatasource.setDescribe(ControlsProperties.getString("String_Label_ResultDatasource"));
		parameterSingleDataset.setRequisite(true);
		parameterSingleDataset.setDescribe(ProcessProperties.getString("String_AggregateDataset"));
		parameterStaticModel.setToolTip(ProcessProperties.getString("String_StatisticsModeTip"));
		parameterWeightIndex.setToolTip(ProcessProperties.getString("String_WeightIndexTip"));
	}

	private void initComponentLayout() {
		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_setParameter"));
		parameterCombineSetting.addParameters(
				parameterAggregationType,
				parameterTextFieldAddress,
				parameterDataBaseName,
				parameterTextFieldUserName,
				parameterTextFieldPassword,
				parameterBigDatasourceDatasource,
				parameterSingleDataset,
				parameterStaticModel,
				parameterWeightIndex);
		parameters.setParameters(
				parameterIServerLogin,
				parameterInputDataType,
				parameterCombineSetting
		);
		parameters.getOutputs().addData("PolygonAggregationResult", ProcessOutputResultProperties.getString("String_PolygonAnalysisResult"), Type.UNKOWN);
	}

	private void initComponentState() {
		parameterInputDataType.setSupportDatasetType(DatasetType.POINT);
		Dataset defaultBigDataStoreDataset = DatasetUtilities.getDefaultDataset(DatasetType.REGION);
		if (defaultBigDataStoreDataset != null) {
			parameterBigDatasourceDatasource.setSelectedItem(defaultBigDataStoreDataset.getDatasource());
			parameterSingleDataset.setSelectedItem(defaultBigDataStoreDataset);
		}
	}


	private void initConstraint() {
		EqualDatasourceConstraint equalSourceDatasource = new EqualDatasourceConstraint();
		equalSourceDatasource.constrained(parameterBigDatasourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalSourceDatasource.constrained(parameterSingleDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}


	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_PolygonAggregation");
	}

	@Override
	public IParameterPanel getComponent() {
		return this.parameters.getPanel();
	}

	@Override
	public boolean execute() {
		boolean isSuccess;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			IServerService service = parameterIServerLogin.login();
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			parameterInputDataType.initSourceInput(input);
			Dataset dataset = parameterSingleDataset.getSelectedDataset();
			CommonSettingCombine fields = new CommonSettingCombine("fields", parameterWeightIndex.getSelectedItem().toString());
			CommonSettingCombine statisticModes = new CommonSettingCombine("statisticModes", parameterStaticModel.getSelectedItem().toString());
			String regionDatasourceStr = "{\\\"type\\\":\\\"pg\\\",\\\"info\\\":[{\\\"server\\\":\\\"" + parameterTextFieldAddress.getSelectedItem() + "\\\",\\\"datasetNames\\\":[\\\"" + dataset.getName() + "\\\"],\\\"database\\\":\\\"" + parameterDataBaseName.getSelectedItem() + "\\\",\\\"user\\\":\\\"" + parameterTextFieldUserName.getSelectedItem() + "\\\",\\\"password\\\":\\\"" + parameterTextFieldPassword.getSelectedItem() + "\\\"}]}";
			CommonSettingCombine regionDatasource = new CommonSettingCombine("regionDatasource", regionDatasourceStr);
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			analyst.add(fields, statisticModes, regionDatasource);

			CommonSettingCombine type = new CommonSettingCombine("type", parameterAggregationType.getSelectedData().toString());
			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input, analyst, type);

			JobResultResponse response = service.queryResult(MetaKeys.POLYGON_AGGREGATION, commonSettingCombine.getFinalJSon());
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
						fireRunning(new RunningEvent(MetaProcessPolygonAggregation.this, percent, message, -1));
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
				isSuccess = messageBus.run();
			} else {
				fireRunning(new RunningEvent(this, 100, "Failed"));
				isSuccess = false;
			}
			parameters.getOutputs().getData("PolygonAggregationResult").setValue("");// TODO: 2017/6/26 也许没结果,but
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			return false;
		} finally {
			CursorUtilities.setDefaultCursor();
		}
		return isSuccess;
	}

	@Override
	public String getKey() {
		return MetaKeys.POLYGON_AGGREGATION;
	}
}
