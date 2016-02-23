package com.supermap.desktop.CtrlAction.Map.MapMeasure;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure.MeasureAngle;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.DockingWindowAdapter;
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
		MeasureAngle measureAngle = getMeasureAngle();
		if (measureAngle != null) {
			measureAngle.startMeasure();
		}
	}

	private MeasureAngle getMeasureAngle() {
		MeasureAngle result = null;
		final IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormMap) {
			MapControl mapControl = ((FormMap) activeForm).getMapControl();
			if (hashMap == null) {
				hashMap = new HashMap<>();
			}
			result = hashMap.get(mapControl);
			if (result == null) {
				result = new MeasureAngle();
				((FormMap) activeForm).addListener(new DockingWindowAdapter() {
					@Override
					public void windowClosed(DockingWindow window) {
						if (window instanceof FormMap) {
							hashMap.remove(((FormMap) window).getMapControl());
							((FormMap) activeForm).removeListener(this);
						}
					}
				});
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
