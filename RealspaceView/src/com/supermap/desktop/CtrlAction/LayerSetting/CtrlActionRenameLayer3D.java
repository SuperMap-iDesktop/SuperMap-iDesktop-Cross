package com.supermap.desktop.CtrlAction.LayerSetting;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.Layer3DsTree;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.mapping.Layer;

public class CtrlActionRenameLayer3D extends CtrlAction {

	public CtrlActionRenameLayer3D(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			Layer3DsTree layer3DsTree = UICommonToolkit.getLayersManager().getLayer3DsTree();
			TreePath treeSelectionPath = layer3DsTree.getSelectionPaths()[0];
			layer3DsTree.setEditable(true);
			layer3DsTree.startEditingAtPath(treeSelectionPath);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		Layer3DsTree layer3DsTree = UICommonToolkit.getLayersManager().getLayer3DsTree();
		if (layer3DsTree != null && layer3DsTree.getSelectionCount() == 1) {
			enable = true;
		}

		return enable;
	}
}