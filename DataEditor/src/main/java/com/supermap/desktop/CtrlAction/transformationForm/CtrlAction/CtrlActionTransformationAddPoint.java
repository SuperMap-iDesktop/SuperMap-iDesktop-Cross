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
public class CtrlActionTransformationAddPoint extends CtrlAction {

	public CtrlActionTransformationAddPoint(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm.getWindowType() != WindowType.TRANSFORMATION) {
			return;
		}
		IFormTransformation formTransformation = (IFormTransformation) activeForm;
		if (!formTransformation.isAddPointing()) {
			formTransformation.startAddPoint();
		}
	}


	@Override
	public boolean enable() {
		return Application.getActiveApplication().getActiveForm().getWindowType() == WindowType.TRANSFORMATION;
	}

}
