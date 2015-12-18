package com.supermap.desktop.CtrlAction.LayerSetting;

import javax.swing.tree.DefaultMutableTreeNode;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.Layer3DsTree;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.realspace.Feature3D;
import com.supermap.realspace.Feature3Ds;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.ScreenLayer3D;
import com.supermap.realspace.TerrainLayer;
import com.supermap.realspace.Theme3DRangeItem;
import com.supermap.realspace.Theme3DUniqueItem;

public class CtrlActionLayer3DVisible extends CtrlAction {

	public CtrlActionLayer3DVisible(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Layer3DsTree layer3DsTree = UICommonToolkit.getLayersManager().getLayer3DsTree();
			Object object = layer3DsTree.getSelectionPaths()[0].getLastPathComponent();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
			TreeNodeData data = (TreeNodeData) node.getUserObject();

			boolean isVisible = !getObjectVisitble(data);
			for (int i = 0; i < layer3DsTree.getSelectionCount(); i++) {
				node = (DefaultMutableTreeNode) layer3DsTree.getSelectionPaths()[i].getLastPathComponent();
				data = (TreeNodeData) node.getUserObject();
				if (data.getType() == NodeDataType.FEATURE3D) {
					((Feature3D) data.getData()).setVisible(isVisible);
					if (!isVisible) {
						Layer3D layer3d = (Layer3D) data.getData();
						layer3d.getSelection().remove(((Feature3D) data.getData()).getID());
						layer3d.getSelection().updateData();
					}
				} else if (data.getType() == NodeDataType.FEATURE3DS) {
					((Feature3Ds) data.getData()).setVisible(isVisible);
				} else if (data.getType() == NodeDataType.LAYER) {
					Layer3D layer3d = (Layer3D) data.getData();
					layer3d.setVisible(isVisible);
				} else if (data.getType() == NodeDataType.SCREEN_LAYER3D) {
					ScreenLayer3D screenLayer3D = (ScreenLayer3D) data.getData();
					screenLayer3D.setVisible(isVisible);
					int count = screenLayer3D.getCount();
					if (!isVisible && count > 0) {
						for (int j = 0; j < count; j++) {
							if (screenLayer3D.isSelected(j)) {
								screenLayer3D.setSelected(j, false);
							}
						}
					}
				} else if (data.getType() == NodeDataType.THEME3D_UNIQUE_ITEM) {
					((Theme3DRangeItem) data.getData()).setVisible(isVisible);
				} else if (data.getType() == NodeDataType.THEME3D_RANGE_ITEM) {
					((Theme3DUniqueItem) data.getData()).setVisible(isVisible);
				} else if (data.getType() == NodeDataType.TERRAIN_LAYER) {
					((TerrainLayer) data.getData()).setVisible(isVisible);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean check() {
		boolean check = false;
		IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
		// 不支持三态，这里先简单的设置两个状态了
		if (formScene.getActiveLayer3Ds()[0].isVisible()) {
			check = true;
		}
		return check;
	}

	private boolean getObjectVisitble(TreeNodeData treeNode) {
		boolean visible = true;
		try {
			if (treeNode.getType() == NodeDataType.UNKNOWN || treeNode.getType() == NodeDataType.LAYER3DS || treeNode.getType() == NodeDataType.TERRAIN_LAYERS) {
				visible = false;
			} else if (treeNode.getType() == NodeDataType.SCREEN_LAYER3D) {
				visible = ((ScreenLayer3D) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.LAYER) {
				visible = ((Layer3D) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.TERRAIN_LAYER) {
				visible = ((TerrainLayer) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.FEATURE3DS) {
				visible = ((Feature3Ds) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.FEATURE3D) {
				visible = ((Feature3D) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.THEME3D_UNIQUE_ITEM) {
				visible = ((Theme3DUniqueItem) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.THEME3D_RANGE_ITEM) {
				visible = ((Theme3DRangeItem) treeNode.getData()).isVisible();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return visible;
	}
}