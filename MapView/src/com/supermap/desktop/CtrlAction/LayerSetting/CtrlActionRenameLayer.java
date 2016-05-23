package com.supermap.desktop.CtrlAction.LayerSetting;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.mapping.Layer;

public class CtrlActionRenameLayer extends CtrlAction {

	public CtrlActionRenameLayer(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
			TreePath treeSelectionPath = layersTree.getSelectionPaths()[0];
			layersTree.setEditable(true);
			layersTree.startEditingAtPath(treeSelectionPath);
			layersTree.firePropertyChangeWithLayerSelect();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (formMap != null) {
				LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
				if (layersTree != null && layersTree.getSelectionPaths().length == 1) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) layersTree.getSelectionPaths()[0].getLastPathComponent();
					TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
					Layer layer = (Layer) selectedNodeData.getData();
					if (layer != null && selectedNodeData.getType() != NodeDataType.WMSSUB_LAYER) {
						enable = true;
					}
				}
			}
		}
		return enable;
	}
}