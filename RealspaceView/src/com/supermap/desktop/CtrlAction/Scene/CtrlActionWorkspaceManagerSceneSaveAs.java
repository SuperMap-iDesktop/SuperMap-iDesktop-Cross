package com.supermap.desktop.CtrlAction.Scene;

import javax.swing.tree.DefaultMutableTreeNode;

import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.DialogSaveAsScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

public class CtrlActionWorkspaceManagerSceneSaveAs extends CtrlAction {

	public CtrlActionWorkspaceManagerSceneSaveAs(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			String sceneName = (String)selectedNodeData.getData();
			
			Workspace workspace = Application.getActiveApplication().getWorkspace();
			DialogSaveAsScene dialogSaveAs = new DialogSaveAsScene();
			dialogSaveAs.setScenes(workspace.getScenes());
			dialogSaveAs.setSceneName(sceneName);
			dialogSaveAs.setIsNewWindow(false);
			if (dialogSaveAs.showDialog() == DialogResult.YES) {
				String oldXML = workspace.getScenes().getSceneXML(sceneName);
				workspace.getScenes().add(dialogSaveAs.getSceneName(), oldXML);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		if (workspaceTree.getSelectionCount() == 1) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			if (selectedNodeData != null && selectedNodeData.getType() == NodeDataType.SCENE_NAME) {
				enable = true;
			}
		}
      return enable;
	}

}
