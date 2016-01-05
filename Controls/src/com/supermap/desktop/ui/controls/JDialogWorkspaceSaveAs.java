package com.supermap.desktop.ui.controls;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.data.WorkspaceVersion;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.PropertyManagerUtilties;
import com.supermap.desktop.utilties.SystemPropertyUtilties;

public class JDialogWorkspaceSaveAs extends SmDialog {

	// Variables declaration
	// End of variables declaration

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// UI Variables declaration - do not modify
	private final JPanel contentPanel = new JPanel();
	private javax.swing.JButton cancelButton;
	private javax.swing.JButton okButton;
	private JList<String> listWorkspaceType;
	private JPanelWorkspaceSaveAsFile panelSaveAsFile;
	private transient GroupLayout groupLayoutContentPanel;
	public final static int saveAsFile = 0;
	public final static int saveAsOracle = 1;
	public final static int saveAsSQL = 2;
	private transient WorkspaceConnectionInfo workspaceConnectionInfo = new WorkspaceConnectionInfo();

	public JDialogWorkspaceSaveAs(JFrame owner, boolean model, int flag) {
		super(owner, model);
		setTitle(ControlsProperties.getString("String_WorkspaceSaveAs"));
		setBounds(100, 100, 575, 301);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		listWorkspaceType = new JList<String>();
		listWorkspaceType.setBorder(MetalBorders.getTextBorder());
		listWorkspaceType.setModel(new ListModel<String>() {
			// @formatter:off
			private String[] values = SystemPropertyUtilties.isWindows() ? 
						new String[] { CoreProperties.getString("String_WorkspaceType_File"), CoreProperties.getString("String_WorkspaceType_Oracle"), CoreProperties.getString("String_WorkspaceType_SQL") }: 
						new String[] { CoreProperties.getString("String_WorkspaceType_File"), CoreProperties.getString("String_WorkspaceType_Oracle")};
			// @formatter:on

			@Override
			public void removeListDataListener(ListDataListener l) {
				// 默认实现，后续进行初始化操作
			}

			@Override
			public int getSize() {
				return values.length;
			}

			@Override
			public String getElementAt(int index) {
				return values[index];
			}

			@Override
			public void addListDataListener(ListDataListener l) {
				// 默认实现，后续进行初始化操作
			}
		});

		Font font = new Font(null, 0, 15);
		listWorkspaceType.setFont(font);
		listWorkspaceType.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				listWorkspaceType_ItemSelectedChanged();
			}
		});

		panelSaveAsFile = new JPanelWorkspaceSaveAsFile();
		groupLayoutContentPanel = new GroupLayout(contentPanel);
		groupLayoutContentPanel.setHorizontalGroup(groupLayoutContentPanel.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayoutContentPanel.createSequentialGroup().addComponent(listWorkspaceType, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panelSaveAsFile, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)));
		groupLayoutContentPanel.setVerticalGroup(groupLayoutContentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayoutContentPanel.createSequentialGroup().addGap(78).addContainerGap(156, Short.MAX_VALUE))
				.addComponent(listWorkspaceType, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
				.addComponent(panelSaveAsFile, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE));

		contentPanel.setLayout(groupLayoutContentPanel);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton(CommonProperties.getString("String_Button_OK"));
		okButton.setPreferredSize(new java.awt.Dimension(75, 23));
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		cancelButton = new JButton(CommonProperties.getString("String_Button_Cancel"));
		cancelButton.setPreferredSize(new java.awt.Dimension(75, 23));
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		okButton.addActionListener(new OkBUttonActionLisenter());
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		if (flag == saveAsFile) {
			JPanel existingPanel = getPanel();
			JPanel newPanel = getPanel(saveAsFile);
			this.groupLayoutContentPanel.replace(existingPanel, newPanel);
			listWorkspaceType.setSelectedIndex(saveAsFile);
		} else if (flag == saveAsOracle) {
			JPanel existingPanel = getPanel();
			JPanel newPanel = getPanel(saveAsOracle);
			this.groupLayoutContentPanel.replace(existingPanel, newPanel);
			listWorkspaceType.setSelectedIndex(saveAsOracle);
		} else if (flag == saveAsSQL) {
			JPanel existingPanel = getPanel();
			JPanel newPanel = getPanel(saveAsSQL);
			this.groupLayoutContentPanel.replace(existingPanel, newPanel);
			listWorkspaceType.setSelectedIndex(saveAsSQL);
		}
	}

	public WorkspaceConnectionInfo getWorkspaceConnectionInfo() {
		return this.workspaceConnectionInfo;
	}

	private void listWorkspaceType_ItemSelectedChanged() {
		try {
			int index = this.listWorkspaceType.getSelectedIndex();
			JPanel existingPanel = getPanel();
			JPanel newPanel = getPanel(index);
			this.groupLayoutContentPanel.replace(existingPanel, newPanel);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private JPanel getPanel(int index) {
		JPanel result = null;
		switch (index) {
		case 0:
			result = new JPanelWorkspaceSaveAsFile();
			break;
		case 1:
			result = new JPanelWorkspaceSaveAsSQL(saveAsOracle);
			break;
		case 2:
			result = new JPanelWorkspaceSaveAsSQL(saveAsSQL);
			break;
		default:
			break;
		}
		return result;
	}

	private JPanel getPanel() {
		Component component = this.contentPanel.getComponent(1);
		JPanel result = null;
		if (component instanceof JPanel) {
			result = (JPanel) component;
		}
		return result;
	}

	class OkBUttonActionLisenter implements ActionListener {

		private void setWorkspaceVersion(WorkspaceConnectionInfo workspaceConnectionInfo, String workspaceVersion) {
			if ("SuperMap UGC 7.0".equals(workspaceVersion)) {
				workspaceConnectionInfo.setVersion(WorkspaceVersion.UGC70);
			}
			if ("SuperMap UGC 6.0".equals(workspaceVersion)) {
				workspaceConnectionInfo.setVersion(WorkspaceVersion.UGC60);
			}
		}

		private void saveAs(Workspace workspace, WorkspaceConnectionInfo workspaceConnectionInfo, String workspaceName, String fileName) {
			try {
				if (workspaceConnectionInfo.getVersion() == WorkspaceVersion.UGC60
						&& JOptionPane.OK_OPTION != UICommonToolkit.showConfirmDialogWithCancel(ControlsProperties.getString("String_WorkspaceSaveAs_Confirm"))) {
					return;
				}
				String WorkspaceBeforeName = workspace.getCaption();
				if (null != fileName) {
					workspace.setCaption(fileName);
				}
				if (workspace.saveAs(workspaceConnectionInfo)) {
					Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_WorkspaceSaveAs") + workspaceName);
					dispose();
				} else {
					workspace.setCaption(WorkspaceBeforeName);
					Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_SaveWorkspace_Failed"));
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String workspaceFileName = "";
			String workspacePassword = "";
			String workspacePasswordConfirm = "";
			String workspaceVersion = "";
			String serverName = "";
			String databaseName = "";
			String userName = "";
			String userPassword = "";
			String workspaceName = "";
			Workspace workspace = Application.getActiveApplication().getWorkspace();
			final JPanel tempPanel = getPanel();
			int index = listWorkspaceType.getSelectedIndex();

			if (tempPanel instanceof JPanelWorkspaceSaveAsFile) {
				String fileName = ((JPanelWorkspaceSaveAsFile) tempPanel).getFileName();
				workspaceFileName = ((JPanelWorkspaceSaveAsFile) tempPanel).getjTextFieldFileName().getText();
				workspacePassword = String.valueOf(((JPanelWorkspaceSaveAsFile) tempPanel).getjPasswordFieldPassword().getPassword());
				workspacePasswordConfirm = String.valueOf(((JPanelWorkspaceSaveAsFile) tempPanel).getjPasswordFieldPasswordConfrim().getPassword());
				workspaceConnectionInfo.setServer(workspaceFileName);
				workspaceConnectionInfo.setType(getWorkspaceType(workspaceFileName));
				if (workspaceFileName.isEmpty()) {
					UICommonToolkit.showMessageDialog(CoreProperties.getString("String_ErrorProvider_FileName_Empty"));
					((JPanelWorkspaceSaveAsFile) tempPanel).getjButtonBrowser().requestFocus();
					return;
				}
				if (!workspacePassword.equals(workspacePasswordConfirm)) {
					UICommonToolkit.showMessageDialog(CoreProperties.getString("String_ErrorProvider_Password_Confirm"));
					((JPanelWorkspaceSaveAsFile) tempPanel).getjPasswordFieldPassword().requestFocus();
					return;
				} else {
					workspaceConnectionInfo.setPassword(workspacePassword);
				}
				workspaceVersion = (String) ((JPanelWorkspaceSaveAsFile) tempPanel).getjComboBoxVersion().getSelectedItem();
				setWorkspaceVersion(workspaceConnectionInfo, workspaceVersion);
				fileName = fileName.lastIndexOf(".") > 0 ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
				saveAs(workspace, workspaceConnectionInfo, workspaceFileName, fileName);
			}
			if (tempPanel instanceof JPanelWorkspaceSaveAsSQL) {
				serverName = (String) ((JPanelWorkspaceSaveAsSQL) tempPanel).getjComboBoxServer().getSelectedItem();
				databaseName = ((JPanelWorkspaceSaveAsSQL) tempPanel).getjTextFieldDatabaseName().getText();
				userName = ((JPanelWorkspaceSaveAsSQL) tempPanel).getjTextFieldUserName().getText();
				userPassword = String.valueOf(((JPanelWorkspaceSaveAsSQL) tempPanel).getjTextFieldPassword().getPassword());
				workspaceName = (String) ((JPanelWorkspaceSaveAsSQL) tempPanel).getjComboBoxWorkspaceName().getSelectedItem();
				workspaceVersion = (String) ((JPanelWorkspaceSaveAsSQL) tempPanel).getjComboBoxWorkspaceVersion().getSelectedItem();
				if (saveAsOracle == index) {
					workspaceConnectionInfo.setType(WorkspaceType.ORACLE);
					workspaceConnectionInfo.setServer(serverName);
					workspaceConnectionInfo.setDatabase(databaseName);
					if (null == workspaceName || workspaceName.isEmpty()) {
						UICommonToolkit.showMessageDialog(CoreProperties.getString("String_WorkspaceName_Empty"));
					} else {
						workspaceConnectionInfo.setName(workspaceName);
					}
					workspaceConnectionInfo.setUser(userName);
					workspaceConnectionInfo.setPassword(userPassword);
					setWorkspaceVersion(workspaceConnectionInfo, workspaceVersion);
					saveAs(workspace, workspaceConnectionInfo, workspaceName, workspaceName);
				}
				if (saveAsSQL == index) {
					workspaceConnectionInfo.setType(WorkspaceType.SQL);
					workspaceConnectionInfo.setDriver("SQL Server");
					workspaceConnectionInfo.setServer(serverName);
					workspaceConnectionInfo.setDatabase(databaseName);
					if (null == workspaceName || workspaceName.isEmpty()) {
						UICommonToolkit.showMessageDialog(CoreProperties.getString("String_WorkspaceName_Empty"));
					} else {
						workspaceConnectionInfo.setName(workspaceName);
					}
					workspaceConnectionInfo.setUser(userName);
					workspaceConnectionInfo.setPassword(userPassword);
					setWorkspaceVersion(workspaceConnectionInfo, workspaceVersion);
					saveAs(workspace, workspaceConnectionInfo, workspaceName, workspaceName);
				}
			}
			PropertyManagerUtilties.refreshPropertyManager();
		}

		private WorkspaceType getWorkspaceType(String workspaceFilePath) {
			String fileType = workspaceFilePath.substring(workspaceFilePath.indexOf(".") + 1, workspaceFilePath.length());
			WorkspaceType result = WorkspaceType.SMWU;
			if ("smwu".equalsIgnoreCase(fileType)) {
				result = WorkspaceType.SMWU;
			}
			if ("sxwu".equalsIgnoreCase(fileType)) {
				result = WorkspaceType.SXWU;
			}
			return result;
		}

	}
}
