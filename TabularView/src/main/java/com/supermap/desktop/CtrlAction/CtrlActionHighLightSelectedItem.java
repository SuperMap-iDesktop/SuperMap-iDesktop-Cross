package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.GeometryPropertyBindWindow.BindUtilties;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionHighLightSelectedItem extends CtrlAction {
	public CtrlActionHighLightSelectedItem(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		if (Application.getActiveApplication().getActiveForm() instanceof IFormTabular) {
			BindUtilties.queryMap((IFormTabular) Application.getActiveApplication().getActiveForm());
		}
	}

	@Override
	public boolean enable() {
		return BindUtilties.isFormInBind(Application.getActiveApplication().getActiveForm());
	}
}
