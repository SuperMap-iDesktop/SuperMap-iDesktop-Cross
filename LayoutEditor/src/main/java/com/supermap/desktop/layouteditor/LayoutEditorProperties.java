package com.supermap.desktop.layouteditor;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

public class LayoutEditorProperties extends Properties {
	public static final String LAYOUT_EDITOR = "LayoutEditor";

	public static final String getString(String key) {
		return getString(LAYOUT_EDITOR, key);
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
