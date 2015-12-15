package com.supermap.desktop.frame;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class FrameProperties extends Properties {
	public static final String FRAME = "resources.Frame";

	public static final String getString(String key) {
		return getString(FRAME, key);
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
