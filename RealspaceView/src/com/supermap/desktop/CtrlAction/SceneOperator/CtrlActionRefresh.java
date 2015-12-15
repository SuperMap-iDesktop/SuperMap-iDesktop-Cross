package com.supermap.desktop.CtrlAction.SceneOperator;

import javax.swing.JOptionPane;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionRefresh extends CtrlAction {

	public CtrlActionRefresh(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormScene formScene = (IFormScene)Application.getActiveApplication().getActiveForm();
			if (formScene != null) {
				formScene.getSceneControl().getScene().refresh();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
