package com.supermap.desktop.CtrlAction.Scene;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionSceneSave extends CtrlAction {

	public CtrlActionSceneSave(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		IFormScene formScene = (IFormScene)Application.getActiveApplication().getActiveForm();
		formScene.save();
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		IFormScene formScene = (IFormScene)Application.getActiveApplication().getActiveForm();
		if (formScene != null) {
			enable = true;
		}
		return enable;
	}

}
