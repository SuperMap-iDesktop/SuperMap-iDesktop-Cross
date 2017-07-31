package com.supermap.desktop.WorkflowView.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.WorkFlowUtilities;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;


/**
 * @author XiaJT
 */
public class CtrlActionProcessDelete extends CtrlAction {
	public CtrlActionProcessDelete(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		ArrayList<IWorkflow> workflows = new ArrayList<>();
		for (TreePath treePath : workspaceTree.getSelectionPaths()) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) treeNode.getUserObject();
			Workflow workflow = (Workflow) selectedNodeData.getData();
			workflows.add(workflow);
		}
		WorkFlowUtilities.deleteProcess(workflows);
		ToolbarUIUtilities.updataToolbarsState();
	}


	@Override
	public boolean enable() {
		return true;
	}
}