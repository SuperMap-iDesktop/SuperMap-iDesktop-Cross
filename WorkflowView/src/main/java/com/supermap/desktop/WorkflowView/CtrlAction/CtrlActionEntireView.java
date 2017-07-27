package com.supermap.desktop.WorkflowView.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.WorkflowView.FormWorkflow;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by highsad on 2017/4/25.
 */
public class CtrlActionEntireView extends CtrlAction {

	public CtrlActionEntireView(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		FormWorkflow formWorkflow = (FormWorkflow) Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
		formWorkflow.getCanvas().entireView();
	}
}
