package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS.HDFSDefine;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.dialog.HDFSTableModel;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.CursorUtilities;

public class CtrlActionDownLoadHDFS extends CtrlAction {

	public CtrlActionDownLoadHDFS(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormLBSControl control = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
			control.download();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormLBSControl) {
			IFormLBSControl control = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
			if (control.getSelectRow() > -1 && !((HDFSDefine) ((HDFSTableModel) control.getTable().getModel()).getRowTagAt(control.getSelectRow())).isDir()) {
				// 不能下载文件夹，选中文件夹时灰选
				enable = true;
			}
		}
		return enable;
	}

}
