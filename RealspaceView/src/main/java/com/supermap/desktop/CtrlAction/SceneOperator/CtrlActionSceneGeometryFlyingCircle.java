package com.supermap.desktop.CtrlAction.SceneOperator;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;

public class CtrlActionSceneGeometryFlyingCircle extends CtrlActionSceneGeometryFlying {

	public CtrlActionSceneGeometryFlyingCircle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			 super.flyingCircleSpeedRatio = 2.0;
			 super.run();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
	
	@Override 
	public DesktopFlyingMode getFlyingMode()
    {
        return DesktopFlyingMode.FLYINGCIRCLE;
    }
}
