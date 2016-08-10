package com.supermap.desktop.util;

import com.supermap.desktop.dataconversion.DataConversionProperties;

import javax.swing.*;

/**
 * @author Administrator 通用的字符集组合框模板类
 */
public class CommonComboBoxModel extends DefaultComboBoxModel<Object> {

	private static final long serialVersionUID = 1L;

	public CommonComboBoxModel() {
		super(new String[] {
				"ASCII",
				"ASCII  (Default)",
				"ASCII  (OEM)",
				"CHINESE_BIG5",
				"CHINESE_GB18030",
				"Cyrillic  (Windows)",
				"IA5",
				"IA5  (German)",
				"IA5  (Norweigian)",
				"IA5  (Swedish)",
				// "IS02022JP2",
				"Mac", "Unicode", "UTF-7", "UTF-8", "Windows1252",
				DataConversionProperties.getString("code_arabic"),
				DataConversionProperties.getString("code_baltic"),
				DataConversionProperties.getString("code_koreana"),
				DataConversionProperties.getString("code_koreanah"),
				DataConversionProperties.getString("code_easteuropean"),
				DataConversionProperties.getString("code_ru"),
				DataConversionProperties.getString("code_symbol"),
				DataConversionProperties.getString("code_korean"),
				DataConversionProperties.getString("code_japanese"),
				DataConversionProperties.getString("code_thai"),
				DataConversionProperties.getString("code_trukish"),
				DataConversionProperties.getString("code_hebrew"),
				DataConversionProperties.getString("code_greek"),
				DataConversionProperties.getString("code_vietnamese") });
	};
}
