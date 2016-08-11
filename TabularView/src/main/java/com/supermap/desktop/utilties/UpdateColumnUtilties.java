package com.supermap.desktop.utilties;

import com.supermap.data.FieldType;
import com.supermap.data.Geometry;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.FieldTypeUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class UpdateColumnUtilties {
	/**
	 * 判断运算符号中是否包含“Object”
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isObjectConnect(String method) {
		if (method.contains("Object")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断运算符是否为Math类提供的运算
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isMathInfo(String method) {
		if (method.equals("Abs") || method.equals("Sqrt") || method.equals("Ln") || method.equals("Log") || method.equals("Int")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断运算符是否为日期型运算
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isDaysInfo(String method) {
		if ("DaysInMonth".equals(method) || "Millisecond".equals(method) || "Second".equals(method) || "Minute".equals(method) || "Hour".equals(method)
				|| "Day".equals(method) || "Month".equals(method) || "Year".equals(method) || "DayOfYear".equals(method) || "DayOfWeek".equals(method)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断运算符是否为字符型运算符
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isTextInfo(String method) {
		if (method.equals("Left") || method.equals("Right") || method.equals("Mid") || method.equals("UCase") || method.equals("LCase")
				|| method.equals("TrimEnd") || method.equals("TrimStart") || method.equals("Trim") || method.equals("LRemove") || method.equals("RRemove")
				|| method.equals("Replace")) {
			return true;
		}
		return false;
	}

	public static boolean isIntegerType(FieldType fieldType) {
		if (fieldType.equals(FieldType.INT16) || fieldType.equals(FieldType.INT32) || fieldType.equals(FieldType.INT64)) {
			return true;
		}
		return false;
	}

	public static Object getUpdataModeMathValueText(String method, Object srcObject, String expression, String expression1) {
		String srcString = "";
		if (srcObject instanceof Date && null != srcObject) {
			srcString = Convert.resultFormat.format(srcObject);
		} else if (srcObject instanceof Boolean && null != srcObject) {
			if (srcObject.equals(true)) {
				srcString = "True";
			}else{
				srcString = "False";
			}
		} else if (null != srcObject) {
			srcString = srcObject.toString();
		}
		Object desValue = srcObject;
		try {
			int length = srcString.length();
			if ("Left".equals(method)) {
				desValue = srcString.substring(0, Convert.toInteger(expression) < length ? Convert.toInteger(expression) : length);
			} else if ("Right".equals(method)) {
				desValue = srcString.substring(length - Convert.toInteger(expression) >= 0 ? length - Convert.toInteger(expression) : 0);
			} else if ("Mid".equals(method)) {
				int startIndex = Convert.toInteger(expression) >= 0 ? Convert.toInteger(expression) : 0;
				startIndex = startIndex < length ? startIndex : length;

				int endIndex = Convert.toInteger(expression1) - startIndex;
				endIndex = endIndex < length - startIndex ? endIndex : length - startIndex;
				desValue = srcString.substring(startIndex, length);
			} else if ("UCase".equals(method)) {
				desValue = srcString.toUpperCase();
			} else if ("LCase".equals(method)) {
				desValue = srcString.toLowerCase();
			} else if ("TrimEnd".equals(method)) {
				for (int i = 0; i < expression.toCharArray().length; i++) {
					if (srcString.endsWith(expression.toCharArray()[i] + "")) {
						srcObject = srcString.substring(0, srcString.length() - 1);
						break;
					}
				}
				desValue = srcObject;
			} else if ("TrimStart".equals(method)) {
				for (int i = 0; i < expression.toCharArray().length; i++) {
					if (srcString.startsWith(expression.toCharArray()[i] + "")) {
						srcObject = srcString.substring(1, srcString.length());
						break;
					}
				}
				desValue = srcObject;
			} else if ("Trim".equals(method)) {
				desValue = srcString.trim();
			} else if ("LRemove".equals(method)) {
				desValue = srcString.substring(Convert.toInteger(expression) < length ? Convert.toInteger(expression) : length);
			} else if ("RRemove".equals(method)) {
				int subStringLength = length - Convert.toInteger(expression) > 0 ? length - Convert.toInteger(expression) : 0;
				desValue = srcString.substring(0, subStringLength);
			} else if ("Replace".equals(method)) {
				desValue = srcString.replace(expression, expression1);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return desValue;
	}

	public static Object getMathInfo(String method, String expression, String expression1, FieldType fieldType) {
		Object desValue = null;
		if (StringUtilities.isNullOrEmpty(expression)) {
			expression = "0";
		}
		if ("Abs".equals(method)) {
			desValue = Math.abs(Convert.toDouble(expression));
		} else if ("Sqrt".equals(method)) {
			desValue = Math.sqrt(Convert.toDouble(expression));
		} else if ("Ln".equals(method)) {
			desValue = Math.log(Convert.toDouble(expression));
		} else if ("Log".equals(method)) {
			desValue = Math.log(Convert.toDouble(expression)) / Math.log(Convert.toDouble(expression1));
		} else if ("Int".equals(method)) {
			desValue = Math.round(Convert.toDouble(expression));
		}
		if (isIntegerType(fieldType)) {
			desValue = Convert.toInteger(desValue.toString());
		}
		if(fieldType.equals(FieldType.BYTE)){
			if (StringUtilities.isNullOrEmptyString(desValue.toString())) {
				desValue = (byte) 0;
			} else if (Convert.toInteger(desValue.toString()) < 128 && Convert.toInteger(desValue.toString()) >= 0) {
				desValue = (byte) Convert.toInteger(desValue.toString());
			} else if (Convert.toInteger(desValue.toString()) >= 128 || Convert.toInteger(desValue.toString()) < 0) {
				desValue = (byte) 0;
			}
		}
		return desValue;
	}

	public static Object getObjectInfo(String method, Geometry geometry, FieldType fieldType) {
		Object desValue = null;
		if ("ObjectCenterX".equals(method)) {
			desValue = geometry.getInnerPoint().getX();
		} else if ("ObjectCenterY".equals(method)) {
			desValue = geometry.getInnerPoint().getY();
		} else if ("ObjectLeft".equals(method)) {
			desValue = geometry.getBounds().getLeft();
		} else if ("ObjectRight".equals(method)) {
			desValue = geometry.getBounds().getRight();
		} else if ("ObjectTop".equals(method)) {
			desValue = geometry.getBounds().getTop();
		} else if ("ObjectBottom".equals(method)) {
			desValue = geometry.getBounds().getBottom();
		} else if ("ObjectWidth".equals(method)) {
			desValue = geometry.getBounds().getWidth();
		} else if ("ObjectHeight".equals(method)) {
			desValue = geometry.getBounds().getHeight();
		}
		if (isIntegerType(fieldType)) {
			desValue = Convert.toInteger(desValue.toString());
		} else if (FieldTypeUtilities.isString(fieldType) || fieldType.equals(FieldType.CHAR)) {
			desValue = desValue.toString();
		}else if(fieldType.equals(FieldType.BYTE)){
			if (StringUtilities.isNullOrEmptyString(desValue.toString())) {
				desValue = (byte) 0;
			} else if (Convert.toInteger(desValue.toString()) < 128 && Convert.toInteger(desValue.toString()) >= 0) {
				desValue = (byte) Convert.toInteger(desValue.toString());
			} else if (Convert.toInteger(desValue.toString()) >= 128 || Convert.toInteger(desValue.toString()) < 0) {
				desValue = (byte) 0;
			}
		}
		return desValue;
	}

	public static Date getUpdataModeMathValueDataTime(Object srcValue, Object updateField, String method, String expression) {
		Date desValue = null;
		try {
			if (null == Convert.toDateTime(updateField)) {
				return new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse("1899/12/30 00:00:00");
			} else if (null == Convert.toDateTime(srcValue)) {
				return desValue;
			}
			GregorianCalendar ca = new GregorianCalendar();
			ca.setTime(Convert.toDateTime(srcValue));
			if ("AddDays".equals(method)) {
				ca.add(GregorianCalendar.DAY_OF_MONTH, Convert.toInteger(expression));
				desValue = ca.getTime();
			} else if ("AddHours".equals(method)) {
				ca.add(GregorianCalendar.HOUR_OF_DAY, Convert.toInteger(expression));
				desValue = ca.getTime();
			} else if ("AddMilliseconds".equals(method)) {
				ca.add(GregorianCalendar.MILLISECOND, Convert.toInteger(expression));
				desValue = ca.getTime();
			} else if ("AddSeconds".equals(method)) {
				ca.add(GregorianCalendar.SECOND, Convert.toInteger(expression));
				desValue = ca.getTime();
			} else if ("AddMinutes".equals(method)) {
				ca.add(GregorianCalendar.MINUTE, Convert.toInteger(expression));
				desValue = ca.getTime();
			} else if ("AddMonths".equals(method)) {
				ca.add(GregorianCalendar.MONTH, Convert.toInteger(expression));
				desValue = ca.getTime();
			} else if ("AddYears".equals(method)) {
				ca.add(GregorianCalendar.YEAR, Convert.toInteger(expression));
				desValue = ca.getTime();
			} else if ("Date".equals(method)) {
				desValue = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse("1899/12/30 00:00:00");
			} else if ("Now".equals(method)) {
				desValue = new Date();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return desValue;
	}

	public static Object getDateInfo(String method, Date date) {
		if (null == date) {
			return 0;
		}
		GregorianCalendar ca = new GregorianCalendar();
		ca.setTime(date);
		Object desValue = null;
		if ("DaysInMonth".equals(method)) {
			desValue = ca.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		} else if ("Millisecond".equals(method)) {
			desValue = ca.get(GregorianCalendar.MILLISECOND);
		} else if ("Second".equals(method)) {
			desValue = ca.get(GregorianCalendar.SECOND);
		} else if ("Minute".equals(method)) {
			desValue = ca.get(GregorianCalendar.MINUTE);
		} else if ("Hour".equals(method)) {
			desValue = ca.get(GregorianCalendar.HOUR);
		} else if ("Day".equals(method)) {
			desValue = ca.get(GregorianCalendar.DAY_OF_MONTH);
		} else if ("Month".equals(method)) {
			desValue = ca.get(GregorianCalendar.MONTH) + 1;
		} else if ("Year".equals(method)) {
			desValue = ca.get(GregorianCalendar.YEAR);
		} else if ("DayOfYear".equals(method)) {
			desValue = ca.get(GregorianCalendar.DAY_OF_YEAR);
		} else if ("DayOfWeek".equals(method)) {
			desValue = ca.get(GregorianCalendar.DAY_OF_WEEK);
		}
		return desValue;
	}

	public static Object getCommonMethodInfo(String method, Object value, Object value1, FieldType fieldType) {
		Double fieldValue = 0.0;
		try {
			fieldValue = Convert.toDouble(value);
		} catch (Exception e) {
			fieldValue = 0.0;
		}
		Double param = 0.0;
		try {
			param = Convert.toDouble(value1);
		} catch (Exception e) {
			param = 0.0;
		}
		Object desValue = null;
		if ("+".equals(method)) {
			desValue = fieldValue + param;
		} else if ("-".equals(method)) {
			desValue = fieldValue - param;
		} else if ("*".equals(method)) {
			desValue = fieldValue * param;
		} else if ("/".equals(method)) {
			desValue = fieldValue / param;
		} else if ("%".equals(method)) {
			desValue = fieldValue % param;
		} else if (">".equals(method)) {
			desValue = fieldValue > param;
		} else if (">=".equals(method)) {
			desValue = fieldValue >= param;
		} else if ("<".equals(method)) {
			desValue = fieldValue < param;
		} else if ("<=".equals(method)) {
			desValue = fieldValue <= param;
		} else if ("==".equals(method)) {
			desValue = (fieldValue - param == 0);
		} else if ("!=".equals(method)) {
			desValue = (fieldValue - param != 0);
		}
		if (isIntegerType(fieldType)) {
			desValue = Convert.toInteger(desValue.toString());
		} else if (fieldType.equals(FieldType.BOOLEAN)) {
			if (desValue instanceof Boolean) {
				return desValue;
			}
			desValue = (desValue instanceof Double && Double.compare((double) desValue, 0.0) > 0) ? true : false;
		} else if (fieldType.equals(FieldType.BYTE)) {
			if (StringUtilities.isNullOrEmptyString(desValue.toString())) {
				desValue = (byte) 0;
			} else if (Convert.toInteger(desValue.toString()) < 128 && Convert.toInteger(desValue.toString()) >= 0) {
				desValue = (byte) Convert.toInteger(desValue.toString());
			} else if (Convert.toInteger(desValue.toString()) >= 128 || Convert.toInteger(desValue.toString()) < 0) {
				desValue = (byte) 0;
			}
		}
		return desValue;
	}

}
