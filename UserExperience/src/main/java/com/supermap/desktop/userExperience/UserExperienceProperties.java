package com.supermap.desktop.userExperience;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

/**
 * @author XiaJT
 */
public class UserExperienceProperties extends Properties {
	public static final String USER_EXPERIENCE = "UserExperience";

	public static String getString(String key) {
		return getString(USER_EXPERIENCE, key);
	}

	public static String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

}
