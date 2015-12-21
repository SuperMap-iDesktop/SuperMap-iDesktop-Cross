package com.supermap.desktop.popupmenus;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.DataExportFrame;

import javax.swing.*;

public class CtrlActionDataExport extends CtrlAction {

	public CtrlActionDataExport(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame parent = (JFrame) Application.getActiveApplication().getMainFrame();
				Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
				DataExportFrame dataImportFrame = new DataExportFrame(datasets, parent, true);
				dataImportFrame.setVisible(true);
			}
		});
	}

	@Override
	public boolean enable() {
		boolean enable = Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0;
		return enable;
	}
}
