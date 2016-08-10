package com.supermap.desktop.CtrlAction.MapOperator;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action;

public class CtrlActionZoomOut extends CtrlActionMapActionBase {

	public CtrlActionZoomOut(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public Action getAction() {
		return Action.ZOOMOUT;
	}
}
