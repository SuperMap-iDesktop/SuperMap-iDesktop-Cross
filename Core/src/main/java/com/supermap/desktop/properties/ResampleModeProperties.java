package com.supermap.desktop.properties;

import java.util.ResourceBundle;

/**
 * Created By Chens on 2017/8/14 0014
 */
public class ResampleModeProperties extends Properties {
	public static final String RESAMPLEMODE = "ResampleMode";

	public static final String getString(String key) {
		return getString(RESAMPLEMODE, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String BILINEAR = "String_Bilinear";
	public static final String CUBIC = "String_Cubic";
	public static final String NEAREST = "String_Nearest";
}
