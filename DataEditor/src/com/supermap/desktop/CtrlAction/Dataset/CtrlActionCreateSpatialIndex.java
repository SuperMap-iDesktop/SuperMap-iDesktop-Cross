package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Dataset.SpatialIndex.JDialogBulidSpatialIndex;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

import javax.swing.*;
import java.awt.*;

public class CtrlActionCreateSpatialIndex extends CtrlAction {

	public CtrlActionCreateSpatialIndex(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			JDialogBulidSpatialIndex jDialogBulidSpatialIndex = new JDialogBulidSpatialIndex();
			jDialogBulidSpatialIndex.showDialog();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.DEFAULT_CURSOR);
		}
	}

	@Override
	public boolean enable() {
		boolean result = false;
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		if (workspace != null && workspace.getDatasources() != null && workspace.getDatasources().getCount() > 0) {
			result = true;
		}
		return result;
	}
}
