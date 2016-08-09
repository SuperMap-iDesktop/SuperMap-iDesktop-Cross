package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class PixelFormatProperties extends Properties {
	public static final String PIXELFORMAT = "PixelFormat";

	public static final String getString(String key) {
		return getString(PIXELFORMAT, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String RGB = "String_RGB";
	public static final String RGBA = "String_RGBA";
	public static final String Double = "String_Double";
	public static final String Single = "String_Single";
	public static final String Bit8 = "String_Bit8";
	public static final String Bit16 = "String_Bit16";
	public static final String Bit32 = "String_Bit32";
	public static final String Bit64 = "String_Bit64";
	public static final String UBit1 = "String_UBit1";
	public static final String UBit4 = "String_UBit4";
	public static final String UBit8 = "String_UBit8";
	public static final String UBit16 = "String_UBit16";
	public static final String UBit32 = "String_UBit32";
}
