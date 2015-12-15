package com.supermap.desktop.CtrlAction.MapOperator;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action;

public class CtrlActionSelectRegion extends CtrlActionMapActionBase {

	public CtrlActionSelectRegion(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public Action getAction() {
		return Action.SELECTREGION;
	}
}
