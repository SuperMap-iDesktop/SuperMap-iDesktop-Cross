package com.supermap.desktop.popupmenus;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.DataImportFrame;

import javax.swing.*;

public class CtrlActionDataImport extends CtrlAction {

	public CtrlActionDataImport(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame parent = (JFrame) Application.getActiveApplication().getMainFrame();
				DataImportFrame dataImportFrame = new DataImportFrame(parent, true);
				dataImportFrame.setVisible(true);
			}
		});
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();
		if (datasources != null && datasources.length > 0) {
			for (Datasource datasource : datasources) {
				if (!datasource.isReadOnly()) {
					enable = true;
					break;
				}
			}
		}
		if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			for (int i = 0; i < Application.getActiveApplication().getWorkspace().getDatasources().getCount(); i++) {
				Datasource tempDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(i);
				if (!tempDatasource.isReadOnly()) {
					enable = true;
					break;
				}
			}
		}
		return enable;
	}

	@Override
	public boolean check() {
		return false;
	}

}
