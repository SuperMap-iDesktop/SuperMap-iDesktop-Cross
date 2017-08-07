package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridDistance;

import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

/**
 * Created by yuanR on 2017/8/7 0007.
 */
public class MetaProcessSurfacePathLine extends MetaProcess {

	public MetaProcessSurfacePathLine() {
	}


	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		return isSuccessful;
	}


	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.SURFACE_PATH_LINE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_SurfacePathLine");
	}
}
