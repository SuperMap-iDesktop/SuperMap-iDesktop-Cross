package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class ImageStretchTypeProperties extends Properties {
	public static final String IMAGESTRETCHTYPE = "resources.ImageStretchType";

	public static final String getString(String key) {
		return getString(IMAGESTRETCHTYPE, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String None = "String_None";
	public static final String Gaussian = "String_Gaussian";
	public static final String StandardDeviation = "String_StandardDeviation";
	public static final String MinimumMaximum = "String_MinimumMaximum";
	public static final String HistogramEqualization = "String_HistogramEqualization";
	public static final String HistogramSpecification = "String_HistogramSpecification";
}
