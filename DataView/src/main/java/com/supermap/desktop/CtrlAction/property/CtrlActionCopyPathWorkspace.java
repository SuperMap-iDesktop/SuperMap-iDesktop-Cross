package com.supermap.desktop.CtrlAction.property;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.ClipBoardUtilties;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * Created by lixiaoyao on 2017/9/8.
 */
public class CtrlActionCopyPathWorkspace extends CtrlAction {

	public CtrlActionCopyPathWorkspace(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath selectedPath = workspaceTree.getSelectionPath();
		try {
			if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
				TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();
				if (nodeData.getType() == NodeDataType.WORKSPACE) {
					Workspace workspace = (Workspace) nodeData.getData();
					ClipBoardUtilties.setSysClipboardText(workspace.getConnectionInfo().getServer());
				}
				Application.getActiveApplication().getOutput().output(DataViewProperties.getString("String_CopyFilePath"));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			enable = this.isFileWorkspace();
			if (Application.getActiveApplication().getWorkspace().getCaption().equals("UntitledWorkspace")){
				enable=false;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}


	boolean isFileWorkspace() {
		boolean result = false;
		try {
			if (Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.SMW
					|| Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.SMWU
					|| Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.SXW
					|| Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.SXWU) {
				result = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}
}
