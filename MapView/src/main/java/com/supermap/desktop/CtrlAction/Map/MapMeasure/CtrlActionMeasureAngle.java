package com.supermap.desktop.CtrlAction.Map.MapMeasure;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure.MeasureAngle;
import com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure.MeasureUtilties;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.enums.MeasureType;
import com.supermap.ui.MapControl;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/1/26.
 */
public class CtrlActionMeasureAngle extends CtrlActionMeasureArea {
	public static HashMap<MapControl, MeasureAngle> hashMap;

	public CtrlActionMeasureAngle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormMap) {
			MeasureUtilties.startMeasure((FormMap) activeForm, MeasureType.Angle);
		}
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		return activeForm instanceof FormMap;
	}

}
