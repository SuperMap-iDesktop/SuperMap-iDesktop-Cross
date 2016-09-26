package com.supermap.desktop.utilities;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoreProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 数据转换类
 *
 * @author xie
 */
public class Convert {
    public static SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.US);

    public static String getDateStr(String str) {
        String success = "";
        if (!StringUtilities.isNullOrEmpty(str)) {
            success = getDateInfo(str, success);
        }
        return success;
    }

    private static String getDateInfo(String str, String success) {
        SimpleDateFormat format;
        String temp = ".";
        if (str.contains("-")) {
            str = str.replaceAll("-", ".");
        } else if (str.contains("/")) {
            str = str.replaceAll("/", ".");
        }
        String[] dateStr = str.split("\\" + temp);
        // 串格式为yyyy.MM.dd类型格式的字符串
        if (dateStr.length == 1 && dateStr[0].length() == 4) {
            format = new SimpleDateFormat("yyyy");
            format.setLenient(false);
            success = getResultStr(str, success, format);
        } else if (dateStr.length == 2 && dateStr[0].length() == 4) {
            format = new SimpleDateFormat("yyyy" + temp + "MM");
            format.setLenient(false);
            success = getResultStr(str, success, format);
        } else if (dateStr.length == 3 && dateStr[0].length() == 4 && !dateStr[2].contains(":")) {
            format = new SimpleDateFormat("yyyy" + temp + "MM" + temp + "dd");
            format.setLenient(false);
            success = getResultStr(str, success, format);
        } else if (dateStr.length == 3 && dateStr[0].length() == 4 && dateStr[2].contains(":")) {
            String[] hourStr = dateStr[2].split(":");
            if (hourStr.length == 2) {
                format = new SimpleDateFormat("yyyy" + temp + "MM" + temp + "dd hh:mm");
                format.setLenient(false);
                success = getResultStr(str, success, format);
            } else if (hourStr.length == 3) {
                format = new SimpleDateFormat("yyyy" + temp + "MM" + temp + "dd hh:mm:ss");
                format.setLenient(false);
                success = getResultStr(str, success, format);
            }
        }
        // 格式为mm.dd类型格式的字符串
        if (dateStr.length == 2 && Integer.valueOf(dateStr[0]) <= 12 && Integer.valueOf(dateStr[1]) <= 31) {
            Calendar calendar = Calendar.getInstance();
            format = new SimpleDateFormat("yyyy" + temp + "MM" + temp + "dd");
            format.setLenient(false);
            success = getResultStr(calendar.get(calendar.YEAR) + "." + str, success, format);
        }
        return success;
    }

    private static String getResultStr(String str, String success, SimpleDateFormat format) {
        try {
            if ("null".equals(str)) {
                return "null";
            } else {
                Date date = format.parse(str);
                success = resultFormat.format(date);
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_DateError"));
        }
        return success;
    }

    public static Date toDateTime(Object srcValue) {
        Date result = null;
        try {
            if (null == srcValue) {
                result = new Date();
            } else if (srcValue instanceof Date) {
                result = (Date) srcValue;
            } else if ("null".equals(srcValue.toString())) {
                result = null;
            } else if (!StringUtilities.isNullOrEmptyString(srcValue) && !StringUtilities.isNumber(getDateStr(srcValue.toString()))
                    && !"".equals(getDateStr(srcValue.toString()))) {
                result = resultFormat.parse(getDateStr(srcValue.toString()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static double toDouble(Object expression) {
        if (!StringUtilities.isNullOrEmptyString(expression) && StringUtilities.isNumber(expression.toString())) {
            return Double.parseDouble(expression.toString());
        }
        return 0.0;
    }

    public static int toInteger(Object expression) {
        if (!StringUtilities.isNullOrEmptyString(expression) && StringUtilities.isNumeric(expression.toString()) && expression.toString().length() < 10) {
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
}
