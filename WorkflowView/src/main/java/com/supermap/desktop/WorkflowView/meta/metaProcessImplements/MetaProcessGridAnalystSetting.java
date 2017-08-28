package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.desktop.WorkflowView.WorkflowViewProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.parameter.ipls.ParameterGridAnalystSetting;

/**
 * @author XiaJT
 */
public class MetaProcessGridAnalystSetting extends MetaProcess {

	private ParameterGridAnalystSetting parameterGridAnalystSetting;

	public MetaProcessGridAnalystSetting() {
		parameterGridAnalystSetting = new ParameterGridAnalystSetting();
		getParameters().addParameters(parameterGridAnalystSetting);
		getParameters().addOutputParameters("GridAnalystSetting", ProcessProperties.getString("String_GridAnalystSetting"), Type.UNKOWN, parameterGridAnalystSetting);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Setting_Title");
	}

	@Override
	public boolean execute() {
		fireRunning(new RunningEvent(this, 0, WorkflowViewProperties.getString("String_GridAnalystSetStart")));
		parameterGridAnalystSetting.run();
		getOutputs().getData("GridAnalystSetting").setValue(parameterGridAnalystSetting.getResult());
		fireRunning(new RunningEvent(this, 100, WorkflowViewProperties.getString("String_SetGridAnalystSetSuccess")));
		return true;
	}

	@Override
	protected boolean isReadyHook() {
//		parameterGridAnalystSetting.run();
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.GRID_ANALYST_SETTING;
	}
}
