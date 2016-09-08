package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.ui.Action;

/**
 * @author XiaJT
 */
public class CtrlActionTransformationPan extends CtrlAction {
	public CtrlActionTransformationPan(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm != null && activeForm instanceof IFormTransformation) {
			((IFormTransformation) activeForm).setAction(Action.PAN);
		}
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		return activeForm != null && activeForm instanceof IFormTransformation;
	}
}
