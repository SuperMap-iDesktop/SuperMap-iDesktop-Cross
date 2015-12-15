package com.supermap.desktop.iDesktop;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class DesktopProperties extends Properties {
	public static final String IDESKTOP = "resources.iDesktop";

	public static final String getString(String key) {
		return getString(IDESKTOP, key);
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
