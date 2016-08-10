package com.supermap.desktop.CtrlAction.MapOperator;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.ui.Action;

abstract public class CtrlActionMapActionBase extends CtrlAction {

	public CtrlActionMapActionBase(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public Action getAction() {
		return Action.NULL;
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			formMap.getMapControl().setAction(this.getAction());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}

	@Override
	public boolean check() {
		boolean check = false;

		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (formMap != null && formMap.getMapControl().getAction() == this.getAction()) {
				check = true;
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return check;
	}
}
