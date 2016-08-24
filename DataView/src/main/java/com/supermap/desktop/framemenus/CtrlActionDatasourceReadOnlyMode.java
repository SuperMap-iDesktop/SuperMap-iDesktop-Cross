package com.supermap.desktop.framemenus;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.utilities.DatasourceOpenFileUtilties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.DatasourceUtilities;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * Created by highsad on 2016/8/25.
 */
public class CtrlActionDatasourceReadOnlyMode extends CtrlActionDatasourceMode {

	public CtrlActionDatasourceReadOnlyMode(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}
}