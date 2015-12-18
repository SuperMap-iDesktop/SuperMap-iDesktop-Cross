package com.supermap.desktop.CtrlAction.Scene;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

public class CtrlActionSceneDelete extends CtrlAction {

	public CtrlActionSceneDelete(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		ArrayList<String> sceneNames = new ArrayList<String>();
		for (TreePath treePath : workspaceTree.getSelectionPaths()) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) treeNode.getUserObject();
			sceneNames.add(selectedNodeData.getData().toString());
		}
		CommonToolkit.SceneWrap.deleteScenes(sceneNames.toArray(new String[sceneNames.size()]));
	}

	@Override
	public boolean enable() {
		boolean enable = false;		
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		if (workspaceTree != null && workspaceTree.getSelectionCount() > 0) {
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
