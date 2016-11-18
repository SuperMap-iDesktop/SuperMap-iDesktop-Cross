package com.supermap.desktop.utilities;

import com.supermap.desktop.GlobalParameters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class DoubleUtilities {


	private DoubleUtilities() {
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
		if (Double.isNaN(value)) {
			return "NaN";
		}
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

	/**
	 * double 除法
	 *
	 * @param d1
	 * @param d2
	 * @param scale 四舍五入 小数点位数
	 * @return
	 */
	public static double div(double d1, double d2, int scale) {
		// 当然在此之前，你要判断分母是否为0，
		// 为0你可以根据实际需求做相应的处理

		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static String toMaxLengthString(double d, int maxLength) {
		if (d < 0) {
			--maxLength;
		}
		int length = String.valueOf((long) d).length() + 1;
		if (length >= maxLength) {
			return String.valueOf((long) d);
		} else {
			return DoubleUtilities.toString(d, maxLength - length);
		}
	}

	/**
	 * 判断字符串是否为double
	 *
	 * @param s 需要判断的字符串
	 * @return true->是double
	 */
	public static boolean isDouble(String s) {
		if (StringUtilities.isNullOrEmpty(s)) {
			return false;
		}
		try {
			Double aDouble = Double.valueOf(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否为double而且不以d结尾
	 *
	 * @param s 需要判断的字符串
	 * @return true->是double
	 */
	public static boolean isDoubleWithoutD(String s) {
		return !(s == null || s.contains("d")) && isDouble(s);
	}

	public static int intValue(double value) {
		Double d = new Double(value);
		return d.intValue();
	}

	public static String getFormatString(double value) {

		return getDoubleFormatInstance().format(value);
	}

	public static NumberFormat getDoubleFormatInstance() {
//		NumberFormat numberFormat = NumberFormat.getInstance();
//		numberFormat.setMaximumFractionDigits(8);
//		numberFormat.setMinimumIntegerDigits(0);
//		return numberFormat;
		return DoubleFormatter.getNumberFormat();
	}

	public static Double stringToValue(String scaleString) {
		Double value = null;
		try {
			value = Double.valueOf(String.valueOf(getDoubleFormatInstance().parse(scaleString)));
		} catch (Exception e) {
			// ignore
		}
		return value;
	}
}

class DoubleFormatter extends NumberFormat {
	private static int MaxDigits = 8;
	private static NumberFormat numberFormat;
	private static DoubleFormatter doubleFormatterInstance;

	public DoubleFormatter() {
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(MaxDigits);
		numberFormat.setMinimumIntegerDigits(1);
	}


//	@Override
//	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
//		if (!GlobalParameters.isUseThousandPointDivision()) {
//			if (obj != null) {
//				String str = String.valueOf(obj);
//				toAppendTo.append(str);
//				pos.setBeginIndex(0);
//				pos.setEndIndex(str.length());
//			}
//			return toAppendTo;
//		}
//		return numberFormat.format(obj, toAppendTo, pos);
//
//	}


	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
		if (!GlobalParameters.isUseThousandPointDivision()) {
			toAppendTo.append(String.valueOf(number));
			return toAppendTo;
		}
		return numberFormat.format(number, toAppendTo, pos);
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		if (!GlobalParameters.isUseThousandPointDivision()) {
			toAppendTo.append(String.valueOf(number));
			return toAppendTo;
		}
		return numberFormat.format(number, toAppendTo, pos);
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		return numberFormat.parse(source, parsePosition);
	}

	public static DoubleFormatter getNumberFormat() {
		if (doubleFormatterInstance == null) {
			doubleFormatterInstance = new DoubleFormatter();
		}
		return doubleFormatterInstance;
	}
}
