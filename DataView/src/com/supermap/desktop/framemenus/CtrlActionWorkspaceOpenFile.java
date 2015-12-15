package com.supermap.desktop.framemenus;

import javax.swing.JFileChooser;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.enums.OpenWorkspaceResult;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.dialog.JDialogGetPassword;
import com.supermap.desktop.ui.controls.SmFileChoose;

public class CtrlActionWorkspaceOpenFile extends CtrlAction {

	private WorkspaceConnectionInfo info;
	private OpenWorkspaceResult result;

	public CtrlActionWorkspaceOpenFile(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			if (!SmFileChoose.isModuleExist("WorkspaceOpenFile")) {
				String fileFilters = SmFileChoose.createFileFilter(DataViewProperties.getString("String_FileFilters_Workspace"), "smwu", "sxwu");
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						DataViewProperties.getString("String_OpenWorkspace"), "WorkspaceOpenFile", "OpenOne");
			}

			SmFileChoose fileChooser = new SmFileChoose("WorkspaceOpenFile");
			if (fileChooser.showDefaultDialog() == JFileChooser.APPROVE_OPTION && !"".equals(fileChooser.getSelectedFile().getAbsolutePath())) {
				info = new WorkspaceConnectionInfo(fileChooser.getFilePath());
				result = CommonToolkit.WorkspaceWrap.openWorkspace(info, true);
				if (result == OpenWorkspaceResult.SUCCESSED) {
					if (Application.getActiveApplication().getMainFrame().getFormManager().getCount() > 0) {
						Application.getActiveApplication().getMainFrame().getFormManager().closeAll();
					}
				} else if (result == OpenWorkspaceResult.FAILED_PASSWORD_WRONG) {
					JDialogGetPassword dialogGetPassword = new JDialogGetPassword(CoreProperties.getString("String_WorkspacePasswordPrompt")){
						
						private static final long serialVersionUID = 1L;

						public boolean isRightPassword(String password){
							info.setPassword(getPassword());
							result = CommonToolkit.WorkspaceWrap.openWorkspace(info, false);
							return result != OpenWorkspaceResult.FAILED_PASSWORD_WRONG;
							
						}
					};
					dialogGetPassword.showDialog();
				}

				if (result != OpenWorkspaceResult.SUCCESSED) {
					String stMsg = String.format(CoreProperties.getString("String_OpenWorkspaceFailed"), fileChooser.getFilePath());
					if (result != OpenWorkspaceResult.FAILED_CANCEL) {
						Application.getActiveApplication().getWorkspace().close();
					} else if (result == OpenWorkspaceResult.FAILED_PASSWORD_WRONG) {
						stMsg = String.format(CoreProperties.getString("String_OpenWorkspaceFailed_WrongPassword"), fileChooser.getFilePath());
						Application.getActiveApplication().getOutput().output(stMsg);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}

}
