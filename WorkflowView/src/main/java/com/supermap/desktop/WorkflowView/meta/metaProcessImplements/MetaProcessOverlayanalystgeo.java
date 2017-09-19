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
import com.supermap.desktop.process.parameter.interfaces.datas.types.BasicTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterIServerLogin;
import com.supermap.desktop.process.parameter.ipls.ParameterInputDataType;
import com.supermap.desktop.process.parameters.ParameterPanels.DefaultOpenServerMap;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.CursorUtilities;

/**
 * 矢量裁剪分析
 *
 * @author XiaJT
 */
public class MetaProcessOverlayanalystgeo extends MetaProcess {

	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	private ParameterInputDataType parameterInputDataType = new ParameterInputDataType();
	private ParameterInputDataType parameterClipDataType = new ParameterInputDataType();
	private ParameterComboBox parameterOverlayTypeComboBox;

	public MetaProcessOverlayanalystgeo() {
		initComponents();
		initComponentState();
	}

	private void initComponents() {
		//设置输入数据
		parameterInputDataType.setDescribe(ProcessProperties.getString("String_FileInputPath"));
		parameterIServerLogin.setInputDataType(this.parameterInputDataType);
		//设置裁剪数据
		parameterClipDataType.setDescribe(CommonProperties.getString("String_clipDataset"));
		parameterIServerLogin.setAnalystDataType(this.parameterClipDataType);
		//设置分析参数
		parameterOverlayTypeComboBox = new ParameterComboBox(CoreProperties.getString("String_OverlayAnalystType"));
		parameterOverlayTypeComboBox.setRequisite(true);
		parameterOverlayTypeComboBox.setItems(
				new ParameterDataNode(CoreProperties.getString("String_Clip"), "clip"),
				new ParameterDataNode(CoreProperties.getString("String_Intersect"), "intersect")
		);

		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_AnalystSet"));
		parameterCombineSetting.addParameters(parameterOverlayTypeComboBox);

		parameters.addParameters(parameterIServerLogin, parameterInputDataType, parameterClipDataType, parameterCombineSetting);
		//parameters.addInputParameters("overlay", Type.UNKOWN, parameterCombineOverlay);// 缺少对应的类型
		parameters.addOutputParameters("OverlayResult", ProcessOutputResultProperties.getString("String_VectorAnalysisResult"), BasicTypes.STRING);
	}

	private void initComponentState() {
		parameterClipDataType.parameterDataInputWay.removeAllItems();
		parameterClipDataType.parameterDataInputWay.setItems(new ParameterDataNode(ProcessProperties.getString("String_BigDataStore"), "3"),
				new ParameterDataNode(ProcessProperties.getString("String_UDBFile"), "1"), new ParameterDataNode(ProcessProperties.getString("String_PG"), "2"));
		parameterClipDataType.parameterSwitch.switchParameter("3");
		parameterClipDataType.setBool(true);
		parameterInputDataType.setSupportDatasetType(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		parameterClipDataType.setSupportDatasetType(DatasetType.REGION);
		parameterIServerLogin.setDataType(parameterInputDataType.supportDatasetType);
		parameterIServerLogin.setAnalystDatasetTypes(parameterClipDataType.supportDatasetType);
	}


	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_overlayanaly");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful;
		try {
			fireRunning(new RunningEvent(this, ProcessProperties.getString("String_Running")));
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			parameterInputDataType.initSourceInput(input);

			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			parameterClipDataType.initAnalystInput(analyst, 0);
			CommonSettingCombine mode = new CommonSettingCombine("mode", (String) parameterOverlayTypeComboBox.getSelectedData());
			analyst.add(mode);

			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input, analyst);
			JobResultResponse response = parameterIServerLogin.getService().queryResult(MetaKeys.OVERLAYANALYSTGEO, commonSettingCombine.getFinalJSon());
			CursorUtilities.setWaitCursor();
			if (null != response) {
				NewMessageBus messageBus = new NewMessageBus(response, DefaultOpenServerMap.INSTANCE);
				isSuccessful = messageBus.run();
			} else {
				isSuccessful = false;
			}
			parameters.getOutputs().getData("OverlayResult").setValue("");// TODO: 2017/6/26 也许没结果,but
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
		return MetaKeys.OVERLAYANALYSTGEO;
	}
}
