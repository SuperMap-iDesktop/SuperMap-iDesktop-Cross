package com.supermap.desktop.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.PrjCoordSysType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.controls.utilties.SceneUtilties;
import com.supermap.desktop.implement.CtrlAction;
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

			SceneUtilties.addDatasetToScene(scene, datasets);

			scene.refresh();

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		if (!(Application.getActiveApplication().getActiveForm() instanceof IFormScene)) {
			return false;
		}
		Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
		if (datasets == null || datasets.length <= 0) {
			return false;
		}
		for (Dataset dataset : datasets) {
			if (dataset.getType() == DatasetType.TABULAR || dataset.getType() == DatasetType.TOPOLOGY) {
				return false;
			}
			if (dataset.getPrjCoordSys().getType() == PrjCoordSysType.PCS_NON_EARTH) {
				return false;
			}
		}
		return true;
	}

}
