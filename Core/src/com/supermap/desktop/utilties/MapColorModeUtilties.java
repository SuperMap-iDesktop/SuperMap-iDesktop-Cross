package com.supermap.desktop.utilties;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.MapColorModeProperties;
import com.supermap.mapping.MapColorMode;

public class MapColorModeUtilties {
	private MapColorModeUtilties() {
		// 工具类不提供构造函数
	}

	public static String toString(MapColorMode data) {
		String result = "";

		try {
			if (data == MapColorMode.DEFAULT) {
				result = MapColorModeProperties.getString(MapColorModeProperties.Default);
			} else if (data == MapColorMode.BLACK_WHITE_REVERSE) {
				result = MapColorModeProperties.getString(MapColorModeProperties.BlackWhiteReverse);
			} else if (data == MapColorMode.BLACKWHITE) {
				result = MapColorModeProperties.getString(MapColorModeProperties.BlackWhite);
			} else if (data == MapColorMode.GRAY) {
				result = MapColorModeProperties.getString(MapColorModeProperties.Gray);
			} else if (data == MapColorMode.ONLY_BLACK_WHITE_REVERSE) {
				result = MapColorModeProperties.getString(MapColorModeProperties.OnlyBlackWhiteReverse);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static MapColorMode valueOf(String text) {
		MapColorMode result = MapColorMode.DEFAULT;

		try {
			if (text.equalsIgnoreCase(MapColorModeProperties.getString(MapColorModeProperties.Default))) {
				result = MapColorMode.DEFAULT;
			} else if (text.equalsIgnoreCase(MapColorModeProperties.getString(MapColorModeProperties.BlackWhite))) {
				result = MapColorMode.BLACKWHITE;
			} else if (text.equalsIgnoreCase(MapColorModeProperties.getString(MapColorModeProperties.BlackWhiteReverse))) {
				result = MapColorMode.BLACK_WHITE_REVERSE;
			} else if (text.equalsIgnoreCase(MapColorModeProperties.getString(MapColorModeProperties.Gray))) {
				result = MapColorMode.GRAY;
			} else if (text.equalsIgnoreCase(MapColorModeProperties.getString(MapColorModeProperties.OnlyBlackWhiteReverse))) {
				result = MapColorMode.ONLY_BLACK_WHITE_REVERSE;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
