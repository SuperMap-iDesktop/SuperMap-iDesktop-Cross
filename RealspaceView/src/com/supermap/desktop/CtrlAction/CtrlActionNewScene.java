package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.realspace.Scene;

public class CtrlActionNewScene extends CtrlAction {

	public CtrlActionNewScene(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {			
			IFormScene formScene = (IFormScene)CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.SCENE);
			if (formScene != null) {
				Scene scene = formScene.getSceneControl().getScene();
				formScene.setWorkspace(Application.getActiveApplication().getWorkspace());
				scene.refresh();
				UICommonToolkit.getLayersManager().setScene(scene);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}
