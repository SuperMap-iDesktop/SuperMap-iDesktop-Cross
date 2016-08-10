package com.supermap.desktop.controls.utilities;

import com.supermap.data.Datasource;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.WorkspaceComponentManager;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 工作空间管理器工具类
 * 
 * @author highsad
 *
 */
public class WorkspaceTreeManagerUIUtilities {

	/**
	 * 刷新指定数据源的树节点
	 * 
	 * @param datasource
	 */
	public static void refreshNode(Datasource datasource) {
		WorkspaceComponentManager manager = UICommonToolkit.getWorkspaceManager();
		if (manager != null) {
			final WorkspaceTree workspaceTree = manager.getWorkspaceTree();
			DefaultMutableTreeNode datasourcesNode = workspaceTree.getDatasourcesNode();

			for (int i = 0; i < datasourcesNode.getChildCount(); i++) {
				final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) datasourcesNode.getChildAt(i);

				if (childNode != null) {
					TreeNodeData childNodeData = (TreeNodeData) childNode.getUserObject();

					if (childNodeData != null && childNodeData.getData() == datasource) {
						workspaceTree.refreshNode(childNode);
						break;
					}
				}
			}
		}
	}

	private WorkspaceTreeManagerUIUtilities() {
		// 工具类不提供构造函数
	}
}
