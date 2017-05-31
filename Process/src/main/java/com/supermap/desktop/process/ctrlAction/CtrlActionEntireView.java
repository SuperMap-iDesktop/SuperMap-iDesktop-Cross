package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormWorkflow;

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
