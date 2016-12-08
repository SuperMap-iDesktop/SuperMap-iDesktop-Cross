package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.Layouts;
import com.supermap.data.Maps;
import com.supermap.data.Scenes;
import com.supermap.data.Workspace;

import javax.swing.table.TableModel;

/**
 * @author YuanR
 */

public class GetTableModel {

	private TableModel workspaceTableModel;
	private TableModel datasourcesTableModel;
	private TableModel datasourceTableModel;
	private TableModel datasourceTypeTableModel;
	private TableModel mapsTableModel;
	private TableModel layoutsTableModel;
	private TableModel scenesTableModel;
	private TableModel resourcesTableModel;

	//获得资源model
	public TableModel getResourcesTableModel() {
		this.resourcesTableModel = new TableModelResources();
		return this.resourcesTableModel;
	}

	//获得工作空间（第一层）model
	public TableModel getWorkspaceTableModel(Workspace workspace) {
		this.workspaceTableModel = new TableModelWorkspace(workspace);
		return this.workspaceTableModel;
	}

	//获得数据源（第二层）model
	public TableModel getDatasourcesTableModel(Datasources datasources) {
		this.datasourcesTableModel = new TableModelDatasources(datasources);
		return this.datasourcesTableModel;
	}

	//获得地图（第二层）model
	public TableModel getMapsTableModel(Maps maps) {
		this.mapsTableModel = new TableModelMaps(maps);
		return this.mapsTableModel;
	}

	//获得布局（第二层）model
	public TableModel getLayoutsTableModel(Layouts layouts) {
		this.layoutsTableModel = new TableModelLayouts(layouts);
		return this.layoutsTableModel;
	}

	//获得场景（第二层）model
	public TableModel getScenesTableModel(Scenes scenes) {
		this.scenesTableModel = new TableModelScenes(scenes);
		return this.scenesTableModel;
	}

	//获得数据集（第三层）model
	public TableModel getDatasourceTableModel(Datasource datasource) {
		this.datasourceTableModel = new TableModelDatasource(datasource);
		return this.datasourceTableModel;
	}

	//获得特定类型数据集（第三层）model
	public TableModel getDatasourceTypeTableModel(Datasource datasource, DatasetType datasetType) {
		this.datasourceTypeTableModel = new TableModelDatasType(datasource, datasetType);
		return this.datasourceTypeTableModel;
	}
}

