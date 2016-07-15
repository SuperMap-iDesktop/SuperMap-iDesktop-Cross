package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.CursorUtilities;

public class CtrlActionDownLoadHDFS extends CtrlAction {

	public CtrlActionDownLoadHDFS(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormLBSControl control = (IFormLBSControl) Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
			control.downLoad();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm()
				&& Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm() instanceof IFormLBSControl) {
			enable = true;
		}
		return enable;
	}

}
