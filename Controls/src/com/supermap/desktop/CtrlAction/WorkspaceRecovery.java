package com.supermap.desktop.CtrlAction;

import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceSavedAsEvent;
import com.supermap.data.WorkspaceSavedAsListener;
import com.supermap.data.WorkspaceSavedEvent;
import com.supermap.data.WorkspaceSavedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dialog.JDialogGetPassword;
import com.supermap.desktop.enums.OpenWorkspaceResult;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.FileLocker;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import com.supermap.desktop.utilities.LogUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.WorkspaceConnectionInfoUtilities;
import com.supermap.desktop.utilities.WorkspaceUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.misc.BASE64Decoder;

import javax.swing.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class WorkspaceRecovery {
	private static WorkspaceRecovery workspaceRecovery = new WorkspaceRecovery();
	private WorkspaceSavedListener workspaceSavedListener;
	private WorkspaceSavedAsListener workspaceSavedAsListener;

	private WorkspaceRecovery() {

	}

	public void run() {
		try {
			recovery();
		} catch (Exception e) {
			LogUtilities.error("Recovery Failed", e);
		} finally {
			// one change
			workspaceRecovery = null;
			WorkspaceAutoSave.getInstance().start();
		}
	}

	private void recovery() {
		String appDataPath = FileUtilities.getAppDataPath();
		if (appDataPath != null) {
			appDataPath += "tempWorkspace" + File.separator;
			File appDataPathFile = new File(appDataPath);
			if (appDataPathFile.exists()) {
				ArrayList<File> fileList = new ArrayList<>();
				File[] files = appDataPathFile.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.getName().toLowerCase().endsWith(".xml")) {
							if (file.length() > 0) {

								FileLocker fileLock = new FileLocker(file);
								if (fileLock.tryLock()) {
									fileList.add(file);
									fileLock.release();
								}
							} else {
								file.delete();
							}
						}
					}
				}
				if (fileList.size() >= 1) {
					File file = fileList.get(0);
					Document document = XmlUtilities.getDocument(file.getPath());
					if (document != null && JOptionPaneUtilities.showConfirmDialog(CoreProperties.getString("String_ExistUnSaveWorkspace")) == JOptionPane.OK_OPTION) {
						recoveryFromFile(file);
						ToolbarUIUtilities.updataToolbarsState();
					} else {
						deleteAutoSaveConfigFile(file);
					}
				}
			}
		}
	}

	private void recoveryFromFile(File file) {

		FileLocker fileLocker = new FileLocker(file);
		if (fileLocker.tryLock()) {
			RandomAccessFile randomAccessFile = fileLocker.getRandomAccessFile();
			try {
				long length = randomAccessFile.length();
				byte[] bytes = new byte[(int) length];
				randomAccessFile.read(bytes);
				Document document = XmlUtilities.stringToDocument(new String(bytes, "UTF-8"));
				if (document != null) {
					Node root = document.getChildNodes().item(0);

					String workSpaceFilePath = "";
					Node workspacePath = XmlUtilities.getChildElementNodeByName(root, "WorkspacePath");
					if (workspacePath.getChildNodes() != null && workspacePath.getChildNodes().getLength() > 0) {
						workSpaceFilePath = workspacePath.getChildNodes().item(0).getNodeValue();
					}


					Node workspaceConnectionInfoNode = XmlUtilities.getChildElementNodeByName(root, "WorkspaceConnectionInfo");
					final WorkspaceConnectionInfo workspaceConnectionInfo = WorkspaceConnectionInfoUtilities.fromXml(workspaceConnectionInfoNode);

					String server = workspaceConnectionInfo.getServer();
					String recoveringWorkspacePath = getRecoveringWorkspacePath(server);
					//备份一下
					if (copyWorkspaceToRecoveringPath(server, recoveringWorkspacePath)) {
						workspaceConnectionInfo.setServer(recoveringWorkspacePath);
						WorkspaceUtilities.deleteFileWorkspace(server);
					} else {
						LogUtilities.outPut("Copy Recovery Workspace Failed.");
					}


					Node datasources = XmlUtilities.getChildElementNodeByName(root, "Datasources");
					NodeList childNodes = datasources.getChildNodes();
					ArrayList<DatasourceConnectionInfo> datasourceConnectionInfos = new ArrayList<>();
					if (childNodes != null && childNodes.getLength() > 0) {
						for (int i = 0; i < childNodes.getLength(); i++) {
							Node item = childNodes.item(i);
							if (item != null && item.getNodeType() == Node.ELEMENT_NODE && "Datasource".equalsIgnoreCase(item.getNodeName())) {
								if (item.getChildNodes() != null && item.getChildNodes().getLength() > 0) {
									String nodeValue = item.getChildNodes().item(0).getNodeValue();
									String datasourceConnect = new String(new BASE64Decoder().decodeBuffer(nodeValue), "UTF-8");
									DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
									if (datasourceConnectionInfo.fromXML(datasourceConnect)) {
										datasourceConnectionInfos.add(datasourceConnectionInfo);
									} else {
										LogUtilities.outPut("fromXml failed");
									}
								}
							}
						}
					}
					OpenWorkspaceResult result = openWorkspace(workspaceConnectionInfo);
					if (result != OpenWorkspaceResult.SUCCESSED) {
						while (result == OpenWorkspaceResult.FAILED_PASSWORD_WRONG) {
							JDialogGetPassword dialogGetPassword = new JDialogGetPassword(CoreProperties.getString("String_WorkspacePasswordPrompt")) {
								@Override
								public boolean isRightPassword(String password) {
									workspaceConnectionInfo.setPassword(password);
									return WorkspaceUtilities.openWorkspace(workspaceConnectionInfo, false) != OpenWorkspaceResult.FAILED_PASSWORD_WRONG;
								}
							};
							if (dialogGetPassword.showDialog() == DialogResult.OK) {
								// 按取消按钮不执行打开工作空间的操作。
								result = OpenWorkspaceResult.SUCCESSED;
							} else {
								result = OpenWorkspaceResult.FAILED_CANCEL;
							}
						}
					}

					if (result == OpenWorkspaceResult.SUCCESSED) {
						if (datasourceConnectionInfos.size() > 0) {
							for (DatasourceConnectionInfo datasourceConnectionInfo : datasourceConnectionInfos) {
								try {
									Application.getActiveApplication().getWorkspace().getDatasources().open(datasourceConnectionInfo);
								} catch (Exception e) {
									Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_OpenDatasourceFailed"), datasourceConnectionInfo.getServer()));
								}
							}
						}

						Class<WorkspaceConnectionInfo> clazz = WorkspaceConnectionInfo.class;
						Field isOpenedWorkspace = clazz.getDeclaredFields()[0];
						isOpenedWorkspace.setAccessible(true);
						WorkspaceConnectionInfo connectionInfo = Application.getActiveApplication().getWorkspace().getConnectionInfo();
						final String deleteServer = connectionInfo.getServer();
						isOpenedWorkspace.setBoolean(connectionInfo, false);
						connectionInfo.setServer(workSpaceFilePath);
						isOpenedWorkspace.setBoolean(connectionInfo, true);
						workspaceSavedAsListener = new WorkspaceSavedAsListener() {
							@Override
							public void workspaceSavedAs(WorkspaceSavedAsEvent workspaceSavedAsEvent) {
								FileUtilities.delete(deleteServer);
								workspaceSavedAsEvent.getWorkspace().removeSavedAsListener(this);
								workspaceSavedAsEvent.getWorkspace().removeSavedListener(workspaceSavedListener);
							}
						};
						workspaceSavedListener = new WorkspaceSavedListener() {
							@Override
							public void workspaceSaved(WorkspaceSavedEvent workspaceSavedEvent) {
								FileUtilities.delete(deleteServer);
								workspaceSavedEvent.getWorkspace().removeSavedListener(this);
								workspaceSavedEvent.getWorkspace().removeSavedAsListener(workspaceSavedAsListener);
							}
						};
						Application.getActiveApplication().getWorkspace().addSavedAsListener(workspaceSavedAsListener);

						Application.getActiveApplication().getWorkspace().addSavedListener(workspaceSavedListener);
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_RecoveryWorkspaceFailed"));
			} finally {
				fileLocker.release();
				deleteAutoSaveConfigFile(file);
			}
		}
	}

	private boolean copyWorkspaceToRecoveringPath(String workSpaceFilePath, String recoveringWorkspacePath) {
		String end = workSpaceFilePath.substring(workSpaceFilePath.length() - 5, workSpaceFilePath.length());
		// 复制工作空间
		if (!FileUtilities.copyFile(workSpaceFilePath, recoveringWorkspacePath, true)) {
			return false;
		}
		if (end.equalsIgnoreCase(".sxwu")) {
			String recoveringWorkspacePrefix = workSpaceFilePath.substring(0, workSpaceFilePath.length() - 4);
			String recoveringWorkspacePathPrefix = recoveringWorkspacePath.substring(0, recoveringWorkspacePath.length() - 4);
			if (!FileUtilities.copyFile(recoveringWorkspacePrefix + "bru", recoveringWorkspacePathPrefix + "bru", true) ||
					!FileUtilities.copyFile(recoveringWorkspacePrefix + "lsl", recoveringWorkspacePathPrefix + "lsl", true) ||
					!FileUtilities.copyFile(recoveringWorkspacePrefix + "sym", recoveringWorkspacePathPrefix + "sym", true)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获得一个空的恢复目录
	 *
	 * @return 恢复目录路径
	 */
	private String getRecoveringWorkspacePath(String workSpaceFilePath) {
		String defaultPath = FileUtilities.getAppDataPath() + "RecoveringWorkspace";
		int i = 0;
		String path = defaultPath + File.separator;
		while (new File(path).exists() && !FileUtilities.delete(path)) {
			i++;
			path = defaultPath + (i == 0 ? "" : "_" + i) + File.separator;
		}
		String name = new File(workSpaceFilePath).getName();
		return path + name;
	}


	private OpenWorkspaceResult openWorkspace(WorkspaceConnectionInfo workspaceConnectionInfo) {
		CursorUtilities.setWaitCursor();
		Application.getActiveApplication().getWorkspace().open(workspaceConnectionInfo);
		CursorUtilities.setDefaultCursor();
		return WorkspaceUtilities.openWorkspace(workspaceConnectionInfo, true);
	}

	private void deleteAutoSaveConfigFile(File file) {
		FileLocker fileLocker = new FileLocker(file);
		if (fileLocker.tryLock()) {
			fileLocker.release();
			Document document = XmlUtilities.getDocument(file.getAbsolutePath());
			if (document != null) {
				try {
					Node rootNode = document.getChildNodes().item(0);
					if (rootNode != null) {
						Node workspaceConnectionInfo = XmlUtilities.getChildElementNodeByName(rootNode, "WorkspaceConnectionInfo");
						if (workspaceConnectionInfo != null) {
							Node server = XmlUtilities.getChildElementNodeByName(workspaceConnectionInfo, "Server");
							if (server != null) {
								NodeList childNodes = server.getChildNodes();
								if (childNodes != null && childNodes.getLength() > 0) {
									String workspacePath = childNodes.item(0).getNodeValue();
									if (!StringUtilities.isNullOrEmpty(workspacePath) && new File(workspacePath).exists()) {
										WorkspaceUtilities.deleteFileWorkspace(workspacePath);
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// ignore
				} finally {
					if (file.exists()) {
						file.delete();
					}
				}
			}
		}
	}

	public static WorkspaceRecovery getInstance() {
		return workspaceRecovery;
	}
}
