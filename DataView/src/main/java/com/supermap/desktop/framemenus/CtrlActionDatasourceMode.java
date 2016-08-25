package com.supermap.desktop.framemenus;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.DatasourceUtilities;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * Created by highsad on 2016/8/25.
 */
public class CtrlActionDatasourceMode extends CtrlAction {


	public CtrlActionDatasourceMode(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public boolean isReadOnly() {
		return false;
	}

	@Override
	public void run() {
		try {
			try {
				Workspace workspace = Application.getActiveApplication().getWorkspace();
				Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();

				// 重新打开之前先关闭地图
				for (int i = 0; i < datasources.length; i++) {
					Datasource datasource = datasources[i];
					DatasourceConnectionInfo info = DatasourceUtilities.cloneInfo(datasource.getConnectionInfo());
					info.setReadOnly(isReadOnly());
					DatasourceUtilities.closeDatasource(datasource);
					datasources[i] = workspace.getDatasources().open(info);
				}

				// 更改完成选中结果
				UICommonToolkit.getWorkspaceManager().selectDatasources(datasources);
				WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
				DefaultMutableTreeNode datasourcesNode = workspaceTree.getDatasourcesNode();

				if (datasourcesNode != null) {
					TreePath datasourcesPath = new TreePath(datasourcesNode.getPath());
					workspaceTree.expandPath(datasourcesPath);
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enabled = true;
		Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();

		if (datasources.length > 0) {
			for (int i = 0; i < datasources.length; i++) {
				Datasource datasource = datasources[i];

				if (datasource.getEngineType() != EngineType.UDB
						|| DatasourceUtilities.isMemoryDatasource(datasource)) {
					enabled = false;
				} else if (datasource.isReadOnly() == isReadOnly()) {
					enabled = false;
				}

				if (!enabled) {
					break;
				}
			}
		} else {
			enabled = false;
		}
		return enabled;
	}
}

