package com.supermap.desktop.framemenus;

import javax.swing.JFrame;

import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.utilties.ToolbarUtilties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.JDialogWorkspaceSaveAs;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilties.WorkspaceUtilties;

public class CtrlActionWorkspaceSaveAs extends CtrlAction {

	public CtrlActionWorkspaceSaveAs(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			run(null);
			ToolbarUtilties.updataToolbarsState();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public DialogResult run(WorkspaceConnectionInfo info) {
		return CtrlActionWorkspaceSaveAs.saveAs(info);
	}

	@Override
	public boolean enable() {
		return true;
	}

	public static DialogResult saveAs(WorkspaceConnectionInfo info) {
		DialogResult dialogResult = DialogResult.CANCEL;
		try {
			boolean isCancel = false;
			if (Application.getActiveApplication().getMainFrame().getFormManager().getCount() > 0) {
				isCancel = !CommonToolkit.FormWrap.saveAllOpenedWindows();
			}

			if (!isCancel) {
				JFrame parent = (JFrame) Application.getActiveApplication().getMainFrame();
				JDialogWorkspaceSaveAs dialog = new JDialogWorkspaceSaveAs(parent, true, JDialogWorkspaceSaveAs.saveAsFile);
				dialogResult = dialog.showDialog();
				if (dialogResult == DialogResult.OK || dialogResult == dialogResult.APPLY) {
					if (info != null) {
						UICommonToolkit.getWorkspaceManager().getWorkspaceTree().updateUI();
					} else {
						WorkspaceConnectionInfo workspaceConnectionInfo = dialog.getWorkspaceConnectionInfo();
						UICommonToolkit.getWorkspaceManager().getWorkspaceTree().updateUI();
						if (workspaceConnectionInfo.getType() == WorkspaceType.SXWU || workspaceConnectionInfo.getType() == WorkspaceType.SMWU) {
							WorkspaceUtilties.addWorkspaceFileToRecentFile(workspaceConnectionInfo.getServer());
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return dialogResult;
	}

}
