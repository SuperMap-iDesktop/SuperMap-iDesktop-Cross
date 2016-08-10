package com.supermap.desktop.dataview;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

public class DataViewProperties extends Properties {
	public static final String DATA_VIEW = "DataView";

	public static final String getString(String key) {
		return getString(DATA_VIEW, key);
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
