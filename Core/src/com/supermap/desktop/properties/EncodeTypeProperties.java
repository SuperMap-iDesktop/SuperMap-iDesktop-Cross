package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class EncodeTypeProperties extends Properties {
	public static final String ENCODETYPE = "resources.EncodeType";

	public static final String getString(String key) {
		return getString(ENCODETYPE, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String Byte = "String_Byte";
	public static final String Compound = "String_Compound";
	public static final String DCT = "String_DCT";
	public static final String Int16 = "String_Int16";
	public static final String Int24 = "String_Int24";
	public static final String Int32 = "String_Int32";
	public static final String LZW = "String_LZW";
	public static final String None = "String_None";
	public static final String PNG = "String_PNG";
	public static final String SGL = "String_SGL";
}
