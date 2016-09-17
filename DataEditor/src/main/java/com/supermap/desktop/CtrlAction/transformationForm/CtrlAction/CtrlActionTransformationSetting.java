package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.Dialogs.JDialogTransformationSetting;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionTransformationSetting extends CtrlAction {
	public CtrlActionTransformationSetting(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		new JDialogTransformationSetting();
	}

	@Override
	public boolean enable() {
		return Application.getActiveApplication().getActiveForm().getWindowType() == WindowType.TRANSFORMATION;
	}
}
