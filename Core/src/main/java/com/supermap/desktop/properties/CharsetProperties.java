package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class CharsetProperties extends Properties {
	public static final String CHARSET = "Charset";

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

	public static final String ANSI = "String_ANSI";
	public static final String Arabic = "String_Arabic";
	public static final String Baltic = "String_Baltic";
	public static final String ChineseBIG5 = "String_ChineseBIG5";
	public static final String Cyrillic = "String_Cyrillic";
	public static final String Default = "String_Default";
	public static final String EastEurope = "String_EastEurope";
	public static final String GB18030 = "String_GB18030";
	public static final String Greek = "String_Greek";
	public static final String Hangeul = "String_Hangeul";
	public static final String Hebrew = "String_Hebrew";
	public static final String Johab = "String_Johab";
	public static final String Korean = "String_Korean";
	public static final String MAC = "String_MAC";
	public static final String OEM = "String_OEM";
	public static final String Russian = "String_Russian";
	public static final String ShiftJIS = "String_ShiftJIS";
	public static final String Symbol = "String_Symbol";
	public static final String Thai = "String_Thai";
	public static final String Turkish = "String_Turkish";
	public static final String Unicode = "String_Unicode";
	public static final String UTF32 = "String_UTF32";
	public static final String UTF7 = "String_UTF7";
	public static final String UTF8 = "String_UTF8";
	public static final String Vietnamese = "String_Vietnamese";
	public static final String Windows1252 = "String_Windows1252";
	public static final String XIA5 = "String_XIA5";
	public static final String XIA5German = "String_XIA5German";
	public static final String XIA5Norwegian = "String_XIA5Norwegian";
	public static final String XIA5Swedish = "String_XIA5Swedish";
	public static final String Unsupport = "String_Unsupport";
}
