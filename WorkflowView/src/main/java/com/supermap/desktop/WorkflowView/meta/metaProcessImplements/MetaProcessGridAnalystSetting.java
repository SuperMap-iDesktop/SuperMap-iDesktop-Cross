package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.desktop.WorkflowView.WorkflowViewProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
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
	protected String getCOMPLETED_MESSAGE() {
		return WorkflowViewProperties.getString("String_SetGridAnalystSetSuccess");
	}
	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Setting_Title");
	}

	@Override
	public boolean execute() {
		parameterGridAnalystSetting.run();
		getOutputs().getData("GridAnalystSetting").setValue(parameterGridAnalystSetting.getResult());
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
