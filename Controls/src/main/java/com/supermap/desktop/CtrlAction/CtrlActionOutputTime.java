package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.OutputFrame;

public class CtrlActionOutputTime extends CtrlAction {

	public CtrlActionOutputTime(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		((OutputFrame) Application.getActiveApplication().getOutput()).timeShowStateChange();
	}

	@Override
	public boolean enable() {
		return true;
	}
	
	@Override
	public boolean check(){
		return ((OutputFrame) Application.getActiveApplication().getOutput()).isShowTime();
	}
}
