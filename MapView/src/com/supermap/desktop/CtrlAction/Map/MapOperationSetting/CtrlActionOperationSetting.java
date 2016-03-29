package com.supermap.desktop.CtrlAction.Map.MapOperationSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;


/**
 * 地图操作设置
 */
public class CtrlActionOperationSetting extends CtrlAction {
	public CtrlActionOperationSetting(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogOperationSetting dialog = new JDialogOperationSetting();
		dialog.showDialog();
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		return activeForm instanceof IFormMap;
	}
}
