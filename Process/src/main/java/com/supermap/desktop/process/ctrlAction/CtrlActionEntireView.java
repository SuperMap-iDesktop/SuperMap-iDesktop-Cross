package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormProcess;

/**
 * Created by highsad on 2017/4/25.
 */
public class CtrlActionEntireView extends CtrlAction {

	public CtrlActionEntireView(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		FormProcess formProcess = (FormProcess) Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
		formProcess.getCanvas().entireView();
	}
}
