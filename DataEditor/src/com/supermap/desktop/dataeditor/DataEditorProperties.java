package com.supermap.desktop.dataeditor;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class DataEditorProperties extends Properties {
	public static final String DATA_EDITOR = "resources.DataEditor";

	public static final String getString(String key) {
		return getString(DATA_EDITOR, key);
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
