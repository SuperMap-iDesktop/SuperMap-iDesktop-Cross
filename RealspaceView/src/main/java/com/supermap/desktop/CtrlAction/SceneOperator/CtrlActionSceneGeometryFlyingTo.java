package com.supermap.desktop.CtrlAction.SceneOperator;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;

public class CtrlActionSceneGeometryFlyingTo extends CtrlActionSceneGeometryFlying {

	public CtrlActionSceneGeometryFlyingTo(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	
	@Override 
	public DesktopFlyingMode getFlyingMode()
    {
        return DesktopFlyingMode.FLYINGTO;
    }
}
