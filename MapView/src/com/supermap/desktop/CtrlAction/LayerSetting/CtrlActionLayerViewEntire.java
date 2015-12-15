package com.supermap.desktop.CtrlAction.LayerSetting;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Point2D;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.utilties.LayerUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;

public class CtrlActionLayerViewEntire extends CtrlAction {

	public CtrlActionLayerViewEntire(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

			LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) layersTree.getSelectionPaths()[0].getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			Layer layer = (Layer) selectedNodeData.getData();
			Rectangle2D rectangle2D = Rectangle2D.getEMPTY();
			if (layer instanceof LayerGroup) {
				rectangle2D = LayerUtilties.getLayerBounds(formMap.getMapControl().getMap(), (LayerGroup) layer);
			} else {
				rectangle2D = LayerUtilties.getLayerBounds(formMap.getMapControl().getMap(), layer);
			}

			if(rectangle2D.getHeight()>0){
				formMap.getMapControl().getMap().setViewBounds(rectangle2D);
			}else {
				formMap.getMapControl().getMap().setCenter(new Point2D(0, 0));
			}
			formMap.getMapControl().getMap().refresh();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
		if (layersTree.getSelectionPaths().length == 1) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) layersTree.getSelectionPaths()[0].getLastPathComponent();
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			if (selectedNodeData != null && selectedNodeData.getData() != null && selectedNodeData.getData() instanceof Layer) {
				Layer layer = (Layer) selectedNodeData.getData();
				if (layer != null) {
					if (layer.getDataset() != null) {
						enable = true;
					} else if (layer instanceof LayerGroup && ((LayerGroup) layer).getCount() > 0) {
						enable = true;
					}
				}
			}
		}
		return enable;
	}
}