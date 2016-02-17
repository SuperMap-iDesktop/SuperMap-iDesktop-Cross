package com.supermap.desktop.CtrlAction.Map.MapMeasure;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.utilties.ToolbarUtilties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

/**
 * Created by Administrator on 2016/1/26.
 */
public class CtrlActionMeasureClear extends CtrlAction {
	public CtrlActionMeasureClear(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormMap) {
			FormMap formMap = (FormMap) activeForm;
			formMap.getMapControl().getMap().getTrackingLayer().clear();
			formMap.getMapControl().setAction(Action.SELECT2);
			formMap.getMapControl().setTrackMode(TrackMode.EDIT);
			formMap.getMapControl().getMap().refreshTrackingLayer();
			ToolbarUtilties.updataToolbarsState();
		}
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		return activeForm instanceof FormMap && ((FormMap) activeForm).getMapControl().getMap().getTrackingLayer().getCount() > 0;
	}
}
