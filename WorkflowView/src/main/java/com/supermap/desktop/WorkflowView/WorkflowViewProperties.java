package com.supermap.desktop.WorkflowView;

import com.supermap.desktop.properties.Properties;

import java.util.ResourceBundle;

/**
 * Created by highsad on 2017/7/26.
 */
public class WorkflowViewProperties extends Properties {
	public static final String WORKFLOWVIEW = "WorkflowView";

	public static final String getString(String key) {
		return getString(WORKFLOWVIEW, key);
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
