package com.supermap.desktop.CtrlAction.Scene;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionSceneSaveAs extends CtrlAction {

	public CtrlActionSceneSaveAs(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			IFormScene formScene = (IFormScene)Application.getActiveApplication().getActiveForm();
			formScene.saveAs(false);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getActiveForm() != null
    			&& Application.getActiveApplication().getActiveForm() instanceof IFormScene) {
    		enable = true;
    	}
        return enable;
	}

}
