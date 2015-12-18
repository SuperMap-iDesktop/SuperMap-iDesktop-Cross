package com.supermap.desktop.layoutview;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class LayoutViewProperties extends Properties {
	public static final String LAYOUT_VIEW = "resources.LayoutView";

	public static final String getString(String key) {
		return getString(LAYOUT_VIEW, key);
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
