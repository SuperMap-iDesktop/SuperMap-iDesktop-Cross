package com.supermap.desktop.utilties;

import com.supermap.data.FieldType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoreProperties;

public class FieldTypeUtilties {

	private FieldTypeUtilties() {
		// 工具类不提供构造函数
	}

	/**
	 * 根据字段类型获取对应的资源字符串
	 * 
	 * @param fieldType
	 * @return
	 */
	public static String getFieldTypeName(FieldType fieldType) {
		String type = CoreProperties.getString("String_FieldType_Unknown");
		try {
			if (fieldType == FieldType.BOOLEAN) {
				type = CoreProperties.getString(CoreProperties.Boolean);
			} else if (fieldType == FieldType.DATETIME) {
				type = CoreProperties.getString(CoreProperties.DateTime);
			} else if (fieldType == FieldType.DOUBLE) {
				type = CoreProperties.getString(CoreProperties.Double);
			} else if (fieldType == FieldType.BYTE) {
				type = CoreProperties.getString(CoreProperties.Byte);
			} else if (fieldType == FieldType.INT16) {
				type = CoreProperties.getString(CoreProperties.Short);
			} else if (fieldType == FieldType.INT32) {
				type = CoreProperties.getString(CoreProperties.Integer);
			} else if (fieldType == FieldType.INT64) {
				type = CoreProperties.getString(CoreProperties.Long);
			} else if (fieldType == FieldType.LONGBINARY) {
				type = CoreProperties.getString(CoreProperties.LongBinary);
			} else if (fieldType == FieldType.CHAR) {
				type = CoreProperties.getString(CoreProperties.Char);
			} else if (fieldType == FieldType.SINGLE) {
				type = CoreProperties.getString(CoreProperties.Float);
			} else if (fieldType == FieldType.TEXT) {
				type = CoreProperties.getString(CoreProperties.Text);
			} else if (fieldType == FieldType.WTEXT) {
				type = CoreProperties.getString(CoreProperties.WText);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return type;
	}

	/**
	 * 根据字段类型资源字符串获取对应的字段类型
	 * 
	 * @param strFieldType
	 * @return
	 */
	public static FieldType getFieldType(String strFieldType) {
		FieldType type = FieldType.TEXT;
		try {
			if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Boolean))) {
				type = FieldType.BOOLEAN;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.DateTime))) {
				type = FieldType.DATETIME;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Double))) {
				type = FieldType.DOUBLE;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Byte))) {
				type = FieldType.BYTE;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Short))) {
				type = FieldType.INT16;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Integer))) {
				type = FieldType.INT32;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Long))) {
				type = FieldType.INT64;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.LongBinary))) {
				type = FieldType.LONGBINARY;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Char))) {
				type = FieldType.CHAR;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Float))) {
				type = FieldType.SINGLE;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Text))) {
				type = FieldType.TEXT;
			} else if (strFieldType.equalsIgnoreCase(CoreProperties.getString(CoreProperties.WText))) {
				type = FieldType.WTEXT;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return type;
	}

	/**
	 * 判断字段是否数值型
	 * 
	 * @param fieldType
	 * @return
	 */
	public static boolean isNumber(FieldType fieldType) {
		return fieldType == FieldType.BYTE || fieldType == FieldType.DOUBLE || fieldType == FieldType.INT16 || fieldType == FieldType.INT32
				|| fieldType == FieldType.INT64 || fieldType == FieldType.SINGLE;
	}

	/**
	 * 判断字段是否字符串型
	 * 
	 * @param fieldType
	 * @return
	 */
	public static boolean isString(FieldType fieldType) {
		return fieldType == FieldType.WTEXT || fieldType == FieldType.TEXT;
	}

	/**
	 * 获取字段类型对应的长度
	 */
	public static int getFieldTypeMaxLength(FieldType fieldType) {
		if (fieldType == FieldType.BYTE) {
			return 1;
		}
		if (fieldType == FieldType.INT32) {
			return 4;
		}
		if (fieldType == FieldType.INT16) {
			return 2;
		}
		if (fieldType == FieldType.DOUBLE) {
			return 8;
		}
		if (fieldType == FieldType.SINGLE) {
			return 4;
		}
		if (fieldType == FieldType.INT64) {
			return 8;
		}
		if (fieldType == FieldType.BOOLEAN) {
			return 1;
		}
		if (fieldType == FieldType.DATETIME) {
			return 8;
		}
		if (fieldType == FieldType.LONGBINARY) {
			return 0;
		}
		if (fieldType == FieldType.CHAR) {
			return 1;
		}
		return 255;

	}
}
