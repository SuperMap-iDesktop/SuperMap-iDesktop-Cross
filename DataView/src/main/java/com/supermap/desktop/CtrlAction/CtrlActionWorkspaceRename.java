package com.supermap.desktop.CtrlAction;

import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.WorkspaceTree;

import javax.swing.tree.TreePath;

/**
 * Created by xie on 2017/9/4.
 */
public class CtrlActionWorkspaceRename extends CtrlAction {
	public CtrlActionWorkspaceRename(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath treeSelectionPath = workspaceTree.getSelectionPath();
		workspaceTree.setEditable(true);
		workspaceTree.startEditingAtPath(treeSelectionPath);
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		if (null != workspace) {
			enable = true;
		}
		return enable;
	}
}
