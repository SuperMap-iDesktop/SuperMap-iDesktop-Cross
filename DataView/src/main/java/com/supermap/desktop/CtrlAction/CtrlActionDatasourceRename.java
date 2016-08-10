package com.supermap.desktop.CtrlAction;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.WorkspaceTree;

import javax.swing.tree.TreePath;

public class CtrlActionDatasourceRename extends CtrlAction {

	public CtrlActionDatasourceRename(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	
	@Override
	public void run(){
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath treeSelectionPath = workspaceTree.getSelectionPath();
		workspaceTree.setEditable(true);
		workspaceTree.startEditingAtPath(treeSelectionPath);
	}
	
	@Override
	public boolean enable() {
		boolean enable = false;		
		Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();
		if (datasources != null && datasources.length > 0) {
			enable = true;
		}
		return enable;
	}
}
