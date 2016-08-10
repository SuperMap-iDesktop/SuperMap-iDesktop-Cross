package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS.HDFSDefine;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.dialog.HDFSTableModel;
import com.supermap.desktop.dialog.RenameDialog;
import com.supermap.desktop.http.CreateFile;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.StringUtilities;

public class CtrlActionRename extends CtrlAction {
	public CtrlActionRename(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IFormLBSControl lbsControl = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
		if (lbsControl.getSelectRow() > -1) {
			String oldName = (String) lbsControl.getTable().getValueAt(lbsControl.getSelectRow(), 0);
			RenameDialog dialog = new RenameDialog(oldName);
			if (dialog.showDialog().equals(DialogResult.OK) && !StringUtilities.isNullOrEmpty(dialog.getNewName())) {
				CreateFile createFile = new CreateFile();
				HDFSDefine define = (HDFSDefine) ((HDFSTableModel) lbsControl.getTable().getModel()).getRowTagAt(lbsControl.getTable().getSelectedRow());
				createFile.renameFile(lbsControl.getURL(), oldName, dialog.getNewName(),define.isDir());
			}
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormLBSControl) {
			enable = true;
		}
		return enable;
	}
}
