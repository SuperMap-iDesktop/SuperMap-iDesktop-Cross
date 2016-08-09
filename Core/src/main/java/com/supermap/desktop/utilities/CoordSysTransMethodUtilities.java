package com.supermap.desktop.utilities;

import com.supermap.data.CoordSysTransMethod;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoordSysTransMethodProperties;

public class CoordSysTransMethodUtilities {
	private CoordSysTransMethodUtilities() {
		// 工具类不提供构造函数
	}

	public static String toString(CoordSysTransMethod data) {
		String result = "";

		try {
			if (data == CoordSysTransMethod.MTH_BURSA_WOLF) {
				result = CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.BursaWolf);
			} else if (data == CoordSysTransMethod.MTH_COORDINATE_FRAME) {
				result = CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.CoordinateFrame);
			} else if (data == CoordSysTransMethod.MTH_EXTENTION) {
				result = CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.Extension);
			} else if (data == CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION) {
				result = CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.GeocentricTranslation);
			} else if (data == CoordSysTransMethod.MTH_MOLODENSKY) {
				result = CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.Molodensky);
			} else if (data == CoordSysTransMethod.MTH_MOLODENSKY_ABRIDGED) {
				result = CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.MolodenskyAbridged);
			} else if (data == CoordSysTransMethod.MTH_POSITION_VECTOR) {
				result = CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.PositionVector);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static CoordSysTransMethod valueOf(String text) {
		CoordSysTransMethod result = CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION;

		try {
			if (text.equalsIgnoreCase(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.BursaWolf))) {
				result = CoordSysTransMethod.MTH_BURSA_WOLF;
			} else if (text.equalsIgnoreCase(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.CoordinateFrame))) {
				result = CoordSysTransMethod.MTH_COORDINATE_FRAME;
			} else if (text.equalsIgnoreCase(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.Extension))) {
				result = CoordSysTransMethod.MTH_EXTENTION;
			} else if (text.equalsIgnoreCase(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.GeocentricTranslation))) {
				result = CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION;
			} else if (text.equalsIgnoreCase(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.Molodensky))) {
				result = CoordSysTransMethod.MTH_MOLODENSKY;
			} else if (text.equalsIgnoreCase(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.MolodenskyAbridged))) {
				result = CoordSysTransMethod.MTH_MOLODENSKY_ABRIDGED;
			} else if (text.equalsIgnoreCase(CoordSysTransMethodProperties.getString(CoordSysTransMethodProperties.PositionVector))) {
				result = CoordSysTransMethod.MTH_POSITION_VECTOR;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
