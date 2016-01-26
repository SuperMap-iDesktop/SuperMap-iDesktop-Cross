package com.supermap.desktop.utilties;

import com.supermap.desktop.properties.CoreProperties;
import com.supermap.ui.SelectionMode;

/**
 * SelectionMode转化类
 */
public class SelectionModeUtilties {
	private SelectionModeUtilties() {
		// 公共类不提供构造函数
	}

	/**
	 * SelectionMode -> Sting
	 *
	 * @param selectionMode
	 * @return
	 */
	public static String toString(SelectionMode selectionMode) {
		if (selectionMode == SelectionMode.CONTAIN_INNER_POINT) {
			return CoreProperties.getString("String_SelectionMode_ContainInnerPoint");
		} else if (selectionMode == SelectionMode.CONTAIN_OBJECT) {
			return CoreProperties.getString("String_SelectionMode_ContainObject");
		} else if (selectionMode == SelectionMode.INTERSECT) {
			return CoreProperties.getString("String_SelectionMode_Intersect");
		}
		return "";
	}

	/**
	 * String -> SelectionMode
	 *
	 * @param name
	 * @return
	 */
	public static SelectionMode getValue(String name) {
		if (name.equals(CoreProperties.getString("String_SelectionMode_ContainInnerPoint"))) {
			return SelectionMode.CONTAIN_INNER_POINT;
		} else if (name.equals(CoreProperties.getString("String_SelectionMode_ContainObject"))) {
			return SelectionMode.CONTAIN_OBJECT;
		} else if (name.equals(CoreProperties.getString("String_SelectionMode_Intersect"))) {
			return SelectionMode.INTERSECT;
		}
		return null;
	}
}
