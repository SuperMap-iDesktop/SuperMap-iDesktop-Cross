package com.supermap.desktop.dataprocess;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class DataProcessProperties extends Properties {
	public static final String DATA_PROCESS = "resources.DataProcess";

	public static final String getString(String key) {
		return getString(DATA_PROCESS, key);
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
