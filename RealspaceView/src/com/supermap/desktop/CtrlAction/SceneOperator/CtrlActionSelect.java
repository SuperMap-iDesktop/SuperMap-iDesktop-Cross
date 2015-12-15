package com.supermap.desktop.CtrlAction.SceneOperator;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action3D;

public class CtrlActionSelect extends CtrlActionSceneActionBase {

	public CtrlActionSelect(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public Action3D getAction() {
		return Action3D.SELECT;
	}
}
