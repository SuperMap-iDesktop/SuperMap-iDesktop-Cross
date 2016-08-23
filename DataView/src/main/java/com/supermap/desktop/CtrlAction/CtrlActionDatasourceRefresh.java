package com.supermap.desktop.CtrlAction;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class CtrlActionDatasourceRefresh extends CtrlAction {

	public CtrlActionDatasourceRefresh(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		boolean isReopened = false; // 是否重新打开过数据源
		Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();

		for (int i = 0; i < datasources.length; i++) {
			Datasource datasource = datasources[i];

			// 内存数据源不能刷新
			if (!datasource.getConnectionInfo().getServer().equalsIgnoreCase(DataViewProperties.getString("String_DatasourceServer_Memory"))) {
				if (!datasource.isOpened()) {
					Workspace workspace = datasource.getWorkspace();
					DatasourceConnectionInfo srcInfo = datasource.getConnectionInfo();
					DatasourceConnectionInfo info = new DatasourceConnectionInfo();
					info.setAlias(srcInfo.getAlias());
					info.setServer(srcInfo.getServer());
					info.setUser(srcInfo.getUser());
					info.setPassword(srcInfo.getPassword());

					datasource.close();
					Datasource newDatasource = workspace.getDatasources().open(info);
					if (newDatasource != null && newDatasource.isOpened()) {
						datasources[i] = datasource;
					}
					isReopened = true;
				} else {
					datasource.refresh();
				}
			}
		}

		if (isReopened) {
			Application.getActiveApplication().setActiveDatasources(datasources);
		}

		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath[] treeSelectionPaths = workspaceTree.getSelectionPaths();
		for (TreePath treePath : treeSelectionPaths) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			if (selectedNodeData != null) {
				Object nodeData = selectedNodeData.getData();
				if (nodeData instanceof Datasource) {
					Datasource datasource = (Datasource) nodeData;
					if (!datasource.getConnectionInfo().getServer().equalsIgnoreCase(DataViewProperties.getString("String_DatasourceServer_Memory"))) {
						workspaceTree.refreshNode(selectedNode);
					}
				}
			}
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();
		if (datasources != null && datasources.length > 0) {
			for (Datasource datasource : datasources) {
				// 内存数据源不能刷新
				if (!datasource.getConnectionInfo().getServer().equalsIgnoreCase(DataViewProperties.getString("String_DatasourceServer_Memory"))) {
					enable = true;
					break;
				}
			}
		}
		return enable;
	}
}
