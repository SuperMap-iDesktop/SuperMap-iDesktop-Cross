package com.supermap.desktop.enums;

import com.supermap.desktop.properties.CoreProperties;

import java.text.MessageFormat;

/**
 * Created by Administrator on 2016/2/24.
 */
public enum AngleUnit {
	// @formatter:off
	DEGREE,//度
	DEGREE_MINUTE_SECOND,//度分秒
	RADIAN;//弧度
	// formatter:on
	public static String getAngleInfo(AngleUnit angleUnit,double value) {
		String result = "";
		switch (angleUnit) {
			case DEGREE:
				result = MessageFormat.format(CoreProperties.getString("String_Degree_Degree"), value);
				break;
			case DEGREE_MINUTE_SECOND:
				double copyValue = value;
				int dd = (int) (copyValue);
				copyValue = (Math.abs(copyValue)% 1) * 60;
				int mm = (int) copyValue;
				copyValue = (Math.abs(copyValue) % 1) * 60;
				int ss = ((int) copyValue);
				result = MessageFormat.format(CoreProperties.getString("String_Degree_DDMMSS"),dd,mm,ss);
				break;
			case RADIAN:
				result = MessageFormat.format(CoreProperties.getString("String_Degree_Radian"), Math.toRadians(value));
				break;
		}
		return result;
	}

	public static AngleUnit getvalueOf(String s) {
		if(s.equals(CoreProperties.getString("String_Degree_Format_Degree"))){
			return AngleUnit.DEGREE;
		}else if(s.equals(CoreProperties.getString("String_Degree_Format_DDMMSS"))){
			return AngleUnit.DEGREE_MINUTE_SECOND;
		} else if(s.equals(CoreProperties.getString("String_Degree_Format_Radian"))) {
			return AngleUnit.RADIAN;
		}
		return AngleUnit.DEGREE;
	}

	public String toString() {
		if (this == AngleUnit.DEGREE) {
			return CoreProperties.getString("String_Degree_Format_Degree");
		} else if (this == AngleUnit.DEGREE_MINUTE_SECOND) {
			return CoreProperties.getString("String_Degree_Format_DDMMSS");
		} else if (this == AngleUnit.RADIAN) {
			return CoreProperties.getString("String_Degree_Format_Radian");
		}
		return CoreProperties.getString("String_Degree_Format_Degree");
	}
}
