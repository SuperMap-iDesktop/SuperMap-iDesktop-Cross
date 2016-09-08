package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionDeleteTransformItems extends CtrlAction {
	public CtrlActionDeleteTransformItems(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm != null && activeForm instanceof IFormTransformation) {
			((IFormTransformation) activeForm).deleteTableSelectedRow();
		}
	}

	@Override
	public boolean enable() {
		boolean result = false;
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm != null && activeForm instanceof IFormTransformation) {
			if (((IFormTransformation) activeForm).getTable().getSelectedRowCount() > 0) {
				result = true;
			}
		}
		return result;
	}
}
