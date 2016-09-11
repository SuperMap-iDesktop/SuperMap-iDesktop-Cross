package com.supermap.desktop.utilities;

import com.supermap.data.Charset;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CharsetProperties;

public class CharsetUtilities {
	private CharsetUtilities() {
		// 工具类不提供构造函数
	}

	public static String toString(Charset data) {
		String result = "";

		try {
			if (data == Charset.ANSI) {
				result = CharsetProperties.getString(CharsetProperties.ANSI);
			} else if (data == Charset.ARABIC) {
                result = CharsetProperties.getString(CharsetProperties.ARABIC);
            } else if (data == Charset.BALTIC) {
                result = CharsetProperties.getString(CharsetProperties.BALTIC);
            } else if (data == Charset.CHINESEBIG5) {
                result = CharsetProperties.getString(CharsetProperties.CHINESEBIG5);
            } else if (data == Charset.CYRILLIC) {
                result = CharsetProperties.getString(CharsetProperties.CYRILLIC);
            } else if (data == Charset.DEFAULT) {
                result = CharsetProperties.getString(CharsetProperties.DEFAULT);
            } else if (data == Charset.EASTEUROPE) {
                result = CharsetProperties.getString(CharsetProperties.EASTEUROPE);
            } else if (data == Charset.GB18030) {
				result = CharsetProperties.getString(CharsetProperties.GB18030);
			} else if (data == Charset.GREEK) {
                result = CharsetProperties.getString(CharsetProperties.GREEK);
            } else if (data == Charset.HANGEUL) {
                result = CharsetProperties.getString(CharsetProperties.HANGEUL);
            } else if (data == Charset.HEBREW) {
                result = CharsetProperties.getString(CharsetProperties.HEBREW);
            } else if (data == Charset.JOHAB) {
                result = CharsetProperties.getString(CharsetProperties.JOHAB);
            } else if (data == Charset.KOREAN) {
                result = CharsetProperties.getString(CharsetProperties.KOREAN);
            } else if (data == Charset.MAC) {
				result = CharsetProperties.getString(CharsetProperties.MAC);
			} else if (data == Charset.OEM) {
				result = CharsetProperties.getString(CharsetProperties.OEM);
			} else if (data == Charset.RUSSIAN) {
                result = CharsetProperties.getString(CharsetProperties.RUSSIAN);
            } else if (data == Charset.SHIFTJIS) {
                result = CharsetProperties.getString(CharsetProperties.SHIFTJIS);
            } else if (data == Charset.SYMBOL) {
                result = CharsetProperties.getString(CharsetProperties.SYMBOL);
            } else if (data == Charset.THAI) {
                result = CharsetProperties.getString(CharsetProperties.THAI);
            } else if (data == Charset.TURKISH) {
                result = CharsetProperties.getString(CharsetProperties.TURKISH);
            } else if (data == Charset.UNICODE) {
                result = CharsetProperties.getString(CharsetProperties.UNICODE);
            } else if (data == Charset.UTF8) {
				result = CharsetProperties.getString(CharsetProperties.UTF8);
			} else if (data == Charset.UTF7) {
				result = CharsetProperties.getString(CharsetProperties.UTF7);
			} else if (data == Charset.VIETNAMESE) {
                result = CharsetProperties.getString(CharsetProperties.VIETNAMESE);
            } else if (data == Charset.WINDOWS1252) {
                result = CharsetProperties.getString(CharsetProperties.WINDOWS1252);
            } else if (data == Charset.XIA5) {
				result = CharsetProperties.getString(CharsetProperties.XIA5);
			} else if (data == Charset.XIA5GERMAN) {
                result = CharsetProperties.getString(CharsetProperties.XIA5GERMAN);
            } else if (data == Charset.XIA5NORWEGIAN) {
                result = CharsetProperties.getString(CharsetProperties.XIA5NORWEGIAN);
            } else if (data == Charset.XIA5SWEDISH) {
                result = CharsetProperties.getString(CharsetProperties.XIA5SWEDISH);
            } else if (data == null) {
                result = CharsetProperties.getString(CharsetProperties.UNSUPPORT);
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
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.ARABIC))) {
                result = Charset.ARABIC;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.BALTIC))) {
                result = Charset.BALTIC;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.CHINESEBIG5))) {
                result = Charset.CHINESEBIG5;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.CYRILLIC))) {
                result = Charset.CYRILLIC;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.DEFAULT))) {
                result = Charset.DEFAULT;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.EASTEUROPE))) {
                result = Charset.EASTEUROPE;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.GB18030))) {
				result = Charset.GB18030;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.GREEK))) {
                result = Charset.GREEK;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.HANGEUL))) {
                result = Charset.HANGEUL;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.HEBREW))) {
                result = Charset.HEBREW;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.JOHAB))) {
                result = Charset.JOHAB;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.KOREAN))) {
                result = Charset.KOREAN;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.MAC))) {
				result = Charset.MAC;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.OEM))) {
				result = Charset.OEM;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.RUSSIAN))) {
                result = Charset.RUSSIAN;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.SHIFTJIS))) {
                result = Charset.SHIFTJIS;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.SYMBOL))) {
                result = Charset.SYMBOL;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.THAI))) {
                result = Charset.THAI;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.TURKISH))) {
                result = Charset.TURKISH;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.UNICODE))) {
                result = Charset.UNICODE;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.UTF7))) {
				result = Charset.UTF7;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.UTF8))) {
				result = Charset.UTF8;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.VIETNAMESE))) {
                result = Charset.VIETNAMESE;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.WINDOWS1252))) {
                result = Charset.WINDOWS1252;
			} else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.XIA5))) {
				result = Charset.XIA5;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.XIA5GERMAN))) {
                result = Charset.XIA5GERMAN;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.XIA5NORWEGIAN))) {
                result = Charset.XIA5NORWEGIAN;
            } else if (text.equalsIgnoreCase(CharsetProperties.getString(CharsetProperties.XIA5SWEDISH))) {
                result = Charset.XIA5SWEDISH;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
