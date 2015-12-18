package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.realspace.Camera;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.Scene;

public class CtrlActionLayer3DManagerMinVisibleAltitude extends CtrlAction {

	public CtrlActionLayer3DManagerMinVisibleAltitude(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run() {
		try {
			IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
			if (null != formScene) {
				Scene scene = formScene.getSceneControl().getScene();
				Camera camera = scene.getCamera();
				double altitude = camera.getAltitude();
				Layer3D currentLayer = formScene.getActiveLayer3Ds()[0];
				currentLayer.setMinVisibleAltitude(altitude);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

	}

	public boolean enable() {
		return true;
	}
}
