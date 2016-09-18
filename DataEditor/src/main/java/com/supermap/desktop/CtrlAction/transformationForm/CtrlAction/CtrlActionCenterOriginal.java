package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionCenterOriginal extends CtrlAction {

	public CtrlActionCenterOriginal(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm != null && activeForm instanceof IFormTransformation) {
			((IFormTransformation) activeForm).centerOriginal();
		}
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm == null || activeForm.getWindowType() != WindowType.TRANSFORMATION) {
			return false;
		}
		return ((IFormTransformation) activeForm).getTable().getSelectedRows().length > 0;
	}
}
