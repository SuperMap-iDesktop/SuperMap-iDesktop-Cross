package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.ColorSchemeDialogs.JDialogColorScheme;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionColorScheme extends CtrlAction {
	public CtrlActionColorScheme(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogColorScheme jDialogColorScheme = new JDialogColorScheme();
		jDialogColorScheme.showDialog();
		jDialogColorScheme.dispose();
	}

	@Override
	public boolean enable() {
		return true;
	}
}
