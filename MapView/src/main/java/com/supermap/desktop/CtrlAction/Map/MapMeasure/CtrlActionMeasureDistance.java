package com.supermap.desktop.CtrlAction.Map.MapMeasure;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure.MeasureDistance;
import com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure.MeasureUtilties;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.enums.MeasureType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.ui.MapControl;

import java.util.HashMap;

/**
 * 距离量算
 */
public class CtrlActionMeasureDistance extends CtrlAction {

	public static HashMap<MapControl, MeasureDistance> hashMap;

	public CtrlActionMeasureDistance(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormMap) {
			MeasureUtilties.startMeasure((FormMap) activeForm, MeasureType.Distance);
		}
	}


	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		return activeForm instanceof FormMap;
	}
}
