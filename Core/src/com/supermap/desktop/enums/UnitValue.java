package com.supermap.desktop.enums;

import com.supermap.data.Unit;
import com.supermap.desktop.properties.CommonProperties;

public class UnitValue {

	public static final int MILLIMETER = 10; // millimeter，毫米
	public static final int CENTIMETER = 100; // centimeter，厘米
	public static final int DECIMETER = 1000; // decimeter，分米
	public static final int METER = 10000; // meter，米
	public static final int KILOMETER = 10000000; // kilometer，千米
	public static final int MILE = 16090000; // mile，英里
	public static final int INCH = 254; // inch，英寸
	public static final int FOOT = 3048; // foot，英尺
	public static final int YARD = 9144; // yard，码
	public static final int SECOND = 1000000485; // second，秒
	public static final int MINUTE = 1000029089; // minute，分
	public static final int DEGREE = 1001745329; // degree，度
	public static final int RADIAN = 1100000000; // radian，弧度

	private UnitValue() {
		// 默认实现
	}
	public static String parseToString(Unit unit){
		String result = "";
		if (unit.equals(Unit.CENTIMETER)) {
			result=CommonProperties.getString("String_DistanceUnit_Centimeter");
		}
		if (unit.equals(Unit.DECIMETER)) {
			result=CommonProperties.getString("String_DistanceUnit_Decimeter");
		}
		if (unit.equals(Unit.DEGREE)) {
			result=CommonProperties.getString("String_AngleUnit_Degree");
		}
		if (unit.equals(Unit.FOOT)) {
			result=CommonProperties.getString("String_DistanceUnit_Foot");
		}
		if (unit.equals(Unit.INCH)) {
			result=CommonProperties.getString("String_DistanceUnit_Inch");
		}
		if (unit.equals(Unit.KILOMETER)) {
			result=CommonProperties.getString("String_DistanceUnit_Kilometer");
		}
		if (unit.equals(Unit.METER)) {
			result=CommonProperties.getString("String_DistanceUnit_Meter");
		}
		if (unit.equals(Unit.MILE)) {
			result=CommonProperties.getString("String_DistanceUnit_Mile");
		}
		if (unit.equals(Unit.MILIMETER)) {
			result=CommonProperties.getString("String_DistanceUnit_Millimeter");
		}
		if (unit.equals(Unit.MINUTE)) {
			result=CommonProperties.getString("String_AngleUnit_Minute");
		}
		if (unit.equals(Unit.RADIAN)) {
			result=CommonProperties.getString("String_AngleUnit_Radian");
		}
		if (unit.equals(Unit.SECOND)) {
			result=CommonProperties.getString("String_AngleUnit_Second");
		}
		if (unit.equals(Unit.YARD)) {
			result=CommonProperties.getString("String_DistanceUnit_Yard");
		}
		return result;
	}
}
