package com.supermap.desktop.mapview;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class MapViewProperties extends Properties {
	public static final String MAP_VIEW = "resources.MapView";

	public static final String getString(String key) {
		return getString(MAP_VIEW, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}
}
