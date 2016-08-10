package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;

public class CtrlActionLogin extends CtrlAction {
	
	public CtrlActionLogin(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		UICommonToolkit.showMessageDialog("Show Login Window");
	}

	@Override
	public boolean enable() {
		// TODO Auto-generated method stub
		return true;
	}

}
