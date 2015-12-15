package com.supermap.desktop.realspaceEffect;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class RealspaceEffectProperties extends Properties {
	public static final String REALSPACE_EFFECT = "resources.RealspaceEffect";

	public static final String getString(String key) {
		return getString(REALSPACE_EFFECT, key);
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
