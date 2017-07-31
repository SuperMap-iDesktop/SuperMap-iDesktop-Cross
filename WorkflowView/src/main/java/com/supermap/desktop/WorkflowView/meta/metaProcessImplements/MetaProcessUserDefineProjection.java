package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;

/**
 * @author XiaJT
 */
public class MetaProcessUserDefineProjection extends MetaProcess {


	@Override
	public String getTitle() {
		return ProcessProperties.getString("UserDefineProjection");
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public String getKey() {
		return MetaKeys.USER_DEFINE_PROJECTION;
	}
}
