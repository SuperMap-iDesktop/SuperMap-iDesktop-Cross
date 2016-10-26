package com.supermap.desktop.CtrlAction;

import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionDatasourceClose extends CtrlAction {

	public CtrlActionDatasourceClose(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			if (Application.getActiveApplication().getActiveDatasources().length > 0) {
				CtrlActionDatasourcesCloseAll.close(Application.getActiveApplication().getActiveDatasources());
			} else {
				Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
				Datasource[] datasources1 = new Datasource[datasources.getCount()];
				for (int i = 0; i < datasources1.length; i++) {
					datasources1[i] = datasources.get(i);
				}
				CtrlActionDatasourcesCloseAll.close(datasources1);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getActiveDatasources().length > 0) {
			enable = true;
		} else {
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			if (datasources.getCount() > 0) {
				enable = true;
			}
		}
		return enable;
	}

}