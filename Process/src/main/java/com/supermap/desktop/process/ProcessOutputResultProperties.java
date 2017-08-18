package com.supermap.desktop.process;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

/**
 * Created by yuanR on 2017/8/18 0018.
 */
public class ProcessOutputResultProperties extends Properties {

	public static final String PROCESSOUTPUTRESULT = " ProcessOutputResult";

	public static final String getString(String key) {
		return getString(PROCESSOUTPUTRESULT, key);
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