package com.supermap.desktop.CtrlAction.SceneOperator;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action3D;

public class CtrlActionPan extends CtrlActionSceneActionBase {

	public CtrlActionPan(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public Action3D getAction() {
		return Action3D.PAN2;
	}
}
