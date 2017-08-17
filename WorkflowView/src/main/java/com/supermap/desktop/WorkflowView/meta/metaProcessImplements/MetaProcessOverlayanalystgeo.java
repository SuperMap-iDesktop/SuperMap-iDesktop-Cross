package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
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
import com.supermap.desktop.process.parameter.interfaces.datas.types.BasicTypes;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.DefaultOpenServerMap;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.util.concurrent.CancellationException;

/**
 * 矢量裁剪分析
 *
 * @author XiaJT
 */
public class MetaProcessOverlayanalystgeo extends MetaProcess {

	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	ParameterInputDataType parameterInputDataType = new ParameterInputDataType();
	private ParameterBigDatasourceDatasource parameterOverlayDatasource;
	private ParameterSingleDataset parameterOverlayDataset;
	private ParameterComboBox parameterOverlayTypeComboBox;

	public MetaProcessOverlayanalystgeo() {
		initComponents();
		initComponentState();
		initConstraint();
		initListener();
	}

	private void initComponents() {
		parameterOverlayDatasource = new ParameterBigDatasourceDatasource();
		parameterOverlayDatasource.setDescribe(CommonProperties.getString("String_Label_Datasource"));
		parameterOverlayDataset = new ParameterSingleDataset(DatasetType.REGION);
		parameterOverlayDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));

		parameterOverlayTypeComboBox = new ParameterComboBox(CoreProperties.getString("String_OverlayAnalystType"));
		parameterOverlayTypeComboBox.setItems(
				new ParameterDataNode(CoreProperties.getString("String_Clip"), "clip"),
				new ParameterDataNode(CoreProperties.getString("String_Intersect"), "intersect")
		);

		ParameterCombine parameterCombineOverlay = new ParameterCombine();
		parameterCombineOverlay.setDescribe(CommonProperties.getString("String_clipDataset"));
		parameterCombineOverlay.addParameters(parameterOverlayDatasource, parameterOverlayDataset);
		ParameterCombine parameterCombineSetting = new ParameterCombine();
		parameterCombineSetting.setDescribe(ProcessProperties.getString("String_AnalystSet"));
		parameterCombineSetting.addParameters(parameterOverlayTypeComboBox);

		parameters.addParameters(parameterIServerLogin, parameterInputDataType, parameterCombineOverlay, parameterCombineSetting);
		parameters.addInputParameters("overlay", Type.UNKOWN, parameterCombineOverlay);// 缺少对应的类型
		parameters.addOutputParameters("OverlayResult", BasicTypes.STRING, null);
	}

	private void initComponentState() {
		parameterInputDataType.setSupportDatasetType(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		Dataset defaultBigDataStoreDataset = DatasetUtilities.getDefaultBigDataStoreDataset();
		if (defaultBigDataStoreDataset != null) {
			parameterOverlayDatasource.setSelectedItem(defaultBigDataStoreDataset.getDatasource());
			parameterOverlayDataset.setSelectedItem(defaultBigDataStoreDataset);

		}
	}

	private void initConstraint() {
		EqualDatasourceConstraint equalOverlayDatasource = new EqualDatasourceConstraint();
		equalOverlayDatasource.constrained(parameterOverlayDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalOverlayDatasource.constrained(parameterOverlayDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void initListener() {

	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_overlayanaly");
	}

	@Override
	public boolean execute() {
		boolean isSuccess;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			IServerService service = parameterIServerLogin.login();
			CommonSettingCombine input = new CommonSettingCombine("input", "");
			parameterInputDataType.initSourceInput(input);
			Dataset overlayDataset = parameterOverlayDataset.getSelectedDataset();
			CommonSettingCombine datasetOverlay = new CommonSettingCombine("datasetOverlay", overlayDataset.getName());
			CommonSettingCombine mode = new CommonSettingCombine("mode", (String) parameterOverlayTypeComboBox.getSelectedData());
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			analyst.add(datasetOverlay, mode);

			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input, analyst);
			JobResultResponse response = service.queryResult(MetaKeys.OVERLAYANALYSTGEO, commonSettingCombine.getFinalJSon());
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
						fireRunning(new RunningEvent(MetaProcessOverlayanalystgeo.this, percent, message, -1));
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
			parameters.getOutputs().getData("OverlayResult").setValue("");// TODO: 2017/6/26 也许没结果,but
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
		return MetaKeys.OVERLAYANALYSTGEO;
	}
}
