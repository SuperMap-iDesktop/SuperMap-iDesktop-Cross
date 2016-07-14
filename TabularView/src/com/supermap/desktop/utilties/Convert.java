package com.supermap.desktop.utilties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.supermap.data.Recordset;
import com.supermap.desktop.utilities.StringUtilities;

public class Convert {
	public static boolean isValidDate(String str) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

	public static Date toDateTime(String srcValue) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date result = null;
		if (isValidDate(srcValue)) {
			try {
				result = format.parse(srcValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public static double toDouble(Object expression) {
		return Double.parseDouble(expression.toString());
	}

	public static int toInteger(Object expression) {
		if (!StringUtilities.isNullOrEmptyString(expression) && StringUtilities.isNumeric(expression.toString())) {
			return Integer.parseInt(expression.toString());
		}
		if (!StringUtilities.isNullOrEmptyString(expression) && expression.toString().contains(".")) {
			double temp = Math.round(Double.parseDouble(expression.toString()));
			String tempStr = String.valueOf(temp);
			expression = tempStr.substring(0, tempStr.indexOf("."));
			return Integer.parseInt(expression.toString());
		}
		return 0;
	}

	public static boolean toBoolean(Object expression) {
		if (!StringUtilities.isNullOrEmptyString(expression) && !StringUtilities.isNumber(expression.toString())) {
			if (expression.toString().equalsIgnoreCase("true")) {
				return true;
			}
		}
		if (!StringUtilities.isNullOrEmptyString(expression) && StringUtilities.isNumber(expression.toString())) {
			return Double.compare(toDouble(expression), 0.0) >= 0 ? true : false;
		}
		return false;
	}
	public static void main(String[] args) {
		System.out.println(toDateTime("2015.1.1"));
	}
}
