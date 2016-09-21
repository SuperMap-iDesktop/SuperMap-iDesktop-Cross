package com.supermap.desktop.CtrlAction;

import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceClosingEvent;
import com.supermap.data.WorkspaceClosingListener;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceOpenedEvent;
import com.supermap.data.WorkspaceOpenedListener;
import com.supermap.data.WorkspaceSavedAsEvent;
import com.supermap.data.WorkspaceSavedAsListener;
import com.supermap.data.WorkspaceSavedEvent;
import com.supermap.data.WorkspaceSavedListener;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.utilities.DatasourceUtilities;
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
import java.lang.reflect.Field;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XiaJT
 */
public class WorkspaceTempSave {

	private static WorkspaceTempSave workspaceTempSave = null;
	private Timer timer;
	private TimerTask task;
	private String defaultName = "tempWorkspace";
	private File autoSaveWorkspaceConfigFile;
	private FileLock fileLock;
	private RandomAccessFile randomAccessFile;
	private Workspace workspace;
	private long nextSaveSymbolTime = 0;
	// 手动保存(另存)的时候会保存一次;
	// 工作空间关闭的时候会重置符号库计数，下次保存的时候会保存符号库
	// 工作空间打开会保存一次
	private WorkspaceOpenedListener workspaceOpenedListener = new WorkspaceOpenedListener() {
		@Override
		public void workspaceOpened(WorkspaceOpenedEvent workspaceOpenedEvent) {
			nextSaveSymbolTime = 0;
			autoSave(true);
		}
	};
	private WorkspaceClosingListener workspaceClosingListener = new WorkspaceClosingListener() {
		@Override
		public void workspaceClosing(WorkspaceClosingEvent workspaceClosingEvent) {
			// 工作空间关闭可能是要打开当前保存的工作空间，所以先关闭一次
			closeTempWorkspace();
			nextSaveSymbolTime = 0;
		}
	};

	private String lastServer;
	private WorkspaceSavedListener workspaceSavedListener = new WorkspaceSavedListener() {
		@Override
		public void workspaceSaved(WorkspaceSavedEvent workspaceSavedEvent) {
			autoSave(true);
		}
	};

	private WorkspaceSavedAsListener workspaceSavedAsListener = new WorkspaceSavedAsListener() {
		@Override
		public void workspaceSavedAs(WorkspaceSavedAsEvent workspaceSavedAsEvent) {
			autoSave(true);
		}
	};


	private WorkspaceTempSave() {

	}

	public void start() {
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
		if (task != null) {
			task.cancel();
		}
		task = new TimerTask() {
			@Override
			public void run() {
				autoSave(false);
			}
		};
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer("WorkspaceTempSave", true);
		addListeners();
		timer.schedule(task, 60000, GlobalParameters.getWorkspaceAutoSaveTime() * 60000);
	}

	private void addListeners() {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		workspace.addOpenedListener(workspaceOpenedListener);
		workspace.addClosingListener(workspaceClosingListener);
		workspace.addSavedListener(workspaceSavedListener);
		workspace.addSavedAsListener(workspaceSavedAsListener);
	}

	private void removeListeners() {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		workspace.removeOpenedListener(workspaceOpenedListener);
		workspace.removeClosingListener(workspaceClosingListener);
		workspace.removeSavedListener(workspaceSavedListener);
		workspace.removeSavedAsListener(workspaceSavedAsListener);
	}

