package com.supermap.desktop.CtrlAction.SceneOperator;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;

public class CtrlActionSceneGeometryFlyingPlay extends CtrlActionSceneGeometryFlying {

	public CtrlActionSceneGeometryFlyingPlay(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	
	@Override 
	public DesktopFlyingMode getFlyingMode()
    {
        return DesktopFlyingMode.FLYINGPLAY;
    }
}
