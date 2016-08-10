package com.supermap.desktop.CtrlAction.MapOperator;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action;

public class CtrlActionSelectCircle extends CtrlActionMapActionBase {

	public CtrlActionSelectCircle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public Action getAction() {
		return Action.SELECTCIRCLE;
	}
}
