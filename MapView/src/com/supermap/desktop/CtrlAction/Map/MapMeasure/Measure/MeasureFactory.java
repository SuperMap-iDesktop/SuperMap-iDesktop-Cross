package com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure;

import com.supermap.desktop.enums.MeasureType;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.utilties.LogUtilities;

/**
 * @author XiaJt
 */
public class MeasureFactory {

	public static IMeasureAble getMeasureInstance(MeasureType measureType) {
		if (measureType == MeasureType.Distance) {
			return new MeasureDistance();
		} else if (measureType == MeasureType.Area) {
			return new MeasureArea();
		} else if (measureType == MeasureType.Angle) {
			return new MeasureAngle();
		}
		LogUtilities.debug(MapViewProperties.getString("Log_MeasureTypeUnSupport") + measureType);
		return null;
	}
}
