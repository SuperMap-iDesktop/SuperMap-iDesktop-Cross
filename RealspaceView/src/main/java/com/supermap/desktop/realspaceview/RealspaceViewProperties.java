package com.supermap.desktop.realspaceview;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

public class RealspaceViewProperties extends Properties {
	public static final String REALSPACE_VIEW = "RealspaceView";

	public static final String getString(String key) {
		return getString(REALSPACE_VIEW, key);
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
