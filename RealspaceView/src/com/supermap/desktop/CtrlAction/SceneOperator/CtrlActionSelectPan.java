package com.supermap.desktop.CtrlAction.SceneOperator;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action;
import com.supermap.ui.Action3D;

public class CtrlActionSelectPan extends CtrlActionSceneActionBase {

	public CtrlActionSelectPan(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public Action3D getAction() {
		return Action3D.PAN;
	}
}
