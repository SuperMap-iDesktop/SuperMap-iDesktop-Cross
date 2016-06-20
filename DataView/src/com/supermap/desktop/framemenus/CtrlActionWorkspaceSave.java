package com.supermap.desktop.framemenus;

import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.event.SaveWorkspaceEvent;
import com.supermap.desktop.event.SaveWorkspaceListener;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.WorkspaceUtilities;

public class CtrlActionWorkspaceSave extends CtrlAction {

	static boolean isInited = false;
	private WorkspaceConnectionInfo newWorkspaceConnectionInfo;

	private SaveWorkspaceListener saveWorkspaceListener = new SaveWorkspaceListener() {

		@Override
		public void saveWorkspace(SaveWorkspaceEvent event) {
			try {
				abstraSaveWorkspace(event);
				ToolbarUIUtilities.updataToolbarsState();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
	};

	private void abstraSaveWorkspace(SaveWorkspaceEvent event) {
		newWorkspaceConnectionInfo = event.getWorkspaceConnectionInfo();
		if (!event.isSaveCurrentWorkspace()) {
			if (event.isCloseAllOpenedWindows()) {
				Application.getActiveApplication().getMainFrame().getFormManager().closeAll();
			}

			if (event.isCloseWorkspace()) {
				Application.getActiveApplication().getWorkspace().close();
			}
		} else if (event.isSaveCurrentWorkspace() && run(newWorkspaceConnectionInfo) == DialogResult.OK) {
			if (event.isCloseAllOpenedWindows()) {
				Application.getActiveApplication().getMainFrame().getFormManager().closeAll();
			}

			if (event.isCloseWorkspace()) {
				Application.getActiveApplication().getWorkspace().close();
			}

			if (newWorkspaceConnectionInfo != null) {
				// 打开工作空间
				WorkspaceUtilities.openWorkspace(newWorkspaceConnectionInfo, false);
			}
		} else {
			event.setHandled(false);
		}
	}

	public CtrlActionWorkspaceSave(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		if (!isInited) {
			isInited = true;
			newWorkspaceConnectionInfo = null;
			WorkspaceUtilities.addSaveWorkspaceListener(saveWorkspaceListener);
		}
	}

	@Override
	public void run() {
		try {
			run(null);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public DialogResult run(WorkspaceConnectionInfo info) {
		DialogResult dialogResult = DialogResult.CANCEL;
		try {
			dialogResult = CtrlActionWorkspaceSave.save(info);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return dialogResult;
	}

	public static DialogResult save(WorkspaceConnectionInfo info) {
		DialogResult dialogResult = DialogResult.CANCEL;
		try {
			WorkspaceType workspaceType = Application.getActiveApplication().getWorkspace().getType();
			// 是默认类型，说明需要进行另存操作
			if (workspaceType == WorkspaceType.DEFAULT) {
				dialogResult = CtrlActionWorkspaceSaveAs.saveAs(info);
			} else {
				if (!WorkspaceUtilities.isWorkspaceReadonly()) {
					if (Application.getActiveApplication().getMainFrame().getFormManager().getCount() > 0) {
						CommonToolkit.FormWrap.saveAllOpenedWindows();
					}
					Application.getActiveApplication().getWorkspace().save();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			ToolbarUIUtilities.updataToolbarsState();
		}

		return dialogResult;
	}

	@Override
	public boolean enable() {
		return WorkspaceUtilities.isWorkspaceModified();
	}
}
