package com.supermap.desktop.enums;

import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilties.LogUtilities;

import java.text.MessageFormat;

/**
 * Created by Administrator on 2016/3/31.
 */
public enum MeasureType {
	Distance,
	Area,
	Angle;

	@Override
	public String toString() {
		if (this == Distance) {
			return CoreProperties.getString("String_DistanceMeasure");
		} else if (this == Area) {
			return CoreProperties.getString("String_AreaMeasure");
		} else if (this == Angle) {
			return CoreProperties.getString("String_AngleMeasure");
		}
		LogUtilities.debug(MessageFormat.format(CoreProperties.getString("Log_AddedMeasureNeedAddToString"), this));
		return super.toString();
	}
}
