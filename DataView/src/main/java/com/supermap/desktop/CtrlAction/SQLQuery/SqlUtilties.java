package com.supermap.desktop.CtrlAction.SQLQuery;

import com.supermap.desktop.dataview.DataViewProperties;

/**
 * Created by Administrator on 2015/12/8.
 */
public class SqlUtilties {
	private static final String[] needSizeChar = new String[]{"'", "\"", "(", "[", "{"};

	private SqlUtilties() {
		//公共类不提供构造方法
	}

	public static int resizeCursorPlace(String data) {
		for (String aNeedSizeChar : needSizeChar) {
			int index = data.indexOf(aNeedSizeChar);
			if (index != -1) {
				return index - data.length() + 1;
			}
		}
		return 0;
	}

	/**
	 * asc -> 升序
	 *
	 * @param data
	 * @return
	 */
	public static String format(String data) {
		if (data.equals("asc")) {
			return DataViewProperties.getString("String_SQLQueryASC");
		} else if (data.equals("desc")) {
			return DataViewProperties.getString("String_Descend_D");
		} else {
			return "";
		}
	}

	/**
	 * 升序 -> asc
	 *
	 * @param data
	 * @return
	 */
	public static String covert(String data) {
		if (data.equals(DataViewProperties.getString("String_SQLQueryASC"))) {
			return "asc";
		} else if (data.equals(DataViewProperties.getString("String_Descend_D"))) {
			return "desc";
		} else {
			return "";
		}
	}
}
