package com.supermap.desktop.utilties;

import com.supermap.data.Charset;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CharsetProperties;

public class CharsetUtilties {
	private CharsetUtilties() {
		// 工具类不提供构造函数
	}

	public static String getCharsetName(Charset data) {
		String result = "";

		try {
			if (data == Charset.ANSI) {
				result = CharsetProperties.getString(CharsetProperties.ANSI);
			} else if (data == Charset.ARABIC) {
				result = CharsetProperties.getString(CharsetProperties.Arabic);
			} else if (data == Charset.BALTIC) {
				result = CharsetProperties.getString(CharsetProperties.Baltic);
			} else if (data == Charset.CHINESEBIG5) {
				result = CharsetProperties.getString(CharsetProperties.ChineseBIG5);
			} else if (data == Charset.CYRILLIC) {
				result = CharsetProperties.getString(CharsetProperties.Cyrillic);
			} else if (data == Charset.DEFAULT) {
				result = CharsetProperties.getString(CharsetProperties.Default);
			} else if (data == Charset.EASTEUROPE) {
				result = CharsetProperties.getString(CharsetProperties.EastEurope);
			} else if (data == Charset.GB18030) {
				result = CharsetProperties.getString(CharsetProperties.GB18030);
			} else if (data == Charset.GREEK) {
				result = CharsetProperties.getString(CharsetProperties.Greek);
			} else if (data == Charset.HANGEUL) {
				result = CharsetProperties.getString(CharsetProperties.Hangeul);
			} else if (data == Charset.HEBREW) {
				result = CharsetProperties.getString(CharsetProperties.Hebrew);
			} else if (data == Charset.JOHAB) {
				result = CharsetProperties.getString(CharsetProperties.Johab);
			} else if (data == Charset.KOREAN) {
				result = CharsetProperties.getString(CharsetProperties.Korean);
			} else if (data == Charset.MAC) {
				result = CharsetProperties.getString(CharsetProperties.MAC);
			} else if (data == Charset.OEM) {
				result = CharsetProperties.getString(CharsetProperties.OEM);
			} else if (data == Charset.RUSSIAN) {
				result = CharsetProperties.getString(CharsetProperties.Russian);
			} else if (data == Charset.SHIFTJIS) {
				result = CharsetProperties.getString(CharsetProperties.ShiftJIS);
			} else if (data == Charset.SYMBOL) {
				result = CharsetProperties.getString(CharsetProperties.Symbol);
			} else if (data == Charset.THAI) {
				result = CharsetProperties.getString(CharsetProperties.Thai);
			} else if (data == Charset.TURKISH) {
				result = CharsetProperties.getString(CharsetProperties.Turkish);
			} else if (data == Charset.UNICODE) {
				result = CharsetProperties.getString(CharsetProperties.Unicode);
			} else if (data == Charset.UTF8) {
				result = CharsetProperties.getString(CharsetProperties.UTF8);
			} else if (data == Charset.UTF7) {
				result = CharsetProperties.getString(CharsetProperties.UTF7);
			} else if (data == Charset.VIETNAMESE) {
				result = CharsetProperties.getString(CharsetProperties.Vietnamese);
			} else if (data == Charset.WINDOWS1252) {
				result = CharsetProperties.getString(CharsetProperties.Windows1252);
			} else if (data == Charset.XIA5) {
				result = CharsetProperties.getString(CharsetProperties.XIA5);
			} else if (data == Charset.XIA5GERMAN) {
				result = CharsetProperties.getString(CharsetProperties.XIA5German);
			} else if (data == Charset.XIA5NORWEGIAN) {
				result = CharsetProperties.getString(CharsetProperties.XIA5Norwegian);
			} else if (data == Charset.XIA5SWEDISH) {
				result = CharsetProperties.getString(CharsetProperties.XIA5Swedish);
			} else if (data == null) {
				result = CharsetProperties.getString(CharsetProperties.Unsupport);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static Charset valueOf(String text) {
		Charset result = null;

		try {
			if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.ANSI))) {
				result = Charset.ANSI;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Arabic))) {
				result = Charset.ARABIC;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Baltic))) {
				result = Charset.BALTIC;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.ChineseBIG5))) {
				result = Charset.CHINESEBIG5;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Cyrillic))) {
				result = Charset.CYRILLIC;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Default))) {
				result = Charset.DEFAULT;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.EastEurope))) {
				result = Charset.EASTEUROPE;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.GB18030))) {
				result = Charset.GB18030;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Greek))) {
				result = Charset.GREEK;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Hangeul))) {
				result = Charset.HANGEUL;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Hebrew))) {
				result = Charset.HEBREW;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Johab))) {
				result = Charset.JOHAB;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Korean))) {
				result = Charset.KOREAN;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.MAC))) {
				result = Charset.MAC;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.OEM))) {
				result = Charset.OEM;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Russian))) {
				result = Charset.RUSSIAN;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.ShiftJIS))) {
				result = Charset.SHIFTJIS;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Symbol))) {
				result = Charset.SYMBOL;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Thai))) {
				result = Charset.THAI;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Turkish))) {
				result = Charset.TURKISH;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Unicode))) {
				result = Charset.UNICODE;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.UTF7))) {
				result = Charset.UTF7;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.UTF8))) {
				result = Charset.UTF8;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Vietnamese))) {
				result = Charset.VIETNAMESE;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.Windows1252))) {
				result = Charset.WINDOWS1252;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.XIA5))) {
				result = Charset.XIA5;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.XIA5German))) {
				result = Charset.XIA5GERMAN;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.XIA5Norwegian))) {
				result = Charset.XIA5NORWEGIAN;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.XIA5Swedish))) {
				result = Charset.XIA5SWEDISH;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
