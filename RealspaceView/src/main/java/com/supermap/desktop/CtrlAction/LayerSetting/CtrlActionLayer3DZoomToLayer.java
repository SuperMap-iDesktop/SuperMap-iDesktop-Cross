package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Utilties.SceneJumpUtilties;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.Layer3DsTree;
import com.supermap.desktop.ui.controls.TreeNodeData;

import javax.swing.tree.DefaultMutableTreeNode;

public class CtrlActionLayer3DZoomToLayer extends CtrlAction {

	public CtrlActionLayer3DZoomToLayer(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
			Layer3DsTree layer3DsTree = UICommonToolkit.getLayersManager().getLayer3DsTree();

			Object object = layer3DsTree.getSelectionPaths()[0].getLastPathComponent();
			final DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
			TreeNodeData data = (TreeNodeData) node.getUserObject();
			SceneJumpUtilties.zoomToLayer(formScene, data);
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