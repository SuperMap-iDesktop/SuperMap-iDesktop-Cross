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
	public static final String ARABIC = "String_ARABIC";
	public static final String BALTIC = "String_BALTIC";
	public static final String CHINESEBIG5 = "String_CHINESEBIG5";
	public static final String CYRILLIC = "String_CYRILLIC";
	public static final String DEFAULT = "String_DEFAULT";
	public static final String EASTEUROPE = "String_EASTEUROPE";
	public static final String GB18030 = "String_GB18030";
	public static final String GREEK = "String_GREEK";
	public static final String HANGEUL = "String_HANGEUL";
	public static final String HEBREW = "String_HEBREW";
	public static final String JOHAB = "String_JOHAB";
	public static final String KOREAN = "String_KOREAN";
	public static final String MAC = "String_MAC";
	public static final String OEM = "String_OEM";
	public static final String RUSSIAN = "String_RUSSIAN";
	public static final String SHIFTJIS = "String_SHIFTJIS";
	public static final String SYMBOL = "String_SYMBOL";
	public static final String THAI = "String_THAI";
	public static final String TURKISH = "String_TURKISH";
	public static final String UNICODE = "String_UNICODE";
	public static final String UTF32 = "String_UTF32";
	public static final String UTF7 = "String_UTF7";
	public static final String UTF8 = "String_UTF8";
	public static final String VIETNAMESE = "String_VIETNAMESE";
	public static final String WINDOWS1252 = "String_WINDOWS1252";
	public static final String XIA5 = "String_XIA5";
	public static final String XIA5GERMAN = "String_XIA5GERMAN";
	public static final String XIA5NORWEGIAN = "String_XIA5NORWEGIAN";
	public static final String XIA5SWEDISH = "String_XIA5SWEDISH";
	public static final String UNSUPPORT = "String_UNSUPPORT";
}
