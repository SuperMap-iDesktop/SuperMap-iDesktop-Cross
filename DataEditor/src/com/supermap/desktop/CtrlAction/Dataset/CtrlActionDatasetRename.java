package com.supermap.desktop.CtrlAction.Dataset;

import javax.swing.tree.TreePath;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.WorkspaceTree;

public class CtrlActionDatasetRename extends CtrlAction {

	public CtrlActionDatasetRename(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO
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
		Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
		if (datasets != null && datasets.length > 0 && !datasets[0].isReadOnly()) {
			enable = true;
		}
		return enable;
	}
}
