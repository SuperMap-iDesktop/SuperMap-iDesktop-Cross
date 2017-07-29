package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
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
	private ParameterHDFSPath parameterHDFSPath = new ParameterHDFSPath();
	private ParameterTextField parameterTextFieldXIndex = new ParameterTextField(ProcessProperties.getString("String_XIndex"));
	private ParameterTextField parameterTextFieldYIndex = new ParameterTextField(ProcessProperties.getString("String_YIndex"));
	private ParameterTextField parameterTextFieldSeparator = new ParameterTextField(ProcessProperties.getString("String_Separator"));
	private ParameterComboBox parameterAggregationType = new ParameterComboBox().setDescribe(ProcessProperties.getString("String_AggregationType"));
	private ParameterBigDatasourceDatasource parameterBigDatasourceDatasource = new ParameterBigDatasourceDatasource();
	private ParameterSingleDataset parameterSingleDataset = new ParameterSingleDataset();
	private ParameterTextField parameterStaticModel = new ParameterTextField().setDescribe(ProcessProperties.getString("String_StaticModel"));
	private ParameterTextField parameterWeightIndex = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Index"));

	public MetaProcessPolygonAggregation() {
		initComponents();
		initComponentLayout();
		initComponentState();
		initConstraint();
	}

	private void initComponents() {
		parameterHDFSPath.setSelectedItem("hdfs://192.168.20.189:9000/data/newyork_taxi_2013-01_14k.csv");
		parameterTextFieldXIndex.setSelectedItem("10");
		parameterTextFieldYIndex.setSelectedItem("11");
		parameterTextFieldSeparator.setSelectedItem(",");
		parameterTextFieldSeparator.setEnabled(false);

		ParameterDataNode parameterDataNode = new ParameterDataNode(ProcessProperties.getString("String_PolygonAggregationType"), "SummaryRegionMain");
		parameterAggregationType.setItems(parameterDataNode);
		parameterAggregationType.setSelectedItem(parameterDataNode);

		parameterBigDatasourceDatasource.setDescribe(ProcessProperties.getString("String_BigDataSource"));
		parameterSingleDataset.setDescribe(ProcessProperties.getString("String_AggregateDataset"));

		parameterStaticModel.setSelectedItem("max");
		parameterWeightIndex.setSelectedItem("7");
	}

	private void initComponentLayout() {
		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_setParameter"));
		parameterCombineSetting.addParameters(
				parameterHDFSPath,
				parameterTextFieldXIndex,
				parameterTextFieldYIndex,
				parameterTextFieldSeparator,
				parameterAggregationType,
				parameterBigDatasourceDatasource,
				parameterSingleDataset,
				parameterStaticModel,
				parameterWeightIndex);
		parameters.setParameters(
				parameterIServerLogin,
				parameterCombineSetting
		);
		parameters.getOutputs().addData("PolygonAggregationResult", Type.UNKOWN);
	}

	private void initComponentState() {
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
			Dataset dataset = parameterSingleDataset.getSelectedDataset();
			CommonSettingCombine filePath = new CommonSettingCombine("filePath", parameterHDFSPath.getSelectedItem().toString());
			CommonSettingCombine xIndex = new CommonSettingCombine("xIndex", parameterTextFieldXIndex.getSelectedItem().toString());
			CommonSettingCombine yIndex = new CommonSettingCombine("yIndex", parameterTextFieldYIndex.getSelectedItem().toString());
			CommonSettingCombine separator = new CommonSettingCombine("separator", parameterTextFieldSeparator.getSelectedItem().toString());
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			input.add(filePath, xIndex, yIndex, separator);

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
			Application.getActiveApplication().getOutput().output(e.getMessage());
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
