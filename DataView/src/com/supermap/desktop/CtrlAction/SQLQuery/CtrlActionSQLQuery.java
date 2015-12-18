package com.supermap.desktop.CtrlAction.SQLQuery;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionSQLQuery extends CtrlAction{

	public CtrlActionSQLQuery(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogSQLQuery dialogSQLQuery = new JDialogSQLQuery();
		dialogSQLQuery.showDialog();
	}

	@Override
	public boolean enable() {
		return true;
	}
}
