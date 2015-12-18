package com.supermap.desktop.CtrlAction.Layout;

import javax.swing.tree.DefaultMutableTreeNode;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.DialogSaveAsLayout;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

public class CtrlActionWorkspaceManagerLayoutSaveAs extends CtrlAction {

	public CtrlActionWorkspaceManagerLayoutSaveAs(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			String layoutName = (String) selectedNodeData.getData();

			Workspace workspace = Application.getActiveApplication().getWorkspace();
			DialogSaveAsLayout dialogSaveAs = new DialogSaveAsLayout();
			dialogSaveAs.setLayouts(workspace.getLayouts());
			dialogSaveAs.setLayoutName(layoutName);
			dialogSaveAs.setIsNewWindow(false);
			if (dialogSaveAs.showDialog() == DialogResult.YES) {
				String oldXML = workspace.getMaps().getMapXML(layoutName);
				workspace.getLayouts().add(dialogSaveAs.getLayoutName(), oldXML);
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
			if (selectedNodeData != null && selectedNodeData.getType() == NodeDataType.LAYOUT_NAME) {
				enable = true;
			}
		}
		return enable;
	}

}
