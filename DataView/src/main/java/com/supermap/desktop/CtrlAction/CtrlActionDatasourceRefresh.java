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
import com.supermap.desktop.utilities.DatasourceUtilities;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;
import java.text.MessageFormat;

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

				// 因为被占用而打开失败的文件型数据源的 reopen 组件不支持，自行实现
				if (!datasource.isOpened()) {
					DatasourceConnectionInfo srcInfo = datasource.getConnectionInfo();

					// 组件不提供判断占用的方法
					if (!isDatasourceOccupied(srcInfo.getServer())){
						Workspace workspace = datasource.getWorkspace();
						DatasourceConnectionInfo info=DatasourceUtilities.cloneInfo(srcInfo);

						datasource.close();
						Datasource newDatasource = null;
						try {
							newDatasource = workspace.getDatasources().open(info);
						} catch (Exception e) {
							Application.getActiveApplication().getOutput().output(MessageFormat.format(DataViewProperties.getString("String_RefreshDatasouce_Failed"), info.getAlias()));
						}
						if (newDatasource != null) {
							datasources[i] = newDatasource;
						} else {
							datasources[i] = null;
						}
						isReopened = true;
					}else{
						Application.getActiveApplication().getOutput().output(MessageFormat.format(DataViewProperties.getString("String_RefreshDatasouce_Failed"), srcInfo.getAlias()));
					}
				} else {
					datasource.refresh();
				}
			}
		}

		if (isReopened) {

			// 数据源 reopen 是关闭再打开，因此需要重新选中结果数据
			UICommonToolkit.getWorkspaceManager().selectDatasources(datasources);
		}

		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath[] treeSelectionPaths = workspaceTree.getSelectionPaths();
		if (treeSelectionPaths != null) {
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
	}

	/*
	* 判断指定的数据源是否被占用
	*/
	private boolean isDatasourceOccupied(String datasourcePath) {
		File file = new File(datasourcePath);
		return !file.renameTo(file);
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
