package com.supermap.desktop.popupmenus;

import javax.swing.tree.DefaultMutableTreeNode;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.WorkspaceComponentManager;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.utilties.TabularUtilties;

public class CtrlActionDatasetTabularBrowser extends CtrlAction {

	public CtrlActionDatasetTabularBrowser(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			WorkspaceComponentManager workspaceManager = UICommonToolkit.getWorkspaceManager();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceManager.getWorkspaceTree().getLastSelectedPathComponent();
			if (selectedNode != null) {
				TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
				if (selectedNodeData.getData() instanceof Dataset) {
					DatasetVector dataset = (DatasetVector) selectedNodeData.getData();
					if (dataset != null) {
						TabularUtilties.openDatasetVectorFormTabular(dataset);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}

}
