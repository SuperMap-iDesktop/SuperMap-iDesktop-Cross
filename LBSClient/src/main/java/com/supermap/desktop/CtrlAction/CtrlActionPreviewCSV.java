package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.lbs.HDFSDefine;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.lbs.ui.HDFSTableModel;
import com.supermap.desktop.ui.lbs.ui.JDialogPreviewCSV;

/**
 * @author XiaJT
 */
public class CtrlActionPreviewCSV extends CtrlAction {
	public CtrlActionPreviewCSV(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		HDFSDefine hdfsDefineResult = null;
		if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormLBSControl) {
			IFormLBSControl control = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
			if (control.getSelectRow() > -1 && control.getTable().getRowCount() > 0 && control.getTable().getRowCount() > control.getSelectRow()) {
				int[] selectRows = control.getSelectRows();
				for (int selectRow : selectRows) {
					HDFSDefine hdfsDefine = (HDFSDefine) ((HDFSTableModel) control.getTable().getModel()).getRowTagAt(selectRow);
					if (hdfsDefine.getName().toLowerCase().endsWith(".csv")) {
						hdfsDefineResult = hdfsDefine;
						break;
					}
				}
			}
		}
		if (hdfsDefineResult != null) {
			JDialogPreviewCSV jDialogPreviewCSV = new JDialogPreviewCSV(((IFormLBSControl) Application.getActiveApplication().getActiveForm()).getURL(), hdfsDefineResult, (HDFSTableModel) ((IFormLBSControl) Application.getActiveApplication().getActiveForm()).getTable().getModel());
			DialogResult dialogResult = jDialogPreviewCSV.showDialog();
			if (dialogResult == DialogResult.OK) {
				((IFormLBSControl) Application.getActiveApplication().getActiveForm()).refresh();
			}
		}

	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormLBSControl) {
			IFormLBSControl control = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
			if (control.getSelectRow() > -1 && control.getTable().getRowCount() > 0 && control.getTable().getRowCount() > control.getSelectRow()) {
				int[] selectRows = control.getSelectRows();
				for (int selectRow : selectRows) {
					HDFSDefine hdfsDefine = (HDFSDefine) ((HDFSTableModel) control.getTable().getModel()).getRowTagAt(selectRow);
					if (hdfsDefine.getName().toLowerCase().endsWith(".csv")) {
						enable = true;
						break;
					}
				}
			}
		}
		return enable;
	}
}
