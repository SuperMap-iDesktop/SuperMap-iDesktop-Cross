package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.CtrlAction.settings.JDialogSettings;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionSettings extends CtrlAction {
	public CtrlActionSettings(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		new JDialogSettings().showDialog();
	}

	@Override
	public boolean enable() {
		return super.enable();
	}
}
