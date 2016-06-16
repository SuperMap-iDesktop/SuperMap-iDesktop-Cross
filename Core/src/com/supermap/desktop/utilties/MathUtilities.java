package com.supermap.desktop.utilties;

public class MathUtilities {

	private MathUtilities() {
		// 工具类，不提供构造方法
	}

	public static double log(double a, double newBase) {
		return Math.log(a) / Math.log(newBase);
	}

	/**
	 * 判断数组是否为连续数组
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isContinuouslyArray(int[] array) {
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (i + 1 != array.length && array[i + 1] - array[i] == 1) {
				count++;
			}
		}
		if (count == array.length - 1 && array.length > 1) {
			return true;
		}
		return false;
	}

}
