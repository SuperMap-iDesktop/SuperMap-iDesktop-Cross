package com.supermap.desktop.framemenus;

import javax.swing.JOptionPane;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;

public class CtrlActionWorkspaceDelete extends CtrlAction {

	public CtrlActionWorkspaceDelete(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			int Result = UICommonToolkit.showConfirmDialog(DataViewProperties.getString("String_DeleteMessage"));
			if (Result == JOptionPane.OK_OPTION) {
				WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
				info.setDatabase(Application.getActiveApplication().getWorkspace().getConnectionInfo().getDatabase());
				info.setDriver(Application.getActiveApplication().getWorkspace().getConnectionInfo().getDriver());
				info.setName(Application.getActiveApplication().getWorkspace().getConnectionInfo().getName());
				info.setPassword(Application.getActiveApplication().getWorkspace().getConnectionInfo().getPassword());
				info.setServer(Application.getActiveApplication().getWorkspace().getConnectionInfo().getServer());
				info.setType(Application.getActiveApplication().getWorkspace().getConnectionInfo().getType());
				info.setUser(Application.getActiveApplication().getWorkspace().getConnectionInfo().getUser());
				CommonToolkit.WorkspaceWrap.closeWorkspace();
				boolean result = Workspace.deleteWorkspace(info);
				String resultInfo = "";
				if (result) {
					resultInfo = DataViewProperties.getString("String_DeleteWorkspaceSuccessful");
				} else {
					resultInfo = DataViewProperties.getString("String_DeletaWorkspaceFailed");
				}
				Application.getActiveApplication().getOutput().output(resultInfo);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		WorkspaceType workspaceType = Application.getActiveApplication().getWorkspace().getType();
		if (workspaceType == WorkspaceType.ORACLE || workspaceType == WorkspaceType.SQL) {
			enable = true;
		}
		return enable;
	}

}
