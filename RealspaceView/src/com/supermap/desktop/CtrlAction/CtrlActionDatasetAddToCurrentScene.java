package com.supermap.desktop.CtrlAction;

import javax.swing.tree.DefaultMutableTreeNode;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.WorkspaceComponentManager;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.utilties.SceneUtilties;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.Layer3DSettingGrid;
import com.supermap.realspace.Layer3DSettingImage;
import com.supermap.realspace.Layer3DSettingVector;
import com.supermap.realspace.Scene;

public class CtrlActionDatasetAddToCurrentScene extends CtrlAction {

	public CtrlActionDatasetAddToCurrentScene(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
			IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
			Scene scene = formScene.getSceneControl().getScene();

			for (Dataset dataset : datasets) {
				SceneUtilties.addDatasetToScene(scene, dataset, true);
			}

			scene.refresh();
			UICommonToolkit.getLayersManager().setScene(scene);

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
			if ((Application.getActiveApplication().getActiveForm() instanceof IFormScene) && datasets != null && datasets.length > 0
					&& datasets[0].getType() != DatasetType.TABULAR && datasets[0].getType() != DatasetType.TOPOLOGY) {
				enable = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}

}
