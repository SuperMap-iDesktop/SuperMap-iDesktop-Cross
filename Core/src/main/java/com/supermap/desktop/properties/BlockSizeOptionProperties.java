package com.supermap.desktop.properties;

import java.util.ResourceBundle;

/**
 * Created by yuanR on 2017/8/15 0015.
 */
public class BlockSizeOptionProperties extends Properties {
	public static final String BLOCKSIZEOPTION = "BlockSizeOption";

	public static final String getString(String key) {
		return getString(BLOCKSIZEOPTION, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String String_BlockSizeOption_BS_1024 = "String_1024";
	public static final String String_BlockSizeOption_BS_128 = "String_128";
	public static final String String_BlockSizeOption_BS_256 = "String_256";
	public static final String String_BlockSizeOption_BS_512 = "String_512";
	public static final String String_BlockSizeOption_BS_64 = "String_64";

}
