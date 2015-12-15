package com.supermap.desktop.CtrlAction.LayerSetting;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.Layer3DsTree;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.mapping.Layer;
import com.supermap.realspace.Layer3D;

public class CtrlActionLayer3DUpdateData extends CtrlAction {

	public CtrlActionLayer3DUpdateData(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			Layer3DsTree layer3DsTree = UICommonToolkit.getLayersManager().getLayer3DsTree();
			for (int i = 0; i < layer3DsTree.getSelectionCount(); i++) {
				Object object = layer3DsTree.getSelectionPaths()[i].getLastPathComponent();				
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
				TreeNodeData data = (TreeNodeData) node.getUserObject();
				Layer3D layer3D = (Layer3D) data.getData();
				if (layer3D != null) {
					layer3D.updateData();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}