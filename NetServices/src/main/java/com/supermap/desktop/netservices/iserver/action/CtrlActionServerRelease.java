package com.supermap.desktop.netservices.iserver.action;

import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.netservices.NetServicesProperties;
import com.supermap.desktop.netservices.iserver.JDialogServerRelease;
import com.supermap.desktop.netservices.iserver.WorkspaceInfo;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.JDialogWorkspaceSaveAs;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.WorkspaceUtilities;

import javax.swing.*;
import java.text.MessageFormat;

public class CtrlActionServerRelease extends CtrlAction {

	public CtrlActionServerRelease(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			Boolean prepared = prepareWorkspace();
			if (prepared) {
				if (Application.getActiveApplication().getWorkspace().getConnectionInfo().getType() != WorkspaceType.DEFAULT) {
					JDialogServerRelease dialog = new JDialogServerRelease(WorkspaceInfo.getCurrentWorkspaceInfo());
					dialog.setVisible(true);
				} else {
					UICommonToolkit.showMessageDialog(NetServicesProperties.getString("String_iServer_Message_PleaseSave"));
				}
			} else {
				UICommonToolkit.showMessageDialog(NetServicesProperties.getString("String_iServer_Message_PreparedFaild"));
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 为发布服务准备工作空间
	 * 
	 * @return
	 */
	private boolean prepareWorkspace() {
		Boolean prepared = true;

		Boolean closed = Application.getActiveApplication().getMainFrame().getFormManager().closeAll(true);
		Boolean isContinue = true;
		if (closed) {
			if (DatasourceUtilities.isContainMemoryDatasource(Application.getActiveApplication().getWorkspace())) {
				String[] datasources = DatasourceUtilities.getMemoryDatasources(Application.getActiveApplication().getWorkspace());
				String datasourcesName = "";
				for (int i = 0; i < datasources.length - 1; i++) {
					datasourcesName += datasources[i] + "、";
				}
				datasourcesName += datasources[datasources.length - 1];
				String message = MessageFormat.format(NetServicesProperties.getString("String_Message_SaveMemoryDatasource"), datasourcesName);
				int result = UICommonToolkit.showConfirmDialog(message, CoreProperties.getString("String_SaveWorkspace"));
				if (result == JOptionPane.NO_OPTION) {
					isContinue = false;
				} else {
					DatasourceUtilities.closeMemoryDatasource();
				}
			}
			if (isContinue) {
				if (WorkspaceUtilities.isWorkspaceModified()) {
					int result = UICommonToolkit.showConfirmDialog(CoreProperties.getString("String_SaveWorkspacePrompt"),
							CoreProperties.getString("String_SaveWorkspace"));

					if (result == JOptionPane.YES_OPTION) {
						if (Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.DEFAULT) {
							prepared = prepareWorkspaceDefault();
						} else {
							prepared = Application.getActiveApplication().getWorkspace().save();
						}
					} else if (result == JOptionPane.NO_OPTION) {
						if (Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.DEFAULT) {
							if (UICommonToolkit.showConfirmDialog(NetServicesProperties.getString("String_iServer_Message_NoSavingIsCertain")) == JOptionPane.NO_OPTION) {
								prepared = prepareWorkspaceDefault();
							} else {
								prepared = false;
							}
						}
					}
				} else {
					if (Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.DEFAULT) {
						prepared = prepareWorkspaceDefault();
					}
				}
			}
		} else {
			prepared = false;
		}

		return prepared;
	}

	private boolean prepareWorkspaceDefault() {
		Boolean prepare = true;
		JFrame parent = (JFrame) Application.getActiveApplication().getMainFrame();
		JDialogWorkspaceSaveAs dialog = new JDialogWorkspaceSaveAs(parent, true, JDialogWorkspaceSaveAs.saveAsFile);
		prepare = (dialog.showDialog() == DialogResult.APPLY);
		return prepare;
	}
}
