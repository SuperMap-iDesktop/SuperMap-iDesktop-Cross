package com.supermap.desktop.utilities;

import com.supermap.desktop.Application;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtilities {
	private StringUtilities() {
		// 工具类不提供构造函数
	}

	public static boolean isNullOrEmptyString(Object obj) {
		if (obj == null) {
			return true;
		}

		if (isTrimString(obj.toString())) {
			return true;
		}

		return (obj.toString()).isEmpty();
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
	 * 判断是否符合正则表达式，匹配整个字符串
	 *
	 * @param regex
	 * @param orginal
	 * @return
	 */
	public static boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(orginal);
		return matcher.matches();
	}

	/**
	 * 匹配字符串中的一部分
	 *
	 * @param regex
	 * @param orginal
	 * @return
	 */
	public static boolean isFind(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(orginal);
		return matcher.find();
	}

	/**
	 * 判断是否为正整数
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isPositiveInteger(String orginal) {
		return isMatch("^\\+?[1-9]\\d*$", orginal);
	}

	/**
	 * 判断是否为负整数
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isNegativeInteger(String orginal) {
		return isMatch("^-[1-9]\\d*$", orginal);
	}

	/**
	 * 判断是否全为数字
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isWholeNumber(String orginal) {
		return isMatch("^[+-]?0$", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
	}

	/**
	 * 判断是否为正浮点数
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isPositiveDecimal(String orginal) {
		return isMatch("^\\+?[0]\\.[1-9]*$|^\\+?[1-9]\\d*\\.\\d*$", orginal);
	}

	/**
	 * 判断是否为负浮点数
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isNegativeDecimal(String orginal) {
		return isMatch("^-[0]\\.[1-9]*$|^-[1-9]\\d*\\.\\d*$", orginal);
	}

	/**
	 * 判断是否为浮点数
	 *
	 * @param orginal
	 * @return
	 */
	public static boolean isDecimal(String orginal) {
		return isMatch("^[-+]?\\d+\\.\\d*$|^[-+]?\\d*\\.\\d+$", orginal);
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

	public static double getNumber(String s) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= '0' && c <= '9') || c == '.' || c == '-') {
				builder.append(c);
			}
		}
		double result;
		try {
			result = Double.valueOf(builder.toString());
		} catch (Exception e) {
			result = 0.0;
		}
		return result;
	}

	public static String getUniqueName(String newName, List<String> existNames) {
		if (newName == null || existNames == null || existNames.size() <= 0) {
			return newName;
		}
		return getUniqueName(newName, existNames, 0);
	}

	private static String getUniqueName(String newName, List<String> existNames, int i) {
		String tempName = i == 0 ? newName : newName + "_" + i;
		i++;
		for (; existNames.contains(tempName); i++) {
			tempName = newName + "_" + i;
		}
		return tempName;
	}


	/**
	 * 模糊搜索如
	 * isContain(AB,a) = true;
	 * isContain(a,Ab) = false;
	 *
	 * @param searchString 被搜索的字符串
	 * @param inputString  搜索的字符串
	 * @return 是否匹配
	 */
	public static boolean isContain(String searchString, String inputString) {

		if (StringUtilties.isNullOrEmpty(searchString)) {
			// 为空始终显示
			return true;
		}

		if (StringUtilties.isNullOrEmpty(inputString)) {
			return true;
		}

//		StringBuilder stringBuilder = new StringBuilder(".*");
//		for (int i = 0; i < inputString.length(); i++) {
//			stringBuilder.append("[");
//			stringBuilder.append(Character.toLowerCase(inputString.charAt(i)));
//			stringBuilder.append(Character.toUpperCase(inputString.charAt(i)));
//			stringBuilder.append("]");
//		}
//		stringBuilder.append(".*");
//		return searchString.matches(stringBuilder.toString());

		for (int i = 0, j = 0; i < inputString.length(); i++) {
			for (; j < searchString.length(); j++) {
				if (Character.toLowerCase(searchString.charAt(j)) == Character.toLowerCase(inputString.charAt(i))) {
					if (i == inputString.length() - 1) {
						// 匹配成功
						return true;
					}
					j++;
					break;
				}
				if (j == searchString.length() - 1) {
					// 后面字符没找到
					return false;
				}
			}
		}
		return false;
	}
}
