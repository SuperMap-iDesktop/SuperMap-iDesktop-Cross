package com.supermap.desktop.controls.utilities;

import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.desktop.Application;
import com.supermap.desktop.dialog.JDialogGetPassword;
import com.supermap.desktop.enums.OpenWorkspaceResult;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.LogUtilities;

import java.text.MessageFormat;

/**
 * @author XiaJT
 */
public class WorkspaceUIUtilities {
	private WorkspaceUIUtilities() {

	}

	public static void openWorkspace(String filePath) {
		if (filePath.endsWith("smwu") || filePath.endsWith("sxwu")) {
			final WorkspaceConnectionInfo info = new WorkspaceConnectionInfo(filePath);
			final OpenWorkspaceResult[] result = {com.supermap.desktop.utilities.WorkspaceUtilities.openWorkspace(info, true)};
			if (result[0] == OpenWorkspaceResult.SUCCESSED) {
				if (Application.getActiveApplication().getMainFrame().getFormManager().getCount() > 0) {
					LogUtilities.outPut(MessageFormat.format(CoreProperties.getString("String_CloseForms"), Application.getActiveApplication().getMainFrame().getFormManager().getCount()));
					Application.getActiveApplication().getMainFrame().getFormManager().closeAll();
					LogUtilities.outPut(CoreProperties.getString("String_CloseFormsSuccess"));
				}
			} else if (result[0] == OpenWorkspaceResult.FAILED_PASSWORD_WRONG) {
				LogUtilities.outPut(CoreProperties.getString("String_inputPassword"));
				JDialogGetPassword dialogGetPassword = new JDialogGetPassword(CoreProperties.getString("String_WorkspacePasswordPrompt")) {

					private static final long serialVersionUID = 1L;

					public boolean isRightPassword(String password) {
						info.setPassword(getPassword());
						result[0] = com.supermap.desktop.utilities.WorkspaceUtilities.openWorkspace(info, false);
						return result[0] != OpenWorkspaceResult.FAILED_PASSWORD_WRONG;

					}
				};
				dialogGetPassword.showDialog();
			}
			if (result[0] != OpenWorkspaceResult.SUCCESSED) {
				String stMsg;
				if (result[0] != OpenWorkspaceResult.FAILED_CANCEL) {
					stMsg = MessageFormat.format(CoreProperties.getString("String_OpenWorkspaceFailed"), filePath);
					Application.getActiveApplication().getWorkspace().close();
				} else if (result[0] == OpenWorkspaceResult.FAILED_PASSWORD_WRONG) {
					stMsg = MessageFormat.format(CoreProperties.getString("String_OpenWorkspaceFailed_WrongPassword"), filePath);
				} else {
					stMsg = CoreProperties.getString("String_openWorkspaceCancle");
				}
				Application.getActiveApplication().getOutput().output(stMsg);
				LogUtilities.outPut(stMsg);
			} else {
				LogUtilities.outPut(MessageFormat.format(CoreProperties.getString("String_openWorksapceSuccess"), Application.getActiveApplication().getWorkspace().getCaption()));
			}
		}
	}
}
