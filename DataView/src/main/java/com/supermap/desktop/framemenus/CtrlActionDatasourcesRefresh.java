package com.supermap.desktop.framemenus;

import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.DatasourceUtilities;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * Created by highsad on 2016/8/24.
 */
public class CtrlActionDatasourcesRefresh extends CtrlAction {

	public CtrlActionDatasourcesRefresh(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		boolean isReopened = false; // 是否重新打开过数据源
		Workspace workspace = Application.getActiveApplication().getWorkspace();

		for (int i = workspace.getDatasources().getCount() - 1; i >= 0; i--) {
			Datasource datasource = workspace.getDatasources().get(i);
			DatasourceUtilities.refreshDatasource(datasource);
		}

		// 刷新完成展开数据源集合节点
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		DefaultMutableTreeNode datasourcesNode = workspaceTree.getDatasourcesNode();

		if (datasourcesNode != null) {
			TreePath datasourcesPath = new TreePath(datasourcesNode.getPath());
			workspaceTree.expandPath(datasourcesPath);
		}
	}

	/**
	 * 数据源个数大于0即可刷新
	 *
	 * @return
	 */
	@Override
	public boolean enable() {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		return workspace != null && workspace.getDatasources().getCount() > 0;
	}
}
