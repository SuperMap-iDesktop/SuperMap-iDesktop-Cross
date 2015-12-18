package com.supermap.desktop.CtrlAction.CreateGeometry;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action;

public class CtrlActionCreateRoundRectangle extends ActionCreateBase {

	public CtrlActionCreateRoundRectangle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	public Action getAction() {
		return Action.CREATE_ROUND_RECTANGLE;
	}
}
