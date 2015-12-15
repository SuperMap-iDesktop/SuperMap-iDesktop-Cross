package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.realspace.Layer3D;

public class CtrlActionLayer3DManagerClearVisibleAltitude extends CtrlAction {

	public CtrlActionLayer3DManagerClearVisibleAltitude(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run(){
		try {
			IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
			if (null!=formScene) {
				Layer3D scene = formScene.getActiveLayer3Ds()[0];
				scene.setMaxVisibleAltitude(0);
				scene.setMinVisibleAltitude(0);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
	
	public boolean enable(){
		return true;
	}
	
}
