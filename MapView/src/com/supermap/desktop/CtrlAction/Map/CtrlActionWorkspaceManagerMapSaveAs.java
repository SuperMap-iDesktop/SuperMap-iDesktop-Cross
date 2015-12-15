package com.supermap.desktop.CtrlAction.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.DialogSaveAsMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

public class CtrlActionWorkspaceManagerMapSaveAs extends CtrlAction {

	public CtrlActionWorkspaceManagerMapSaveAs(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			String mapName = (String)selectedNodeData.getData();
			
			Workspace workspace = Application.getActiveApplication().getWorkspace();
			DialogSaveAsMap dialogSaveAs = new DialogSaveAsMap();
			dialogSaveAs.setMaps(workspace.getMaps());
			dialogSaveAs.setMapName(mapName, false);
			dialogSaveAs.setIsNewWindow(false);
			if (dialogSaveAs.showDialog() == DialogResult.YES) {
				String oldXML = workspace.getMaps().getMapXML(mapName);
				workspace.getMaps().add(dialogSaveAs.getMapName(), oldXML, Application.getActiveApplication().getWorkspace().getVersion());
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
			if (selectedNodeData != null && selectedNodeData.getType() == NodeDataType.MAP_NAME) {
				enable = true;
			}
		}
      return enable;
	}

}
