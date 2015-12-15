package com.supermap.desktop.CtrlAction;

import java.awt.HeadlessException;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionDatasourceClose extends CtrlAction {

	public CtrlActionDatasourceClose(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			if (Application.getActiveApplication().getActiveDatasources().length > 0) {
				CtrlActionDatasourceCloseSelected.close();
			} else {
				CtrlActionDatasourcesCloseAll.close();
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