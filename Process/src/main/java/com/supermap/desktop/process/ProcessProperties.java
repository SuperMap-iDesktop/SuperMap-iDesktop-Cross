package com.supermap.desktop.process;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

/**
 * @author XiaJT
 */
public class ProcessProperties extends Properties {

	public static final String PROCESS = "process";

	public static final String getString(String key) {
		return getString(PROCESS, key);
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