	private String getFilePath() {
		String appDataPath = FileUtilities.getAppDataPath();

		if (appDataPath == null) {
			LogUtilities.outPut("get AppData path failed. AutoSave exit.");
			return null;
		}
		appDataPath += "tempWorkspace" + File.separator;

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

	private void autoSave(boolean isIgnoreModified) {
		Workspace activeWorkspace = Application.getActiveApplication().getWorkspace();

		synchronized (activeWorkspace) {
			if (!isIgnoreModified && !WorkspaceUtilities.isWorkspaceModified()) {
				return;
			}
			activeWorkspace.addClosingListener(workspaceClosingListener);
			WorkspaceConnectionInfo connectionInfo = activeWorkspace.getConnectionInfo();
			WorkspaceType type = connectionInfo.getType();
			if (type != WorkspaceType.DEFAULT && type != WorkspaceType.SMWU && type != WorkspaceType.SXWU) {
				// 当前工作空间不属于需要保存的类型
				WorkspaceUtilities.deleteFileWorkspace(lastServer);
				lastServer = null;
				closeTempWorkspace();
				WorkspaceUtilities.deleteFileWorkspace(lastServer);
				lastServer = null;
				try {
					randomAccessFile.setLength(0);
				} catch (IOException e) {
					LogUtilities.outPut("database workspace delete config failed");
				}
			} else {
				if (workspace == null) {
					workspace = new Workspace();
				}

				WorkspaceConnectionInfo workspaceConnectionInfo = null;
				if (workspace.getType() != WorkspaceType.DEFAULT && workspace.getVersion() == activeWorkspace.getVersion() && (workspace.getType() == activeWorkspace.getType() || activeWorkspace.getType() == WorkspaceType.DEFAULT)) {
					if (!workspace.getConnectionInfo().getPassword().equals(activeWorkspace.getConnectionInfo().getPassword())) {
						workspace.changePassword(workspace.getConnectionInfo().getPassword(), activeWorkspace.getConnectionInfo().getPassword());
					}
				} else {
					// 之前手动保存的工作空间删除
					WorkspaceUtilities.deleteFileWorkspace(lastServer);
					lastServer = null;

					closeTempWorkspace();
					if (workspace == null) {
						workspace = new Workspace();
					}
					WorkspaceUtilities.deleteFileWorkspace(lastServer);
					lastServer = null;

					String tempWorkspaceFilePath = getTempWorkspaceFilePath(connectionInfo.getType());
					workspaceConnectionInfo = new WorkspaceConnectionInfo(tempWorkspaceFilePath);
					workspaceConnectionInfo.setServer(tempWorkspaceFilePath);
					workspaceConnectionInfo.setType(connectionInfo.getType() != WorkspaceType.SXWU ? WorkspaceType.SMWU : WorkspaceType.SXWU);
					workspaceConnectionInfo.setVersion(connectionInfo.getVersion());
					workspaceConnectionInfo.setUser(connectionInfo.getUser());
					workspaceConnectionInfo.setPassword(connectionInfo.getPassword());
				}

				workspace = WorkspaceUtilities.copyWorkspace(activeWorkspace, workspace);
				boolean saveSuccess;
				if (workspaceConnectionInfo != null) {
					saveSuccess = workspace.saveAs(workspaceConnectionInfo);
				} else {
					saveSuccess = workspace.save();
				}
				if (System.currentTimeMillis() >= nextSaveSymbolTime) {
					saveSymbolLibrary(activeWorkspace);
				}
				if (saveSuccess) {
					if (!saveToFile(activeWorkspace, workspace)) {
						LogUtilities.outPut("save config autoSaveWorkspaceConfigFile failed");
					}
				} else {
					LogUtilities.outPut("workspace autoSave failed");
				}
			}
		}
		if (isIgnoreModified) {
			// 无视改动时要更改保存时间
			resetNextSaveTime();
		}
	}

	private void resetNextSaveTime() {
		Class<TimerTask> clazz = TimerTask.class;
		try {
			Field field = clazz.getDeclaredField("nextExecutionTime");
			field.setAccessible(true);
			// 修改之后刷新时间
			field.setLong(task, System.currentTimeMillis() + GlobalParameters.getWorkspaceAutoSaveTime() * 600000);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void closeTempWorkspace() {
		if (workspace == null) {
			return;
		}
		if (!StringUtilities.isNullOrEmpty(workspace.getConnectionInfo().getServer())) {
			lastServer = workspace.getConnectionInfo().getServer();
			workspace.close();
			workspace = null;
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
		Element workspaceConnectionNode = WorkspaceConnectionInfoUtilities.toXml(workspace.getConnectionInfo(), document);
		rootNode.appendChild(workspaceConnectionNode);

		// 数据源连接信息
		Element datasourcesNode = document.createElement("Datasources");
		Datasources datasources = activeWorkspace.getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).getEngineType() == EngineType.UDB && datasources.get(i).isOpened() && !DatasourceUtilities.isMemoryDatasource(datasources.get(i))) {
				Element datasource = document.createElement("Datasource");
				byte[] bytes;
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
			try {
				randomAccessFile.setLength(0);
			} catch (IOException e) {
				LogUtilities.outPut("file workspace delete config failed");
			}
			randomAccessFile.seek(0);
			String string = XmlUtilities.nodeToString(rootNode, "UTF-8");
			randomAccessFile.write(string.getBytes("UTF-8"));
		} catch (Exception e) {
			LogUtilities.outPut("write to config file failed.");
			return false;
		}
		return true;
	}

	private String getTempWorkspaceFilePath(WorkspaceType type) {
		String fileSuffixes = type == WorkspaceType.SXWU ? ".sxwu" : ".smwu";
		String prefixes = autoSaveWorkspaceConfigFile.getPath().substring(0, autoSaveWorkspaceConfigFile.getPath().length() - 4);// 去掉.xml
		int i = 0;
		for (; new File(prefixes + (i == 0 ? "" : "_" + i) + fileSuffixes).exists(); i++) ;
		return prefixes + (i == 0 ? "" : "_" + i) + fileSuffixes;
	}


	public boolean exit() {
		removeListeners();
		if (task != null) {
			task.cancel();
		}
		if (timer != null) {
			timer.cancel();
		}
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
		if (autoSaveWorkspaceConfigFile != null && autoSaveWorkspaceConfigFile.exists()) {
			if (!autoSaveWorkspaceConfigFile.delete()) {
				LogUtilities.outPut("Delete AutoSaveWorkspaceConfigFile Failed On Exit ");
			}
		}
		if (workspace != null) {
			lastServer = workspace.getConnectionInfo().getServer();
			workspace.close();
			workspace = null;
		}
		if (lastServer != null && !WorkspaceUtilities.deleteFileWorkspace(lastServer)) {
			LogUtilities.outPut("Delete TempWorkspace Failed On Exit ");
		}
		return false;
	}

	public void restart() {
		exit();
		start();
	}

	private void saveSymbolLibrary(Workspace currentWorkspace) {
		nextSaveSymbolTime = System.currentTimeMillis() + GlobalParameters.getSymbolSaveTime() * 60000;
		String server = workspace.getConnectionInfo().getServer();
//		Application.getActiveApplication().getOutput().output(server);
		String tempFolder = server.substring(0, server.length() - 5);

		String markerSymbolFilePath = tempFolder + ".sym";
		if (new File(markerSymbolFilePath).exists()) {
			new File(markerSymbolFilePath).delete();
		}
		currentWorkspace.getResources().getMarkerLibrary().toFile(markerSymbolFilePath);
		String lineSymbolFilePath = tempFolder + ".lsl";
		if (new File(lineSymbolFilePath).exists()) {
			new File(lineSymbolFilePath).delete();
		}
		currentWorkspace.getResources().getLineLibrary().toFile(lineSymbolFilePath);
		String fillSymbolFilePath = tempFolder + ".bru";
		if (new File(fillSymbolFilePath).exists()) {
			new File(fillSymbolFilePath).delete();
		}
		currentWorkspace.getResources().getFillLibrary().toFile(fillSymbolFilePath);
	}

	public static WorkspaceTempSave getInstance() {
		if (workspaceTempSave == null) {
			workspaceTempSave = new WorkspaceTempSave();
		}
		return workspaceTempSave;
	}

	public void setAutoSaveTime(int oldValue, int saveTime) {
		if (task == null) {
			return;
		}
		Class<TimerTask> clazz = TimerTask.class;
		try {
			Field period = clazz.getDeclaredField("period");
			period.setAccessible(true);
			period.set(task, saveTime * 60000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resetNextSaveTime();
	}

	public void setSymbolSaveTime(int oldValue, int saveTime) {
		if (nextSaveSymbolTime != 0) {
			nextSaveSymbolTime = System.currentTimeMillis() + saveTime * 60000;
		}
	}
}
