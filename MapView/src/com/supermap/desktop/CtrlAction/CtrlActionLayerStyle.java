package com.supermap.desktop.CtrlAction;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.mapping.Layer;

public class CtrlActionLayerStyle extends CtrlAction {

	public CtrlActionLayerStyle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			FormMap formMap = (FormMap) Application.getActiveApplication().getActiveForm();
			formMap.showStyleSetDialog();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
			TreePath[] selections = null;
			if (null != layersTree.getSelectionPaths()) {
				selections = layersTree.getSelectionPaths();
				for (int index = 0; index < selections.length; index++) {
					DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) selections[index].getLastPathComponent();
					TreeNodeData treeNodeData = (TreeNodeData) treeNode.getUserObject();
					Layer tempLayer = null;
					if (treeNodeData.getData() instanceof Layer) {
						tempLayer = (Layer) treeNodeData.getData();
					}
					if (tempLayer != null && tempLayer.getTheme() == null && tempLayer.getDataset() != null) {
						if (CommonToolkit.DatasetTypeWrap.isPoint(tempLayer.getDataset().getType())) {
							enable = true;
							break;
						} else if (CommonToolkit.DatasetTypeWrap.isLine(tempLayer.getDataset().getType())) {
							enable = true;
							break;
						} else if (CommonToolkit.DatasetTypeWrap.isRegion(tempLayer.getDataset().getType())) {
							enable = true;
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}

	@Override
	public boolean check() {
		return false;
	}
}
