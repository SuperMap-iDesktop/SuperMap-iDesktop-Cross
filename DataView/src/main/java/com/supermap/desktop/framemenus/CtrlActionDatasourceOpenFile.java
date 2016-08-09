package com.supermap.desktop.framemenus;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.utilities.DatasourceOpenFileUtilties;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionDatasourceOpenFile extends CtrlAction {


	public CtrlActionDatasourceOpenFile(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			DatasourceOpenFileUtilties.datasourceOpenFile();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}
