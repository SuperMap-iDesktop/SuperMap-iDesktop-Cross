package com.supermap.desktop.layouteditor;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class LayoutEditorProperties extends Properties {
	public static final String LAYOUT_EDITOR = "resources.LayoutEditor";

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
