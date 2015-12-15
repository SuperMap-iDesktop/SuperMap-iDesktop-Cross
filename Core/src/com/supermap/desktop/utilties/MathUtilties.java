package com.supermap.desktop.utilties;

public class MathUtilties {

	private MathUtilties() {
		// 工具类，不提供构造方法
	}

	public static double log(double a, double newBase) {
		return Math.log10(newBase) / Math.log10(a);
	}

	public static boolean isContiuityArray(int[] array) {
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (i + 1 != array.length && array[i + 1] - array[i] == 1) {
				count++;
			}
		}
		if (count == array.length - 1) {
			return true;
		}
		return false;
	}
}
