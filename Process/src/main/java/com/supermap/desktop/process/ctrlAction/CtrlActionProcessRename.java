package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.WorkspaceTree;

import javax.swing.tree.TreePath;

/**
 * @author XiaJT
 */
public class CtrlActionProcessRename extends CtrlAction {
	public CtrlActionProcessRename(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath treeSelectionPath = workspaceTree.getSelectionPath();
		workspaceTree.startEditingAtPath(treeSelectionPath);
	}

	@Override
	public boolean enable() {
		return UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getSelectionCount() == 1;
	}
}
