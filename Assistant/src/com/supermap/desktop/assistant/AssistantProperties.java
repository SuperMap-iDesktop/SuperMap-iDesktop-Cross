package com.supermap.desktop.assistant;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class AssistantProperties extends Properties {
	public static final String ASSISTANT = "resources.Assistant";

	public static final String getString(String key) {
		return getString(ASSISTANT, key);
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
