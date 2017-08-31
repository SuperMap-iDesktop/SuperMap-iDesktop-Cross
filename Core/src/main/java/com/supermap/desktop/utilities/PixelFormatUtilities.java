package com.supermap.desktop.utilities;

import com.supermap.data.PixelFormat;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.PixelFormatProperties;

public class PixelFormatUtilities {
	private PixelFormatUtilities() {
		// 工具类不提供构造函数
	}

	public static String toString(PixelFormat data) {
		String result = "";

		try {
			if (data == PixelFormat.RGB) {
				result = PixelFormatProperties.getString(PixelFormatProperties.RGB);
			} else if (data == PixelFormat.RGBA) {
				result = PixelFormatProperties.getString(PixelFormatProperties.RGBA);
			} else if (data == PixelFormat.DOUBLE) {
				result = PixelFormatProperties.getString(PixelFormatProperties.Double);
			} else if (data == PixelFormat.SINGLE) {
				result = PixelFormatProperties.getString(PixelFormatProperties.Single);
			} else if (data == PixelFormat.BIT8) {
				result = PixelFormatProperties.getString(PixelFormatProperties.Bit8);
			} else if (data == PixelFormat.BIT16) {
				result = PixelFormatProperties.getString(PixelFormatProperties.Bit16);
			} else if (data == PixelFormat.BIT32) {
				result = PixelFormatProperties.getString(PixelFormatProperties.Bit32);
			} else if (data == PixelFormat.BIT64) {
				result = PixelFormatProperties.getString(PixelFormatProperties.Bit64);
			} else if (data == PixelFormat.UBIT1) {
				result = PixelFormatProperties.getString(PixelFormatProperties.UBit1);
			} else if (data == PixelFormat.UBIT4) {
				result = PixelFormatProperties.getString(PixelFormatProperties.UBit4);
			} else if (data == PixelFormat.UBIT8) {
				result = PixelFormatProperties.getString(PixelFormatProperties.UBit8);
			} else if (data == PixelFormat.UBIT16) {
				result = PixelFormatProperties.getString(PixelFormatProperties.UBit16);
			} else if (data == PixelFormat.UBIT32) {
				result = PixelFormatProperties.getString(PixelFormatProperties.UBit32);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static PixelFormat valueOf(String text) {
		PixelFormat result = PixelFormat.BIT16;

		try {
			if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.RGB))) {
				result = PixelFormat.RGB;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.RGBA))) {
				result = PixelFormat.RGBA;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.Double))) {
				result = PixelFormat.DOUBLE;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.Single))) {
				result = PixelFormat.SINGLE;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.Bit8))) {
				result = PixelFormat.BIT8;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.Bit16))) {
				result = PixelFormat.BIT16;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.Bit32))) {
				result = PixelFormat.BIT32;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.Bit64))) {
				result = PixelFormat.BIT64;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.UBit1))) {
				result = PixelFormat.UBIT1;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.UBit4))) {
				result = PixelFormat.UBIT4;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.UBit8))) {
				result = PixelFormat.UBIT8;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.UBit16))) {
				result = PixelFormat.UBIT16;
			} else if (text.equalsIgnoreCase(PixelFormatProperties.getString(PixelFormatProperties.UBit32))) {
				result = PixelFormat.UBIT32;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	// 根据枚举值获取像素格式
	public static PixelFormat valueOf(Integer pixelFormatValue) {
		PixelFormat result = PixelFormat.BIT16;

		try {
			if (pixelFormatValue.equals(24)) {
				result = PixelFormat.RGB;
			} else if (pixelFormatValue.equals(32)) {
				result = PixelFormat.RGBA;
			} else if (pixelFormatValue.equals(6400)) {
				result = PixelFormat.DOUBLE;
			} else if (pixelFormatValue.equals(3200)) {
				result = PixelFormat.SINGLE;
			} else if (pixelFormatValue.equals(80)) {
				result = PixelFormat.BIT8;
			} else if (pixelFormatValue.equals(16)) {
				result = PixelFormat.BIT16;
			} else if (pixelFormatValue.equals(320)) {
				result = PixelFormat.BIT32;
			} else if (pixelFormatValue.equals(64)) {
				result = PixelFormat.BIT64;
			} else if (pixelFormatValue.equals(1)) {
				result = PixelFormat.UBIT1;
			} else if (pixelFormatValue.equals(4)) {
				result = PixelFormat.UBIT4;
			} else if (pixelFormatValue.equals(8)) {
				result = PixelFormat.UBIT8;
			} else if (pixelFormatValue.equals(160)) {
				result = PixelFormat.UBIT16;
			} else if (pixelFormatValue.equals(321)) {
				result = PixelFormat.UBIT32;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
