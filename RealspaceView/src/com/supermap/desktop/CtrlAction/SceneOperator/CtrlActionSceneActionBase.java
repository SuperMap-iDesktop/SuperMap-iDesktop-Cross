package com.supermap.desktop.CtrlAction.SceneOperator;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action3D;

abstract public class CtrlActionSceneActionBase extends CtrlAction {
	
	public CtrlActionSceneActionBase(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	
	public Action3D getAction() {
		return Action3D.NULL;
	}
	
	@Override
	public void run() {
		try {
			IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();	
			formScene.getSceneControl().setAction(this.getAction());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
	
	@Override
	public boolean enable() {
		return true;
	}
	
	@Override
	public boolean check() {
		boolean check = false;

		try {			
			IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
			if (formScene != null && formScene.getSceneControl().getAction() == this.getAction()) {
				check = true;
			}
			
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return check;
	}
}
