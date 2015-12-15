package com.supermap.desktop.framemenus;

import java.io.File;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.implement.SmMenuItem;
import com.supermap.desktop.utilties.PathUtilties;

public class CtrlActionExampleData extends CtrlAction {

	public CtrlActionExampleData(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			if (this.getCaller() instanceof SmMenuItem) {
				String filePath = ((SmMenuItem) this.getCaller()).getToolTipText();
				String configFile = PathUtilties.getFullPathName(filePath, false);
				File file = new File(configFile);
				if (isWorkSpaceFile(file)) {
					WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo(file.getAbsolutePath());
					CommonToolkit.WorkspaceWrap.openWorkspace(connectionInfo, true);
				} else {
					CommonToolkit.DatasourceWrap.openFileDatasource(file.getAbsolutePath(), null, false);
				}

			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

	}

	private boolean isWorkSpaceFile(File file) {
		boolean flag = false;
		String fileName = file.getName();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if ("smwu".equalsIgnoreCase(fileType) || "sxmu".equalsIgnoreCase(fileType)) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean enable() {
		boolean result = false;
		String filePath = ((SmMenuItem) this.getCaller()).getToolTipText();
		String configFile = PathUtilties.getFullPathName(filePath, false);
		File file = new File(configFile);
		if (file.exists()) {
			result = true;
		}
		return result;
	}

}
