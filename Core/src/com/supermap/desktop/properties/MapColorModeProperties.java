package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class MapColorModeProperties extends Properties {
	public static final String MAPCOLORMODE = "resources.MapColorMode";

	public static final String getString(String key) {
		return getString(MAPCOLORMODE, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String Default = "String_Default";
	public static final String BlackWhiteReverse = "String_BlackWhiteReverse";
	public static final String BlackWhite = "String_BlackWhite";
	public static final String Gray = "String_Gray";
	public static final String OnlyBlackWhiteReverse = "String_OnlyBlackWhiteReverse";
}
