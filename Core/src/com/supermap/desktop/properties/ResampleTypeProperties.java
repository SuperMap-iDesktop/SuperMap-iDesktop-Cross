package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class ResampleTypeProperties extends Properties {
	public static final String RESAMPLETYPE = "resources.ResampleType";

	public static final String getString(String key) {
		return getString(RESAMPLETYPE, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String RTBend = "String_RTBend";
	public static final String RTGeneral = "String_RTGeneral";
}
