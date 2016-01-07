package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class CoordSysTransMethodProperties extends Properties {
	public static final String COORDSYSTRANSMETHOD = "resources.CoordSysTransMethod";

	public static final String getString(String key) {
		return getString(COORDSYSTRANSMETHOD, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String BursaWolf = "String_BursaWolf";
	public static final String CoordinateFrame = "String_CoordinateFrame";
	public static final String GeocentricTranslation = "String_GeocentricTranslation";
	public static final String Molodensky = "String_Molodensky";
	public static final String MolodenskyAbridged = "String_MolodenskyAbridged";
	public static final String PositionVector = "String_PositionVector";
	public static final String Extension = "String_Extension";
}
