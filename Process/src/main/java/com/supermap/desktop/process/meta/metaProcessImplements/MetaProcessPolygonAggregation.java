package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.messageBus.NewMessageBus;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.process.util.TaskUtil;
import com.supermap.desktop.ui.lbs.Interface.IServerService;
import com.supermap.desktop.ui.lbs.params.CommonSettingCombine;
import com.supermap.desktop.ui.lbs.params.JobResultResponse;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;

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
			CommonSettingCombine filePath = new CommonSettingCombine("filePath",parameterHDFSPath.getSelectedItem().toString());
			CommonSettingCombine xIndex = new CommonSettingCombine("xIndex",parameterTextFieldXIndex.getSelectedItem().toString());
			CommonSettingCombine yIndex = new CommonSettingCombine("yIndex",parameterTextFieldYIndex.getSelectedItem().toString());
			CommonSettingCombine separator = new CommonSettingCombine("separator",parameterTextFieldSeparator.getSelectedItem().toString());
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			input.add(filePath,xIndex,yIndex,separator);

			CommonSettingCombine fields = new CommonSettingCombine("fields",parameterWeightIndex.getSelectedItem().toString());
			CommonSettingCombine statisticModes = new CommonSettingCombine("statisticModes",parameterStaticModel.getSelectedItem().toString());
			CommonSettingCombine regionDataset = new CommonSettingCombine("regionDataset",dataset.getName());
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			analyst.add(fields,statisticModes,regionDataset);

			CommonSettingCombine type = new CommonSettingCombine("type",parameterAggregationType.getSelectedData().toString());
			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input,analyst,type);

			JobResultResponse response = service.queryResult(MetaKeys.POLYGON_AGGREGATION,commonSettingCombine.getFinalJSon());
			CursorUtilities.setWaitCursor();
			if (null != response) {
				ProcessTask task = TaskUtil.getTask(this);
				NewMessageBus messageBus = new NewMessageBus(response, task);
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
