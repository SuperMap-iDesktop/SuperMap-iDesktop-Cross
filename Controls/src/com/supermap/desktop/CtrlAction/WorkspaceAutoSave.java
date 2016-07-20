package com.supermap.desktop.CtrlAction;

import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.LogUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.WorkspaceConnectionInfoUtilities;
import com.supermap.desktop.utilities.WorkspaceUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XiaJT
 */
public class WorkspaceAutoSave {

	private static WorkspaceAutoSave workspaceAutoSave = null;
	private Timer timer;
	private TimerTask task;
	private int period = 60000; // 1 min
	private String defaultName = "tempWorkspace";
	private File autoSaveWorkspaceConfigFile;
	private FileLock fileLock;
	private RandomAccessFile randomAccessFile;
	private File tempWorkspaceFile;
	private Workspace workspace;

	private WorkspaceAutoSave() {
		// 获取数据目录
		String filePath = getFilePath();
		if (StringUtilities.isNullOrEmpty(filePath)) {
			return;
		}
		autoSaveWorkspaceConfigFile = new File(filePath);
		try {
			if ((new File(autoSaveWorkspaceConfigFile.getParent()).exists() || new File(autoSaveWorkspaceConfigFile.getParent()).mkdirs()) && autoSaveWorkspaceConfigFile.createNewFile()) {
				randomAccessFile = new RandomAccessFile(autoSaveWorkspaceConfigFile, "rw");
				fileLock = randomAccessFile.getChannel().tryLock();
				if (fileLock == null || !fileLock.isValid()) {
					throw new IOException();
				}
			}
		} catch (IOException e) {
			exit();
			autoSaveWorkspaceConfigFile = null;
			LogUtilities.outPut("create new autoSaveWorkspaceConfigFile failed. AutoSave exit.");
			return;
		}
		timer = new Timer("WorkspaceSave", true);
		task = new TimerTask() {
			@Override
			public void run() {
				autoSave();
			}
		};
	}

	public void start() {
		timer.schedule(task, period, period);
	}

	private String getFilePath() {
		String appDataPath = FileUtilities.getAppDataPath();

		if (appDataPath == null) {
			LogUtilities.outPut("get AppData path failed. AutoSave exit.");
			return null;
		}
		File file = new File(appDataPath);
		File[] listFiles = file.listFiles();
		ArrayList<String> list = new ArrayList<>();
		if (listFiles != null && listFiles.length > 0) {
			for (File file1 : listFiles) {
				if (file1.getName().toLowerCase().endsWith(".xml")) {
					list.add(file1.getName().substring(0, file1.getName().length() - 4));
				}
			}
		}
		return appDataPath + StringUtilities.getUniqueName(defaultName, list) + ".xml";
	}

	private void autoSave() {
		Workspace activeWorkspace = Application.getActiveApplication().getWorkspace();
		WorkspaceConnectionInfo connectionInfo = activeWorkspace.getConnectionInfo();
		WorkspaceType type = connectionInfo.getType();
		if (type != WorkspaceType.DEFAULT && type != WorkspaceType.SMWU && type != WorkspaceType.SXWU) {
			if (tempWorkspaceFile != null && tempWorkspaceFile.exists()) {
				tempWorkspaceFile.delete();

			}
			try {
				randomAccessFile.setLength(0);
			} catch (IOException e) {
				LogUtilities.outPut("database workspace delete config failed");
			}
		} else {
			WorkspaceConnectionInfo workspaceConnectionInfo = new WorkspaceConnectionInfo(getTempWorkspaceFilePath());
			workspaceConnectionInfo.setServer(getTempWorkspaceFilePath() + (connectionInfo.getServer().toLowerCase().endsWith("sxwu") ? ".sxwu" : ".smwu"));
			workspaceConnectionInfo.setType(connectionInfo.getType() != WorkspaceType.SXWU ? WorkspaceType.SMWU : WorkspaceType.SMWU);
			workspaceConnectionInfo.setVersion(connectionInfo.getVersion());
			workspaceConnectionInfo.setUser(connectionInfo.getUser());
			workspaceConnectionInfo.setPassword(connectionInfo.getPassword());

			if (workspace == null) {
				workspace = new Workspace();
			}
			workspace = WorkspaceUtilities.copyWorkspace(activeWorkspace, workspace);
			if (workspace.saveAs(workspaceConnectionInfo)) {
				if (!saveToFile(activeWorkspace, workspace)) {
					LogUtilities.outPut("save config autoSaveWorkspaceConfigFile failed");
				}
				tempWorkspaceFile = new File(workspace.getConnectionInfo().getServer());
			} else {
				LogUtilities.outPut("workspace autoSave failed");
			}

		}
	}

