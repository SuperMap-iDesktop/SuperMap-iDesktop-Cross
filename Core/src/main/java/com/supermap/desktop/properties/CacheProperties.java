package com.supermap.desktop.properties;

import java.util.ResourceBundle;

/**
 * Created by xie on 2017/8/15.
 */
public class CacheProperties extends Properties{
	public static final String CHARSET = "Cache";

	public static final String getString(String key) {
		return getString(CHARSET, key);
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
