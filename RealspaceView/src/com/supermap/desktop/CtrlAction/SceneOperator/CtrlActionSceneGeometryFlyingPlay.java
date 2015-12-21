package com.supermap.desktop.CtrlAction.SceneOperator;

import javax.swing.JOptionPane;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;

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