	private boolean saveToFile(Workspace activeWorkspace, Workspace workspace) {
		Document document = XmlUtilities.getEmptyDocument();
		if (document == null) {
			return false;
		}
		Element rootNode = document.createElement("AutoSaveWorkspace");

		Element workspacePath = document.createElement("WorkspacePath");
		workspacePath.appendChild(document.createTextNode(activeWorkspace.getConnectionInfo().getServer() == null ? "" : activeWorkspace.getConnectionInfo().getServer()));
		rootNode.appendChild(workspacePath);

		// 工作空间连接信息
		Element workspaceConnectionInfoNode = document.createElement("WorkspaceConnection");
		Element workspaceConnectionNode = WorkspaceConnectionInfoUtilities.toXml(workspace.getConnectionInfo(), document);
		workspaceConnectionInfoNode.appendChild(workspaceConnectionNode);
		rootNode.appendChild(workspaceConnectionInfoNode);

		// 数据源连接信息
		Element datasourcesNode = document.createElement("Datasources");
		Datasources datasources = activeWorkspace.getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).getEngineType() == EngineType.UDB && !datasources.get(i).isReadOnly() && datasources.get(i).isOpened()) {
				Element datasource = document.createElement("Datasource");
				byte[] bytes = null;
				try {
					bytes = datasources.get(i).getConnectionInfo().toXML().getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					LogUtilities.outPut("EncodingFailed");
					return false;
				}
				datasource.appendChild(document.createTextNode(new BASE64Encoder().encode(bytes)));
				datasourcesNode.appendChild(datasource);
			}
		}
		rootNode.appendChild(datasourcesNode);
		try {
			if (tempWorkspaceFile != null && tempWorkspaceFile.exists()) {
				if (!tempWorkspaceFile.delete()) {
					LogUtilities.outPut("deleteFailed");
				}
				try {
					randomAccessFile.setLength(0);
				} catch (IOException e) {
					LogUtilities.outPut("file workspace delete config failed");
				}
			}
			randomAccessFile.seek(0);
			String string = XmlUtilities.nodeToString(rootNode, "UTF-8");
			randomAccessFile.write(string.getBytes());
		} catch (Exception e) {
			LogUtilities.outPut("write to config file failed.");
			return false;
		}
		return true;
	}

	private String getTempWorkspaceFilePath() {
		return autoSaveWorkspaceConfigFile.getPath().substring(0, autoSaveWorkspaceConfigFile.getPath().length() - 4);// 去掉.xml
	}


	public boolean exit() {
		task.cancel();
		if (fileLock != null) {
			try {
				fileLock.release();
			} catch (IOException e) {
				// ignore
			}
		}
		if (randomAccessFile != null) {
			try {
				randomAccessFile.close();
			} catch (IOException e) {
				// ignore
			}
		}
		if (autoSaveWorkspaceConfigFile.exists()) {
			if (!autoSaveWorkspaceConfigFile.delete()) {
				LogUtilities.outPut("Delete AutoSaveWorkspaceConfigFile Failed On Exit ");
			}
		}
		if (workspace != null) {
			workspace.close();
		}
		if (tempWorkspaceFile != null && tempWorkspaceFile.exists()) {
			if (!tempWorkspaceFile.delete()) {
				LogUtilities.outPut("Delete TempWorkspaceFile Failed On Exit ");
			}
		}
		return false;
	}



	public static WorkspaceAutoSave getInstance() {
		if (workspaceAutoSave == null) {
			workspaceAutoSave = new WorkspaceAutoSave();
		}
		return workspaceAutoSave;
	}


}
