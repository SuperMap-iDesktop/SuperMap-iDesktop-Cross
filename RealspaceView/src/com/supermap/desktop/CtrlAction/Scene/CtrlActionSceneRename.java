package com.supermap.desktop.CtrlAction.Scene;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

public class CtrlActionSceneRename extends CtrlAction {

	public CtrlActionSceneRename(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	
	@Override
	public void run(){
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath treeSelectionPath = workspaceTree.getSelectionPath();
		workspaceTree.setEditable(true);
		workspaceTree.startEditingAtPath(treeSelectionPath);
	}
	
	@Override
	public boolean enable() {
		boolean enable = false;		
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		if (workspaceTree != null && workspaceTree.getSelectionCount() == 1) {
			TreePath treeSelectionPath = workspaceTree.getSelectionPath();
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treeSelectionPath.getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData)treeNode.getUserObject();
			if (selectedNodeData != null && selectedNodeData.getType() == NodeDataType.SCENE_NAME) {
				enable = true;
			}
		}

		return enable;
	}
}
