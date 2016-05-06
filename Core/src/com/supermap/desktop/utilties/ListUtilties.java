package com.supermap.desktop.utilties;

import com.supermap.desktop.Interface.ICloneable;

import java.util.ArrayList;
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
	@SafeVarargs
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
	@SafeVarargs
	public static <T> boolean isListOnlyContain(List<T> list, T... checkItems) {
		boolean result = true;
		if (list.size() > 0 && checkItems.length > 0) {
			for (T t : list) {
				if (!ArrayUtilties.isArrayContains(checkItems, t)) {
					result = false;
					break;
				}
			}
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * 判断 List 是否包含指定参数列表中所有的值，是则返回 true，否则返回 false
	 * 
	 * @param list
	 * @param checkItems
	 * @return
	 */
	@SafeVarargs
	public static <T> boolean isListContainAll(List<T> list, T... checkItems) {
		boolean result = true;

		if (list.size() > 0 && checkItems.length > 0) {
			for (T t : checkItems) {
				if (!list.contains(t)) {
					result = false;
					break;
				}
			}
		} else {
			result = false;
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

	/**
	 * 将数组添加到指定集合，保持单值
	 * 
	 * @param list
	 * @param array
	 */
	public static <T> void addArraySingle(List<T> list, T[] array) {
		if (list == null || array == null || array.length == 0) {
			return;
		}

		for (int i = 0; i < array.length; i++) {
			if (!list.contains(array[i])) {
				list.add(array[i]);
			}
		}
	}

	public static <T> List<T> listCopy(List<T> list) {
		if (list == null || list.size() <= 0) {
			return new ArrayList<>();
		}
		if (list.get(0) instanceof ICloneable) {
			return ListUtilties.listDeepCopy(list);
		}
		return ListUtilties.listShallowCopy(list);
	}

	public static <T> List<T> listShallowCopy(List<T> list) {
		List<T> result = new ArrayList<>();
		result.addAll(list);
		return result;
	}

	public static <T> List<T> listDeepCopy(List<T> list) {
		if (list == null || list.size() <= 0) {
			return new ArrayList<>();
		}
		if (!(list.get(0) instanceof ICloneable)) {
			return listShallowCopy(list);
		}
		List<T> result = new ArrayList<>(list.size());
		for (T t : list) {
			result.add((T) ((ICloneable) t).clone());
		}
		return result;
	}
}
