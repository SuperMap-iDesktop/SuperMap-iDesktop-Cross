package com.supermap.desktop.framemenus;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.WorkspaceUtilities;

public class CtrlActionWorkspaceClose extends CtrlAction {

	public CtrlActionWorkspaceClose(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// 缺省实现
	}

	@Override
	public void run() {
		try {
			WorkspaceUtilities.closeWorkspace();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}
