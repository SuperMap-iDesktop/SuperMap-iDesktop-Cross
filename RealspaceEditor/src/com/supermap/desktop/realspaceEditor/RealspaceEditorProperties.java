package com.supermap.desktop.realspaceEditor;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class RealspaceEditorProperties extends Properties{
	public static final String REALSPACE_EDITOR = "resources.RealspaceEditor";

	public static final String getString(String key) {
		return getString(REALSPACE_EDITOR, key);
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
