package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormProcess;
import com.supermap.desktop.process.graphics.GraphCanvas;

/**
 * Created by highsad on 2017/2/28.
 */
public class CtrlActionRun extends CtrlAction {
	public CtrlActionRun(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm form = Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
		if (form instanceof FormProcess) {
			GraphCanvas canvas = ((FormProcess) form).getCanvas();

		}
	}
}
