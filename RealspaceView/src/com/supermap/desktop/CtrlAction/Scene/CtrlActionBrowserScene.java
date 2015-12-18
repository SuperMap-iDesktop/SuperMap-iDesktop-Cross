package com.supermap.desktop.CtrlAction.Scene;

import javax.swing.tree.DefaultMutableTreeNode;

import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.dialog.DialogSaveAsScene;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.realspace.Scene;

public class CtrlActionBrowserScene extends CtrlAction {

	public CtrlActionBrowserScene(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			String sceneName = (String)selectedNodeData.getData();
			
			IFormScene formScene = (IFormScene) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.SCENE, sceneName);
			if (formScene != null) {
				Scene scene = formScene.getSceneControl().getScene();
				// add by huchenpu 20150706
				// 这里必须要设置工作空间，否则不能显示出来。
				// 而且不能在new SceneControl的时候就设置工作空间，必须等球显示出来的时候才能设置。
				scene.setWorkspace(Application.getActiveApplication().getWorkspace());
				scene.open(sceneName);
				scene.refresh();
				UICommonToolkit.getLayersManager().setScene(scene);
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
