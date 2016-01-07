package com.supermap.desktop.utilties;

import com.supermap.desktop.Application;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtilties {
	private StringUtilties() {
		// 工具类不提供构造函数
	}

	public static boolean isNullOrEmptyString(Object obj) {
		if (obj == null) {
			return true;
		}

		if (!(obj instanceof String)) {
			return true;
		}

		if (isTrimString((String) obj)) {
			return true;
		}

		return ((String) obj).isEmpty();
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static boolean stringEquals(String str1, String str2) {
		return stringEquals(str1, str2, true);
	}

	private static boolean isTrimString(String str) {
		return 0 == str.trim().length();
	}

	/**
	 * 判断是否符合正则表达式
	 *
	 * @param regex
	 * @param orginal
	 * @return
	 */
	private static boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	/**
	 * 判断是否为正整数
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isPositiveInteger(String orginal) {
		return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
	}

	/**
	 * 判断是否为负整数
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isNegativeInteger(String orginal) {
		return isMatch("^-[1-9]\\d*", orginal);
	}

	/**
	 * 判断是否全为数字
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isWholeNumber(String orginal) {
		return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
	}

	/**
	 * 判断是否为正浮点数
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isPositiveDecimal(String orginal) {
		return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
	}

	/**
	 * 判断是否为负浮点数
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isNegativeDecimal(String orginal) {
		return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
	}

	/**
	 * 判断是否为浮点数
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isDecimal(String orginal) {
		return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
	}

	/**
	 * 判断是否为数字
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isNumber(String orginal) {
		return isWholeNumber(orginal) || isDecimal(orginal);
	}

	/**
	 * 判断字符串是否为整数
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0; ) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean stringEquals(String str1, String str2, boolean ignoreCase) {
		boolean result = false;

		try {
			// 同时为空，视为相同。null 和 空字符视为相同。
			if (isNullOrEmpty(str1) && isNullOrEmpty(str2)) {
				return true;
			}

			// 执行到这里时，表示不同时为空
			// 如果 str1 不为空，正常比较，得到结果
			// else str1 为空，那么 str2 一定不为空，result 保持 false 即可
			if (!isNullOrEmpty(str1)) {
				if (ignoreCase) {
					result = str1.equalsIgnoreCase(str2);
				} else {
					result = str1.equals(str2);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
