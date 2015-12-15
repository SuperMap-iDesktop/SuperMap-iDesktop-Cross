package com.supermap.desktop.CtrlAction;

import java.io.File;
import java.text.MessageFormat;

import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilties.SystemPropertyUtilties;

public class CtrlActionWorkspaceOpenDirectory extends CtrlAction {

	public CtrlActionWorkspaceOpenDirectory(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			String path = Application.getActiveApplication().getWorkspace().getConnectionInfo().getServer();
			File file = new File(path);
			if (file.exists()) {
				if (SystemPropertyUtilties.isWindows()) {
					Runtime.getRuntime().exec("explorer.exe /select, " + path);
				} else {
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(DataViewProperties.getString("String_LinuxOpenInDirectory"), path));
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			enable = this.isFileWorkspace();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}

	boolean isFileWorkspace() {
		boolean result = false;
		try {
			if (Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.SMW
					|| Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.SMWU
					|| Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.SXW
					|| Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.SXWU) {
				result = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

}
