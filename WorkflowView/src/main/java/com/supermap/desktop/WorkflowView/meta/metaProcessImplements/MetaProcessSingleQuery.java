package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
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
import com.supermap.desktop.process.parameter.interfaces.datas.types.BasicTypes;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.DefaultOpenServerMap;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * @author XiaJT
 */
public class MetaProcessSingleQuery extends MetaProcess {

	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	private ParameterInputDataType parameterInputDataType = new ParameterInputDataType();
	private ParameterInputDataType parameterClipDataType = new ParameterInputDataType();
	private ParameterComboBox parameterQueryTypeComboBox;

	public MetaProcessSingleQuery() {
		initComponents();
		initComponentState();
	}

	private void initComponents() {
		//设置输入数据
		parameterInputDataType.setDescribe(ProcessProperties.getString("String_FileInputPath"));
		parameterIServerLogin.setInputDataType(this.parameterInputDataType);
		//设置查询数据
		parameterClipDataType.setDescribe(ProcessProperties.getString("String_QueryData"));
		parameterIServerLogin.setAnalystDataType(this.parameterClipDataType);
        //设置分析参数
		parameterQueryTypeComboBox = new ParameterComboBox(CoreProperties.getString("String_AnalystType"));
		parameterQueryTypeComboBox.setRequisite(true);
		parameterQueryTypeComboBox.setItems(
				new ParameterDataNode(CoreProperties.getString("String_SpatialQuery_ContainCHS"), "CONTAIN"),
				new ParameterDataNode(CoreProperties.getString("String_SpatialQuery_CrossCHS"), "CROSS"),
				new ParameterDataNode(CoreProperties.getString("String_SpatialQuery_DisjointCHS"), "DISJOINT"),
				new ParameterDataNode(CoreProperties.getString("String_SpatialQuery_IdentityCHS"), "IDENTITY"),
				new ParameterDataNode(CoreProperties.getString("String_SpatialQuery_IntersectCHS"), "INTERSECT"),
				new ParameterDataNode(CoreProperties.getString("String_None"), "NONE"),
				new ParameterDataNode(CoreProperties.getString("String_SpatialQuery_OverlapCHS"), "OVERLAP"),
				new ParameterDataNode(CoreProperties.getString("String_SpatialQuery_TouchCHS"), "TOUCH"),
				new ParameterDataNode(CoreProperties.getString("String_SpatialQuery_WithinCHS"), "WITHIN")
		);
		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_AnalystSet"));
		parameterCombineSetting.addParameters(parameterQueryTypeComboBox);

		parameters.addParameters(parameterIServerLogin, parameterInputDataType, parameterClipDataType, parameterCombineSetting);
		//parameters.addInputParameters("Query", Type.UNKOWN, parameterCombineQuery);// 缺少对应的类型
		parameters.addOutputParameters("QueryResult", ProcessOutputResultProperties.getString("String_SingleDogQueryResult"), BasicTypes.STRING);
	}

	private void initComponentState() {
		parameterClipDataType.parameterDataInputWay.removeAllItems();
		parameterClipDataType.parameterDataInputWay.setItems(new ParameterDataNode(ProcessProperties.getString("String_BigDataStore"), "3"),
				new ParameterDataNode(ProcessProperties.getString("String_UDBFile"), "1"), new ParameterDataNode(ProcessProperties.getString("String_PG"), "2"));
		parameterClipDataType.parameterSwitch.switchParameter("3");
		parameterClipDataType.setBool(true);
		parameterInputDataType.setSupportDatasetType(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		parameterClipDataType.setSupportDatasetType(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		parameterIServerLogin.setDataType(parameterInputDataType.supportDatasetType);
		parameterIServerLogin.setAnalystDatasetTypes(parameterClipDataType.supportDatasetType);
	}


	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_SingleQuery");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful;
		try {
			fireRunning(new RunningEvent(this, ProcessProperties.getString("String_Running")));
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			parameterInputDataType.initSourceInput(input);
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			parameterClipDataType.initAnalystInput(analyst, 1);
			CommonSettingCombine mode = new CommonSettingCombine("mode", (String) parameterQueryTypeComboBox.getSelectedData());
			analyst.add(mode);
			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input, analyst);
			CursorUtilities.setWaitCursor();
			JobResultResponse response = parameterIServerLogin.getService().queryResult(MetaKeys.SINGLE_QUERY, commonSettingCombine.getFinalJSon());
			if (null != response) {
				NewMessageBus messageBus = new NewMessageBus(response, DefaultOpenServerMap.INSTANCE);
				isSuccessful = messageBus.run();
			} else {
				isSuccessful = false;
			}

			parameters.getOutputs().getData("QueryResult").setValue("");
		} catch (Exception e) {
			isSuccessful = false;
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			CursorUtilities.setDefaultCursor();
		}

		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.SINGLE_QUERY;
	}
}
