package com.supermap.desktop.utilties;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DoubleUtilties {

	private DoubleUtilties() {
		// 默认私有构造器
	}

	public static boolean equals(double d1, double d2, double pow) {
		return Math.abs(d1 - d2) < Math.pow(10, pow * (-1));
	}

	/**
	 * double直接转换为string
	 *
	 * @param value 要转换的double值
	 * @return 不为科学计数法的string
	 */
	public static String toString(double value) {
		BigDecimal bigDecimal = new BigDecimal(value);
		return bigDecimal.toString();
	}

	/**
	 * 获得指定精度的double值
	 *
	 * @param value 数值
	 * @param bit   位数
	 * @return 对应的String
	 */
	public static String toString(double value, int bit) {
		StringBuilder builder = new StringBuilder("#.");
		if (bit == 0) {
			builder.append("#");
		} else {
			for (int i = 0; i < bit; i++) {
				builder.append("#");
			}
		}
		DecimalFormat decimalFormat = new DecimalFormat(builder.toString());
		return decimalFormat.format(value);
	}
}
