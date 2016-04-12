package com.supermap.desktop.utilties;

import java.util.List;

public class ListUtilties {

	private ListUtilties() {
		// 工具类，不提供构造方法
	}

	/**
	 * 判断 List 是否包含指定参数列表中的任意一个值，包含则返回 true，不包含则返回 false
	 * 
	 * @param list
	 * @param checkItems
	 * @return
	 */
	public static <T> boolean isListContainAny(List<T> list, T... checkItems) {
		boolean result = false;
		if (list.size() > 0 && checkItems.length > 0) {
			for (int i = 0; i < checkItems.length; i++) {
				T checkItem = checkItems[i];

				if (list.contains(checkItem)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 判断 List 是否仅包含指定参数列表中的值，是则返回 true，否则返回 false
	 * 
	 * @param list
	 * @param checkItems
	 * @return
	 */
	public static <T> boolean isListOnlyContain(List<T> list, T... checkItems) {
		boolean result = true;
		if (list.size() > 0 && checkItems.length > 0) {
			for (T t : list) {
				if (!ArrayUtilties.isArrayContains(checkItems, t)) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 将数组添加到指定集合
	 * 
	 * @param list
	 * @param array
	 */
	public static <T> void addArray(List<T> list, T[] array) {
		if (list == null || array == null || array.length == 0) {
			return;
		}

		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
	}
}
