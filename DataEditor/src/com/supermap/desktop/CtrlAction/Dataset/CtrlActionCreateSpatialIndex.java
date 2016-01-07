package com.supermap.desktop.CtrlAction.Dataset;

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
		boolean enable = false;
		if (null != Application.getActiveApplication().getActiveDatasets() && Application.getActiveApplication().getActiveDatasets().length > 0) {
			enable = !Application.getActiveApplication().getActiveDatasets()[0].getDatasource().isReadOnly();
		}
		return enable;
	}
}
