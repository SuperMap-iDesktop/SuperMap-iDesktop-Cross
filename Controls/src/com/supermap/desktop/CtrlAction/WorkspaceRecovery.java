package com.supermap.desktop.CtrlAction;

import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Enum;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceVersion;
import com.supermap.desktop.Application;
import com.supermap.desktop.dialog.JDialogGetPassword;
import com.supermap.desktop.enums.OpenWorkspaceResult;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.FileLocker;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import com.supermap.desktop.utilities.LogUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.WorkspaceConnectionInfoUtilities;
import com.supermap.desktop.utilities.WorkspaceUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.misc.BASE64Decoder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class WorkspaceRecovery {
	private static WorkspaceRecovery workspaceRecovery = new WorkspaceRecovery();

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
				String s = new String(bytes, "UTF-8");
				Document document = XmlUtilities.stringToDocument(s);
				if (document != null) {
					Node root = document.getChildNodes().item(0);
					Node workspaceConnection = XmlUtilities.getChildElementNodeByName(root, "WorkspaceConnection");
					Node workspaceConnectionInfoNode = XmlUtilities.getChildElementNodeByName(workspaceConnection, "WorkspaceConnectionInfo");
					final WorkspaceConnectionInfo workspaceConnectionInfo = WorkspaceConnectionInfoUtilities.fromXml(workspaceConnectionInfoNode);

					NamedNodeMap attributes = workspaceConnection.getAttributes();
					WorkspaceVersion workspaceCurrentVersion = (WorkspaceVersion) Enum.parse(WorkspaceVersion.class, attributes.getNamedItem("WorkspaceConnectionInfoVersion").getNodeValue());
					String workspaceName = attributes.getNamedItem("WorkspaceName").getNodeValue();

					Node datasources = XmlUtilities.getChildElementNodeByName(root, "Datasources");
					NodeList childNodes = datasources.getChildNodes();
					ArrayList<DatasourceConnectionInfo> datasourceConnectionInfos = new ArrayList<>();
					if (childNodes != null && childNodes.getLength() > 0) {
						for (int i = 0; i < childNodes.getLength(); i++) {
							Node item = childNodes.item(i);
							if (item != null && item.getNodeType() == Node.ELEMENT_NODE && "Datasource".equalsIgnoreCase(item.getNodeValue())) {
								String nodeValue = item.getNodeValue();
								String datasourceConnect = new String(new BASE64Decoder().decodeBuffer(nodeValue), "UTF-8");
								DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
								if (datasourceConnectionInfo.fromXML(datasourceConnect)) {
									datasourceConnectionInfos.add(datasourceConnectionInfo);
								} else {
									System.out.println("fromXml failed");
								}
							}
						}
					}
					OpenWorkspaceResult result = WorkspaceUtilities.openWorkspace(workspaceConnectionInfo, true);
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

					}
				}
			} catch (IOException e) {
				Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_RecoveryWorkspaceFailed"));
			}
		}
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
						Node workspaceConnection = XmlUtilities.getChildElementNodeByName(rootNode, "WorkspaceConnection");
						if (workspaceConnection != null) {
							Node workspaceConnectionInfo = XmlUtilities.getChildElementNodeByName(workspaceConnection, "WorkspaceConnectionInfo");
							if (workspaceConnectionInfo != null) {
								Node server = XmlUtilities.getChildElementNodeByName(workspaceConnectionInfo, "Server");
								if (server != null) {
									NodeList childNodes = server.getChildNodes();
									if (childNodes != null && childNodes.getLength() > 0) {

										String workspacePath = childNodes.item(0).getNodeValue();
										if (!StringUtilities.isNullOrEmpty(workspacePath) && new File(workspacePath).exists()) {
											new File(workspacePath).delete();
										}
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
