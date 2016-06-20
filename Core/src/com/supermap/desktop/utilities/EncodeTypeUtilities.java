package com.supermap.desktop.utilities;

import com.supermap.data.EncodeType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.EncodeTypeProperties;

public class EncodeTypeUtilities {
	private EncodeTypeUtilities() {
		// 工具类不提供构造函数
	}

	public static String toString(EncodeType data) {
		String result = "";

		try {
			if (data == EncodeType.BYTE) {
				result = EncodeTypeProperties.getString(EncodeTypeProperties.Byte);
			} else if (data == EncodeType.COMPOUND) {
				result = EncodeTypeProperties.getString(EncodeTypeProperties.Compound);
			} else if (data == EncodeType.DCT) {
				result = EncodeTypeProperties.getString(EncodeTypeProperties.DCT);
			} else if (data == EncodeType.INT16) {
				result = EncodeTypeProperties.getString(EncodeTypeProperties.Int16);
			} else if (data == EncodeType.INT24) {
				result = EncodeTypeProperties.getString(EncodeTypeProperties.Int24);
			} else if (data == EncodeType.INT32) {
				result = EncodeTypeProperties.getString(EncodeTypeProperties.Int32);
			} else if (data == EncodeType.LZW) {
				result = EncodeTypeProperties.getString(EncodeTypeProperties.LZW);
			} else if (data == EncodeType.NONE) {
				result = EncodeTypeProperties.getString(EncodeTypeProperties.None);
			} else if (data == EncodeType.PNG) {
				result = EncodeTypeProperties.getString(EncodeTypeProperties.PNG);
			} else if (data == EncodeType.SGL) {
				result = EncodeTypeProperties.getString(EncodeTypeProperties.SGL);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static EncodeType valueOf(String text) {
		EncodeType result = EncodeType.NONE;

		try {
			if (text.equalsIgnoreCase(EncodeTypeProperties.getString(EncodeTypeProperties.Byte))) {
				result = EncodeType.BYTE;
			} else if (text.equalsIgnoreCase(EncodeTypeProperties.getString(EncodeTypeProperties.Compound))) {
				result = EncodeType.COMPOUND;
			} else if (text.equalsIgnoreCase(EncodeTypeProperties.getString(EncodeTypeProperties.DCT))) {
				result = EncodeType.DCT;
			} else if (text.equalsIgnoreCase(EncodeTypeProperties.getString(EncodeTypeProperties.Int16))) {
				result = EncodeType.INT16;
			} else if (text.equalsIgnoreCase(EncodeTypeProperties.getString(EncodeTypeProperties.Int24))) {
				result = EncodeType.INT24;
			} else if (text.equalsIgnoreCase(EncodeTypeProperties.getString(EncodeTypeProperties.Int32))) {
				result = EncodeType.INT32;
			} else if (text.equalsIgnoreCase(EncodeTypeProperties.getString(EncodeTypeProperties.LZW))) {
				result = EncodeType.LZW;
			} else if (text.equalsIgnoreCase(EncodeTypeProperties.getString(EncodeTypeProperties.None))) {
				result = EncodeType.NONE;
			} else if (text.equalsIgnoreCase(EncodeTypeProperties.getString(EncodeTypeProperties.PNG))) {
				result = EncodeType.PNG;
			} else if (text.equalsIgnoreCase(EncodeTypeProperties.getString(EncodeTypeProperties.SGL))) {
				result = EncodeType.SGL;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
