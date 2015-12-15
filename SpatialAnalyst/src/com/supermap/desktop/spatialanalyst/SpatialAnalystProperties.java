package com.supermap.desktop.spatialanalyst;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class SpatialAnalystProperties extends Properties {
	public static final String SPATIAL_ANALYST = "resources.SpatialAnalyst";

	public static final String getString(String key) {
		return getString(SPATIAL_ANALYST, key);
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
