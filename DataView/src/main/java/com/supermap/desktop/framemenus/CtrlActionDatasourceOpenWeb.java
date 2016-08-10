package com.supermap.desktop.framemenus;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DatasourceOperatorType;
import com.supermap.desktop.ui.controls.JDialogDatasourceOpenAndNew;

import javax.swing.*;

public class CtrlActionDatasourceOpenWeb extends CtrlAction {

	public CtrlActionDatasourceOpenWeb(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			JFrame owner = (JFrame)Application.getActiveApplication().getMainFrame();
			JDialogDatasourceOpenAndNew dialog = new JDialogDatasourceOpenAndNew(owner, DatasourceOperatorType.OPENWEB);
			dialog.setVisible(true);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		// TODO Auto-generated method stub
		return true;
	}

}