package com.supermap.desktop.mapeditor;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

public class MapEditorProperties extends Properties {
	public static final String MAP_EDITOR = "MapEditor";

	public static final String getString(String key) {
		return getString(MAP_EDITOR, key);
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
