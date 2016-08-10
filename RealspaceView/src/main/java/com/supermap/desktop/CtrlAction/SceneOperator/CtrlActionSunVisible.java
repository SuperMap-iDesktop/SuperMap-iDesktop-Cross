package com.supermap.desktop.CtrlAction.SceneOperator;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;

public class CtrlActionSunVisible extends CtrlActionSceneActionBase {

	public CtrlActionSunVisible(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormScene formScene = (IFormScene)Application.getActiveApplication().getActiveForm();
			if (formScene != null) {
				boolean visible = formScene.getSceneControl().getScene().getSun().isVisible();
				formScene.getSceneControl().getScene().getSun().setVisible(!visible);
			}
			ToolbarUIUtilities.updataToolbarsState();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean check() {
		boolean check = false;
		IFormScene formScene = (IFormScene)Application.getActiveApplication().getActiveForm();
		if (formScene != null) {
			check = formScene.getSceneControl().getScene().getSun().isVisible();
		}
		return check;
	}
}
