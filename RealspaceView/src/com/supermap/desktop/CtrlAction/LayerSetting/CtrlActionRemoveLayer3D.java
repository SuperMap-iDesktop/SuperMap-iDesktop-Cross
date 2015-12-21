package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormScene;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionRemoveLayer3D extends CtrlAction {

	public CtrlActionRemoveLayer3D(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			FormScene formScene = (FormScene) Application.getActiveApplication().getActiveForm();
			formScene.removeLayers(formScene.getActiveLayer3Ds());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}