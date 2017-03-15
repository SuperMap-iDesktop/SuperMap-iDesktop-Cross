package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.DiglogMapOutputPicture;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author YuanR
 */
public class CtrlActionMapOutputPicture extends CtrlAction {
	public CtrlActionMapOutputPicture(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		DiglogMapOutputPicture diglogMapOutputPicture = new DiglogMapOutputPicture();
		diglogMapOutputPicture.showDialog();
	}

	@Override
	public boolean enable() {
		return true;
	}
}
