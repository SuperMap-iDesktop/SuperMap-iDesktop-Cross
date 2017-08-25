package com.supermap.desktop.WorkflowView.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionWorkflowSaveAs extends CtrlAction {
	public CtrlActionWorkflowSaveAs(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		Application.getActiveApplication().getActiveForm().saveAs(false);
	}

	@Override
	public boolean enable() {
		return true;
	}
}
