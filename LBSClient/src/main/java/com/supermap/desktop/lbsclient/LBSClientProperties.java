package com.supermap.desktop.lbsclient;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

public class LBSClientProperties extends Properties {
	public static final String LBSClient = "LBSClient";

	public static final String getString(String key) {
		return getString(LBSClient, key);
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