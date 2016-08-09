package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class ThemeTypeProperties extends Properties{
	public static final String THEMETYPE = "ThemeType";

	public static final String getString(String key) {
		return getString(THEMETYPE, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}
	
	public static final String THEMEUNIQUE = "String_ThemeUnique";
	public static final String THEMERANGE = "String_ThemeRange";
	public static final String THEMELABEL = "String_ThemeLabel";
	public static final String THEMEGRAPH = "String_ThemeGraph";
	public static final String THEMEGRADUTEDSYMBOL = "String_ThemeGraduatedSymbol";
	public static final String THEMECUSTOM = "String_ThemeCustom";
}
