package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.FormScene;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.Layer3DsTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.realspace.Layer3DDataset;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class CtrlActionLayer3DStyle extends CtrlAction {

	public CtrlActionLayer3DStyle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			FormScene formScene = (FormScene) Application.getActiveApplication().getActiveForm();
			formScene.showStyleSetDialog();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			Layer3DsTree layer3DsTree = UICommonToolkit.getLayersManager().getLayer3DsTree();
			if (null != layer3DsTree.getSelectionPaths()) {
				TreePath[] selections = layer3DsTree.getSelectionPaths();
				for (int index = 0; index < selections.length; index++) {
					DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) selections[index].getLastPathComponent();
					TreeNodeData treeNodeData = (TreeNodeData) treeNode.getUserObject();
					Layer3DDataset layer3DDataset = (Layer3DDataset) treeNodeData.getData();
					if (layer3DDataset != null) {
						if (CommonToolkit.DatasetTypeWrap.isPoint(layer3DDataset.getDataset().getType())) {
							enable = true;
							break;
						} else if (CommonToolkit.DatasetTypeWrap.isLine(layer3DDataset.getDataset().getType())) {
							enable = true;
							break;
						} else if (CommonToolkit.DatasetTypeWrap.isRegion(layer3DDataset.getDataset().getType())) {
							enable = true;
							break;
						}
					}
				}
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		enable = false;
		return enable;
	}

	@Override
	public boolean check() {
		return false;
	}
}
