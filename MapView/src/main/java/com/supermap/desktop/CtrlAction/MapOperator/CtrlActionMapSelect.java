package com.supermap.desktop.CtrlAction.MapOperator;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action;

public class CtrlActionMapSelect extends CtrlActionMapActionBase {

	public CtrlActionMapSelect(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public Action getAction() {
		return Action.SELECT2;
	}
}
