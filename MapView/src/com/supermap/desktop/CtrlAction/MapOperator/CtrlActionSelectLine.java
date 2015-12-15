package com.supermap.desktop.CtrlAction.MapOperator;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action;

public class CtrlActionSelectLine extends CtrlActionMapActionBase {

	public CtrlActionSelectLine(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public Action getAction() {
		return Action.SELECTLINE;
	}
}
