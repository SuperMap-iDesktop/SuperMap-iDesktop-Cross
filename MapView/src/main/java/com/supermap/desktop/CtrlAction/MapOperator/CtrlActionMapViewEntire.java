package com.supermap.desktop.CtrlAction.MapOperator;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionMapViewEntire extends CtrlAction {

	public CtrlActionMapViewEntire(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap)Application.getActiveApplication().getActiveForm();
			if (formMap != null) {
				formMap.getMapControl().getMap().viewEntire();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
