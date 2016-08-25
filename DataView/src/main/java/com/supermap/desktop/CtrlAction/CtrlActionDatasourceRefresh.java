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
			Datasource refreshedDatasource = DatasourceUtilities.refreshDatasource(datasource);
			isReopened = (!isReopened && datasource != refreshedDatasource);
			datasources[i] = refreshedDatasource;
		}

		if (isReopened) {

			// 数据源 reopen 是关闭再打开的，因此需要重新选中结果数据
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
						if (!DatasourceUtilities.isMemoryDatasource(datasource)) {
							workspaceTree.refreshNode(selectedNode);
						}
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
				if (!DatasourceUtilities.isMemoryDatasource(datasource)) {
					enable = true;
					break;
				}
			}
		}
		return enable;
	}
}
