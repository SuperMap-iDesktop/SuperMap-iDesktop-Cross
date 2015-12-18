package com.supermap.desktop.CtrlAction.CreateGeometry;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action;

public class CtrlActionCreateCircle extends ActionCreateBase {

	public CtrlActionCreateCircle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	public Action getAction() {
		return Action.CREATECIRCLE;
	}
}
