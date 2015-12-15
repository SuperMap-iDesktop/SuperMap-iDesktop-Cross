package com.supermap.desktop.dataconversion;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class DataConversionProperties extends Properties {
	public static final String DATA_CONVERSION = "resources.DataConversion";

	public static final String getString(String key) {
		return getString(DATA_CONVERSION, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName,
				getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}
}
