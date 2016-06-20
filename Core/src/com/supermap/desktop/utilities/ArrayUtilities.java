package com.supermap.desktop.utilities;

public class ArrayUtilities {

	private ArrayUtilities() {
		// 工具类，不提供构造函数
	}

	public static int[] convertToInt(Integer[] integers) {
		if (integers != null) {
			int[] ints = new int[integers.length];

			for (int i = 0; i < integers.length; i++) {
				ints[i] = integers[i].intValue();
			}
			return ints;
		}

		return null;
	}

	public static Integer[] convertToInteger(int[] ints) {
		if (ints != null) {
			Integer[] integers = new Integer[ints.length];

			for (int i = 0; i < ints.length; i++) {
				integers[i] = new Integer(ints[i]);
			}
			return integers;
		}

		return null;
	}

	public static <T> boolean isArrayContains(T[] array, T item) {
		boolean result = false;

		for (int i = 0; i < array.length; i++) {
			if (array[i] == item) {
				result = true;
				break;
			}
		}
		return result;
	}
}
