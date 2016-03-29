package com.supermap.desktop.CtrlAction.Map.MapMeasure;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * 量算设置
 */
public class CtrlActionMeasureSetting extends CtrlAction {
	public CtrlActionMeasureSetting(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogMeasureSetting jDialogMeasureSetting = new JDialogMeasureSetting();
		jDialogMeasureSetting.showDialog();
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		return activeForm instanceof FormMap;
	}
}
