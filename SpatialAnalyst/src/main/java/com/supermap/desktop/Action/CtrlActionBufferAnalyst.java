package com.supermap.desktop.Action;

import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.spatialanalyst.vectoranalyst.BufferDialog;

public class CtrlActionBufferAnalyst extends CtrlAction {

	public CtrlActionBufferAnalyst(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	@Override
	public void run() {
		new BufferDialog();
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != Application.getActiveApplication().getWorkspace().getDatasources() && Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			for (int i = 0; i < datasources.getCount(); i++) {
				Datasource tempDatasource = datasources.get(i);
				if (!tempDatasource.isReadOnly()) {
					enable = true;
					break;
				}
			}
		}

		return enable;
	}


}
