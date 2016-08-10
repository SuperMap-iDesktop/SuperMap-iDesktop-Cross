package com.supermap.desktop.tabularview;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

public class TabularViewProperties extends Properties {
	public static final String TABULAR_VIEW = "TabularView";

	public static final String getString(String key) {
		return getString(TABULAR_VIEW, key);
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
