package com.supermap.desktop.datatopology;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

public class DataTopologyProperties extends Properties {
	public static final String DATA_TOPOLOGY = "DataTopology";

	public static final String getString(String key) {
		return getString(DATA_TOPOLOGY, key);
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
