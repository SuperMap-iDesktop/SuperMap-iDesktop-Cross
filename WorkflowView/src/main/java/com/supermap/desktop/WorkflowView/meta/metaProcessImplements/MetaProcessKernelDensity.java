package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.lbs.Interface.IServerService;
import com.supermap.desktop.lbs.params.CommonSettingCombine;
import com.supermap.desktop.lbs.params.JobResultResponse;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.messageBus.NewMessageBus;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterIServerLogin;
import com.supermap.desktop.process.parameter.ipls.ParameterInputDataType;
import com.supermap.desktop.process.parameter.ipls.ParameterTextField;
import com.supermap.desktop.process.parameters.ParameterPanels.DefaultOpenServerMap;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.CursorUtilities;

import java.util.concurrent.CancellationException;


/**
 * Created by xie on 2017/2/10.
 */
public class MetaProcessKernelDensity extends MetaProcess {
	private ParameterIServerLogin parameterIServerLogin = new ParameterIServerLogin();
	ParameterInputDataType parameterInputDataType = new ParameterInputDataType();
	private ParameterComboBox parameterComboBoxAnalyseType = new ParameterComboBox(ProcessProperties.getString("String_AnalyseType"));
	private ParameterComboBox parameterComboBoxMeshType = new ParameterComboBox(ProcessProperties.getString("String_MeshType"));
	private ParameterTextField parameterIndex = new ParameterTextField(ProcessProperties.getString("String_Index"));
	private ParameterTextField parameterBounds= new ParameterTextField(ProcessProperties.getString("String_AnalystBounds"));
	private ParameterTextField parameterMeshSize = new ParameterTextField(ProcessProperties.getString("String_MeshSize"));
	private ParameterComboBox parameterMeshSizeUnit = new ParameterComboBox(ProcessProperties.getString("String_MeshSizeUnit"));
	private ParameterTextField parameterRadius= new ParameterTextField(ProcessProperties.getString("String_Radius"));
	private ParameterComboBox parameterRadiusUnit= new ParameterComboBox(ProcessProperties.getString("String_RadiusUnit"));
	private ParameterComboBox parameterAreaUnit= new ParameterComboBox(ProcessProperties.getString("String_AreaUnit"));


	public MetaProcessKernelDensity() {
		initComponents();
	}

	private void initComponents() {
		parameterInputDataType.setSupportDatasetType(DatasetType.POINT);
		ParameterDataNode parameterDataNode = new ParameterDataNode(ProcessProperties.getString("String_KernelDensity"), "1");
		parameterComboBoxAnalyseType.setItems(parameterDataNode);
		parameterComboBoxAnalyseType.setSelectedItem(parameterDataNode);
		parameterComboBoxMeshType.setItems(new ParameterDataNode(ProcessProperties.getString("String_QuadrilateralMesh"), "0"),
				new ParameterDataNode(ProcessProperties.getString("String_HexagonalMesh"), "1"));

		//流程图中不支持在地图中绘制范围，范围表示与iServer的表示相同
		parameterBounds.setSelectedItem("-74.050,40.650,-73.850,40.850");
		parameterIndex.setSelectedItem("col7,col8");
		parameterMeshSize.setSelectedItem("10");
		parameterMeshSizeUnit.setItems(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Meter"), "Meter"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Kilometer"), "Kilometer"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Yard"), "Yard"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Foot"), "Foot"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Mile"), "Mile")
		);
		parameterRadius.setSelectedItem("100");
		parameterRadiusUnit.setItems(new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Meter"), "Meter"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Kilometer"), "Kilometer"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Yard"), "Yard"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Foot"), "Foot"),
				new ParameterDataNode(CommonProperties.getString("String_DistanceUnit_Mile"), "Mile")
		);
		parameterAreaUnit.setItems(new ParameterDataNode(CommonProperties.getString("String_AreaUnit_Meter"), "SquareMeter"),
				new ParameterDataNode(CommonProperties.getString("String_AreaUnit_Kilometer"), "SquareKiloMeter"),
				new ParameterDataNode(CommonProperties.getString("String_AreaUnit_Hectare"), "Hectare"),
				new ParameterDataNode(CommonProperties.getString("String_AreaUnit_Are"), "Are"),
				new ParameterDataNode(CommonProperties.getString("String_AreaUnit_Acre"), "Acre"),
				new ParameterDataNode(CommonProperties.getString("String_AreaUnit_Foot"), "SquareFoot"),
				new ParameterDataNode(CommonProperties.getString("String_AreaUnit_Yard"), "SquareYard"),
				new ParameterDataNode(CommonProperties.getString("String_AreaUnit_Mile"), "SquareMile")
		);
		ParameterCombine parameterCombineAlaysis = new ParameterCombine();
		parameterCombineAlaysis.setDescribe(ProcessProperties.getString("String_AnalystSet"));
		parameterCombineAlaysis.addParameters(parameterComboBoxAnalyseType,
				parameterComboBoxMeshType,
				parameterIndex,
				parameterBounds,
				parameterMeshSize,
				parameterMeshSizeUnit,
				parameterRadius,
				parameterRadiusUnit,
				parameterAreaUnit
		);
		parameters.setParameters(
				parameterIServerLogin,
				parameterInputDataType,
				parameterCombineAlaysis
		);
		parameters.getOutputs().addData("KernelDensityResult", Type.UNKOWN);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_KernelDensityAnalyst");
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


			parameterInputDataType.initSourceInput(input);


			CommonSettingCombine method = new CommonSettingCombine("method",(String) parameterComboBoxAnalyseType.getSelectedData());
			CommonSettingCombine meshType = new CommonSettingCombine("meshType",(String) parameterComboBoxMeshType.getSelectedData());
			CommonSettingCombine fields = new CommonSettingCombine("fields",(String) parameterIndex.getSelectedItem());
			CommonSettingCombine query = new CommonSettingCombine("query",parameterBounds.getSelectedItem().toString());
			CommonSettingCombine resolution = new CommonSettingCombine("resolution",parameterMeshSize.getSelectedItem().toString());
			CommonSettingCombine meshSizeUnit = new CommonSettingCombine("meshSizeUnit",(String)parameterMeshSizeUnit.getSelectedData());
			CommonSettingCombine radius = new CommonSettingCombine("radius",parameterRadius.getSelectedItem().toString());
			CommonSettingCombine radiusUnit = new CommonSettingCombine("radiusUnit",(String)parameterRadiusUnit.getSelectedData());
			CommonSettingCombine areaUnit = new CommonSettingCombine("areaUnit",(String)parameterAreaUnit.getSelectedData());
			CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
			analyst.add(method,meshType,fields,query,resolution,meshSizeUnit,radius,radiusUnit,areaUnit);

			CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
			commonSettingCombine.add(input, analyst);
			JobResultResponse response = service.queryResult(MetaKeys.KERNEL_DENSITY, commonSettingCombine.getFinalJSon());
			CursorUtilities.setWaitCursor();
			if (null != response) {
				CursorUtilities.setDefaultCursor();
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
						fireRunning(new RunningEvent(MetaProcessKernelDensity.this, percent, message, -1));
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
			parameters.getOutputs().getData("KernelDensityResult").setValue("");// // TODO: 2017/5/26
			CursorUtilities.setDefaultCursor();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.KERNEL_DENSITY;
	}

}
