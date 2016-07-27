package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.CursorUtilities;

public class CtrlActionDeleteHDFS extends CtrlAction {

	public CtrlActionDeleteHDFS(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormLBSControl control = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
			control.delete();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormLBSControl) {
			enable = true;
		}
		return enable;
	}

}