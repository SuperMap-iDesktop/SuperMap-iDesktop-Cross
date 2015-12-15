package com.supermap.desktop.CtrlAction;

import java.io.IOException;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DockbarManager;

public class CtrlActionWindowsHorizontal extends CtrlAction {
	
	public CtrlActionWindowsHorizontal(IBaseItem caller, IForm formClass) {
		super (caller, formClass);
	}

	@Override
	public void run() {
		// 未实现
	}

	@Override
	public boolean enable() {
		return true;
	}
}
