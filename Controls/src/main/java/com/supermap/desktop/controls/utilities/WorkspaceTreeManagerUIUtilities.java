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
 */
public class WorkspaceTreeManagerUIUtilities {

	public static final String WorkspaceIconPath = "/controlsresources/WorkspaceManager/Workspace/Image_WorkspaceDefault_Normal.png";
	public static final String DatasourcesIconPath = "/controlsresources/WorkspaceManager/Datasource/Datasources.png";
	public static final String MapsIconPath = "/controlsresources/WorkspaceManager/Map/Image_Map_Normal.png";
	public static final String MapIconPath = "/controlsresources/WorkspaceManager/Map/Maps.png";
	public static final String LayoutsIconPath = "/controlsresources/WorkspaceManager/Layout/Layouts.png";
	public static final String LayoutIconPath = "/controlsresources/WorkspaceManager/Layout/Image_Layout_New.png";
	public static final String ScenesIconPath = "/controlsresources/WorkspaceManager/Scene/Scenes.png";
	public static final String SceneIconPath = "/controlsresources/WorkspaceManager/Scene/Image_Scene_New.png";
	public static final String WorkFlowsIconPath = "/controlsresources/WorkspaceManager/WorkFlow/WorkFlows.png";
	public static final String WorkFlowIconPath = "/controlsresources/WorkspaceManager/WorkFlow/WorkFlow.png";
	public static final String SymbolIconPath = "/controlsresources/WorkspaceManager/Resources/Image_Resources_Normal.png";
	public static final String PointSymbolIconPath = "/controlsresources/WorkspaceManager/Resources/PointSymbol.png";
	public static final String LineSymbolIconPath = "/controlsresources/WorkspaceManager/Resources/LineSymbol.png";
	public static final String RegionSymbolIconPath = "/controlsresources/WorkspaceManager/Resources/RegionSymbol.png";

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
