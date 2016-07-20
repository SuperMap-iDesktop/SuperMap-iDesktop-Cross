package com.supermap.desktop.controls.utilities;

import com.supermap.desktop.CtrlAction.WorkspaceAutoSave;
import com.supermap.desktop.utilities.WorkspaceUtilities;

/**
 * @author XiaJT
 */
public class SystemUIUtilities {

	private SystemUIUtilities() {
	}

	public static void exit() {
		if (WorkspaceUtilities.closeWorkspace()) {
			WorkspaceAutoSave.getInstance().exit();
			System.exit(0);
		}
	}
}
