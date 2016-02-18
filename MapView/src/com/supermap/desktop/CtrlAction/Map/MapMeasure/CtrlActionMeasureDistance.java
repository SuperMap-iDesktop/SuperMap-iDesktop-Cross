package com.supermap.desktop.CtrlAction.Map.MapMeasure;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure.MeasureDistance;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
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
		MeasureDistance measureDistance = getMeasureDistance();
		if (measureDistance != null) {
			measureDistance.startMeasure();
		}
	}

	private MeasureDistance getMeasureDistance() {
		MeasureDistance result = null;
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormMap) {
			MapControl mapControl = ((IFormMap) activeForm).getMapControl();
			if (hashMap == null) {
				hashMap = new HashMap<>();
			}
			result = hashMap.get(mapControl);
			if (result == null) {
				result = new MeasureDistance();
				hashMap.put(mapControl, result);
			}
		}
		return result;
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		return activeForm instanceof FormMap;
	}
}
