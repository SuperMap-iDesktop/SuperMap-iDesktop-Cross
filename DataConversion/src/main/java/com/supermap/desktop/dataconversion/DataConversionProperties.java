package com.supermap.desktop.dataconversion;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

public class DataConversionProperties extends Properties {
	public static final String DATA_CONVERSION = "DataConversion";

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
