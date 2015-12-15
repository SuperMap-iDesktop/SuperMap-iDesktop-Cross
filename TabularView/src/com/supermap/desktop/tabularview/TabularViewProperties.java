package com.supermap.desktop.tabularview;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class TabularViewProperties extends Properties {
	public static final String TABULAR_VIEW = "resources.TabularView";

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
