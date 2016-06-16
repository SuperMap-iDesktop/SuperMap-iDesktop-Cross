package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.controls.utilities.SceneUIUtilities;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.Layer3DsTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3DType;

import javax.swing.tree.DefaultMutableTreeNode;

public class CtrlActionLayer3DSelectable extends CtrlAction {

	public CtrlActionLayer3DSelectable(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Layer3DsTree layer3DsTree = UICommonToolkit.getLayersManager().getLayer3DsTree();
			Object object = layer3DsTree.getSelectionPaths()[0].getLastPathComponent();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
			TreeNodeData data = (TreeNodeData) node.getUserObject();
			boolean isSelectable = !((Layer3D) data.getData()).isSelectable();

			for (int i = 0; i < layer3DsTree.getSelectionCount(); i++) {
				node = (DefaultMutableTreeNode) layer3DsTree.getSelectionPaths()[i].getLastPathComponent();
				data = (TreeNodeData) node.getUserObject();
				Layer3D layer3D = (Layer3D) data.getData();
				if (this.isLayer3DSurpportSelect(layer3D)) {
					layer3D.setSelectable(isSelectable);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
		if (formScene != null && formScene.getActiveLayer3Ds() != null && formScene.getActiveLayer3Ds().length > 0) {
			Layer3DsTree layer3DsTree = UICommonToolkit.getLayersManager().getLayer3DsTree();
			if (layer3DsTree != null) {
				for (int i = 0; i < layer3DsTree.getSelectionCount(); i++) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) layer3DsTree.getSelectionPaths()[i].getLastPathComponent();
					TreeNodeData data = (TreeNodeData) node.getUserObject();
					Layer3D layer3D = (Layer3D) data.getData();
					if (this.isLayer3DSurpportSelect(layer3D) && SceneUIUtilities.getObjectVisitble(data)) {
						enable = true;
						break;
					}
				}
			}
		}

		return enable;
	}

	@Override
	public boolean check() {
		boolean check = false;
		IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
		// 不支持三态，这里先简单的设置两个状态了
		if (formScene.getActiveLayer3Ds().length > 0 && formScene.getActiveLayer3Ds()[0].isSelectable()) {
			check = true;
		}
		return check;
	}

	private boolean isLayer3DSurpportSelect(Layer3D layer3D) {
		boolean result = true;
		//栅格/影像等不可选择
		if (layer3D instanceof Layer3DDataset) {
			Layer3DDataset layer3DDataset = (Layer3DDataset) layer3D;
			if (layer3DDataset.getDataset().getType() == DatasetType.GRID
					|| layer3DDataset.getDataset().getType() == DatasetType.GRIDCOLLECTION
					|| layer3DDataset.getDataset().getType() == DatasetType.IMAGE
					|| layer3DDataset.getDataset().getType() == DatasetType.IMAGECOLLECTION) {
				result = false;
			}
		} else if (layer3D.getType() == Layer3DType.KML
				|| layer3D.getType() == Layer3DType.MAP
				|| layer3D.getType() == Layer3DType.MODEL
				|| layer3D.getType() == Layer3DType.VECTORFILE
				|| layer3D.getType() == Layer3DType.WMS) {
			result = false;
		}

		return result;
	}
}