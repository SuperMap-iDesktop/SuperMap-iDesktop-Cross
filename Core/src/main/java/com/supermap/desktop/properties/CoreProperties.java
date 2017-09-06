package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class CoreProperties extends Properties {
	public static final String CORE = "Core";

	public static final String getString(String key) {
		return getString(CORE, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String ReadOnly = "String_ReadOnly";
	public static final String Exclusive = "String_Exclusive";
	public static final String Default = "String_Default";
	public static final String Clear = "String_Clear";
	public static final String Other = "String_Other";
	public static final String Boolean = "String_Boolean";
	public static final String Byte = "String_Byte";
	public static final String Char = "String_Char";
	public static final String Double = "String_Double";
	public static final String Short = "String_Short";
	public static final String Long = "String_Long";
	public static final String Integer = "String_Integer";
	public static final String LongBinary = "String_LongBinary";
	public static final String Float = "String_Float";
	public static final String Text = "String_Text";
	public static final String WText = "String_WText";
	public static final String DateTime = "String_DateTime";
	public static final String JSONB = "String_JSONB";
	public static final String Left = "String_Left";
	public static final String Right = "String_Right";
	public static final String Top = "String_Top";
	public static final String Bottom = "String_Bottom";
}
