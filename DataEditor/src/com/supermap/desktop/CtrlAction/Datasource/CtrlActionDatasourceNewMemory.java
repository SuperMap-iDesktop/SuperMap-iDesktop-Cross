package com.supermap.desktop.CtrlAction.Datasource;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionDatasourceNewMemory extends CtrlAction {

	public CtrlActionDatasourceNewMemory(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			JDialogDatasourceNewMemory dialog = new JDialogDatasourceNewMemory();
			dialog.setVisible(true);			
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}

}