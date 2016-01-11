package com.supermap.desktop.netservices.iserver;

import java.util.ArrayList;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;

public class WorkspaceInfo {
	private WorkspaceConnectionInfo workspaceConnectionInfo;
	private String workspacePath;
	private ArrayList<String> datasourcesPath;
	private ArrayList<DatasetType> containTypes;
	private int layoutCounts;
	private int mapCounts;
	private int sceneCounts;

	public WorkspaceConnectionInfo getWorkspaceConnectionInfo() {
		return this.workspaceConnectionInfo;
	}

	public String getWorkspacePath() {
		return this.workspacePath;
	}

	public ArrayList<String> getDatasourcesPath() {
		return this.datasourcesPath;
	}

	public ArrayList<DatasetType> getContainTypes() {
		return this.containTypes;
	}

	public int getLayoutCounts() {
		return this.layoutCounts;
	}

	public int getMapCounts() {
		return this.mapCounts;
	}

	public int getSceneCounts() {
		return this.sceneCounts;
	}

	static public WorkspaceInfo getCurrentWorkspaceInfo() {
		WorkspaceInfo workspaceInfo = null;
		if (Application.getActiveApplication().getWorkspace() != null) {
			workspaceInfo = new WorkspaceInfo(Application.getActiveApplication().getWorkspace());
		}
		return workspaceInfo;
	}

	private WorkspaceInfo(Workspace workspace) {
		try {
			this.layoutCounts = 0;
			this.mapCounts = 0;
			this.sceneCounts = 0;
			this.workspacePath = "";
			this.datasourcesPath = new ArrayList<String>();

			if (workspace != null) {
				this.containTypes = new ArrayList<DatasetType>();
				// 拷贝一个，因为可能会在发布失败之后重新打开，传引用会拿到一个空的Info
				WorkspaceConnectionInfo sourceConnection = workspace.getConnectionInfo();
				this.workspaceConnectionInfo = new WorkspaceConnectionInfo();
				this.workspaceConnectionInfo.setDatabase(sourceConnection.getDatabase());
				this.workspaceConnectionInfo.setDriver(sourceConnection.getDriver());
				this.workspaceConnectionInfo.setName(sourceConnection.getName());
				this.workspaceConnectionInfo.setPassword(sourceConnection.getPassword());
				this.workspaceConnectionInfo.setServer(sourceConnection.getServer());
				this.workspaceConnectionInfo.setType(sourceConnection.getType());
				this.workspaceConnectionInfo.setUser(sourceConnection.getUser());
				this.workspaceConnectionInfo.setVersion(sourceConnection.getVersion());

				// 记录文件型工作空间的路径与数据源的路径
				if (IsFileWorkspace(workspace.getType())) {
					this.workspacePath = workspace.getConnectionInfo().getServer();
					FillDatasourcesPathList(workspace);
				}
				FillContainTypesList(workspace);
				this.layoutCounts = workspace.getLayouts().getCount();
				this.mapCounts = workspace.getMaps().getCount();
				this.sceneCounts = workspace.getScenes().getCount();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void FillDatasourcesPathList(Workspace workspace) {
		this.datasourcesPath.clear();
		for (int i = 0; i < workspace.getDatasources().getCount(); i++) {
			Datasource datasource = workspace.getDatasources().get(i);
			if (datasource.getEngineType() == EngineType.UDB) {
				String datasourceServer = datasource.getConnectionInfo().getServer().toLowerCase();
				this.datasourcesPath.add(datasourceServer);
				this.datasourcesPath.add(datasourceServer.replace("udb", "udd"));
			}
		}
	}

	private Boolean IsFileWorkspace(WorkspaceType type) {
		return (type == WorkspaceType.SMW || type == WorkspaceType.SMWU || type == WorkspaceType.SXW || type == WorkspaceType.SXWU || type == WorkspaceType.DEFAULT);
	}

	private void FillContainTypesList(Workspace workspace) {
		if (workspace != null && workspace.getDatasources() != null && workspace.getDatasources().getCount() > 0) {
			for (int i = 0; i < workspace.getDatasources().getCount(); i++) {
				Datasource datasource = workspace.getDatasources().get(i);
				if (datasource.getDatasets() != null && datasource.getDatasets().getCount() > 0) {
					for (int index = 0; index < datasource.getDatasets().getCount(); index++) {
						Dataset dataset = datasource.getDatasets().get(index);
						if (!this.containTypes.contains(dataset.getType())) {
							this.containTypes.add(dataset.getType());
						}
					}
				}
			}
		}
	}
}
