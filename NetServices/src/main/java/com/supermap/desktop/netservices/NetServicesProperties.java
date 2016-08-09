package com.supermap.desktop.netservices;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

public class NetServicesProperties extends Properties {
	public static final String NETSERVICES = "NetServices";

	public static final String getString(String key) {
		return getString(NETSERVICES, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}
}
