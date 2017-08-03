package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
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
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.DefaultOpenServerMap;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
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
	private ParameterTextField parameterStaticModel = new ParameterTextField().setDescribe(ProcessProperties.getString("String_StaticModel"));
	private ParameterTextField parameterWeightIndex = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Index"));

	public MetaProcessPolygonAggregation() {
		initComponents();
		initComponentLayout();
		initComponentState();
		initConstraint();
	}

	private void initComponents() {
		ParameterDataNode parameterDataNode = new ParameterDataNode(ProcessProperties.getString("String_PolygonAggregationType"), "SummaryRegionMain");
		parameterAggregationType.setItems(parameterDataNode);
		parameterAggregationType.setSelectedItem(parameterDataNode);

		parameterBigDatasourceDatasource.setDescribe(ControlsProperties.getString("String_Label_ResultDatasource"));
		parameterSingleDataset.setDescribe(ProcessProperties.getString("String_AggregateDataset"));

		parameterStaticModel.setSelectedItem("max");
		parameterWeightIndex.setSelectedItem("col7");
	}

	private void initComponentLayout() {
		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_setParameter"));
		parameterCombineSetting.addParameters(
				parameterAggregationType,
				parameterBigDatasourceDatasource,
				parameterSingleDataset,
				parameterStaticModel,
				parameterWeightIndex);
		parameters.setParameters(
				parameterIServerLogin,
				parameterInputDataType,
				parameterCombineSetting
		);
		parameters.getOutputs().addData("PolygonAggregationResult", Type.UNKOWN);
	}

	private void initComponentState() {
		parameterInputDataType.parameterDatasetType.setItems(new ParameterDataNode(ProcessProperties.getString("String_Point"), "POINT"));
		parameterInputDataType.parameterSourceDataset.setDatasetTypes(DatasetType.POINT);
		Dataset defaultBigDataStoreDataset = DatasetUtilities.getDefaultBigDataStoreDataset();
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
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			IServerService service = parameterIServerLogin.login();
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			CommonSettingCombine datasetInfo = new CommonSettingCombine("datasetInfo", "");
			if(parameterInputDataType.parameterDataInputWay.getSelectedData().toString().equals("0")){
				CommonSettingCombine filePath = new CommonSettingCombine("filePath",parameterInputDataType.parameterHDFSPath.getSelectedItem().toString());
				input.add(filePath);
			}else if(parameterInputDataType.parameterDataInputWay.getSelectedData().toString().equals("1")){
				CommonSettingCombine type = new CommonSettingCombine("type",parameterInputDataType.parameterDataSouceType.getSelectedItem().toString());
				CommonSettingCombine url = new CommonSettingCombine("url",parameterInputDataType.parameterDataSoucePath.getSelectedItem().toString());
				CommonSettingCombine datasetName = new CommonSettingCombine("datasetName",parameterInputDataType.parameterDatasetName.getSelectedItem().toString());
				CommonSettingCombine datasetType = new CommonSettingCombine("datasetType",(String) parameterInputDataType.parameterDatasetType.getSelectedData());
				CommonSettingCombine numSlices = new CommonSettingCombine("numSlices",parameterInputDataType.parameterSpark.getSelectedItem().toString());
				datasetInfo.add(type,url,datasetName,datasetType);
				input.add(datasetInfo,numSlices);
			}else{
				Dataset sourceDataset = parameterInputDataType.parameterSourceDataset.getSelectedDataset();
				CommonSettingCombine dataSourceName = new CommonSettingCombine("dataSourceName",((Datasource)parameterInputDataType.parameterSourceDatasource.getSelectedItem()).getAlias());
				CommonSettingCombine name = new CommonSettingCombine("name",sourceDataset.getName());
				CommonSettingCombine type = new CommonSettingCombine("type",(String) parameterInputDataType.parameterDatasetType.getSelectedData());
				CommonSettingCombine engineType = new CommonSettingCombine("engineType",parameterInputDataType.parameterEngineType.getSelectedItem().toString());
				CommonSettingCombine server = new CommonSettingCombine("server",parameterInputDataType.parameterTextFieldAddress.getSelectedItem().toString());
				CommonSettingCombine dataBase = new CommonSettingCombine("dataBase",parameterInputDataType.parameterDataBaseName.getSelectedItem().toString());
				CommonSettingCombine user = new CommonSettingCombine("user",parameterInputDataType.parameterTextFieldUserName.getSelectedItem().toString());
				CommonSettingCombine password = new CommonSettingCombine("password",parameterInputDataType.parameterTextFieldPassword.getSelectedItem().toString());
				CommonSettingCombine datasourceConnectionInfo = new CommonSettingCombine("datasourceConnectionInfo", "");
				datasourceConnectionInfo.add(engineType,server,dataBase,user,password);
				datasetInfo.add(type,name,dataSourceName,datasourceConnectionInfo);
				input.add(datasetInfo);
			}
			Dataset dataset = parameterSingleDataset.getSelectedDataset();
			CommonSettingCombine fields = new CommonSettingCombine("fields", parameterWeightIndex.getSelectedItem().toString());
			CommonSettingCombine statisticModes = new CommonSettingCombine("statisticModes", parameterStaticModel.getSelectedItem().toString());
			CommonSettingCombine regionDataset = new CommonSettingCombine("regionDataset", dataset.getName());
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			analyst.add(fields, statisticModes, regionDataset);

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
				messageBus.run();
			}
			fireRunning(new RunningEvent(this, 100, "finished"));
			parameters.getOutputs().getData("OverlayResult").setValue("");// TODO: 2017/6/26 也许没结果,but
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
		return MetaKeys.POLYGON_AGGREGATION;
	}
}
