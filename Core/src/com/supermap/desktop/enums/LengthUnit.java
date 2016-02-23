package com.supermap.desktop.enums;

import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Unit;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;

public enum LengthUnit {
	// @formatter:off
	MILLIMETER(UnitValue.MILLIMETER, Unit.MILIMETER), 
	CENTIMETER(UnitValue.CENTIMETER, Unit.CENTIMETER), 
	DECIMETER(UnitValue.DECIMETER, Unit.DECIMETER), 
	METER(UnitValue.METER, Unit.METER), 
	KILOMETER(UnitValue.KILOMETER, Unit.KILOMETER), 
	MILE(UnitValue.MILE, Unit.MILE), 
	INCH(UnitValue.INCH, Unit.INCH), 
	FOOT(UnitValue.FOOT, Unit.FOOT), 
	YARD(UnitValue.YARD, Unit.YARD), 
	SECOND(UnitValue.SECOND, Unit.SECOND), 
	MINUTE(UnitValue.MINUTE, Unit.MINUTE), 
	DEGREE(UnitValue.DEGREE, Unit.DEGREE), 
	RADIAN(UnitValue.RADIAN, Unit.RADIAN);
	// @formatter:on

	private int value = 0;
	private transient Unit unit = Unit.METER;

	LengthUnit(int value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}

	public Unit getUnit() {
		return this.unit;
	}

	public static LengthUnit convertForm(Unit unit) {
		LengthUnit result = LengthUnit.METER;

		LengthUnit[] lengthUnits = LengthUnit.values();
		for (LengthUnit lengthUnit : lengthUnits) {
			if (lengthUnit.unit == unit) {
				result = lengthUnit;
				break;
			}
		}
		return result;
	}


	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		if (this == METER) {
			return CommonProperties.getString("String_DistanceUnit_Meter");
		} else if (this == KILOMETER) {
			return CommonProperties.getString("String_DistanceUnit_Kilometer");
		} else if (this == CENTIMETER) {
			return CommonProperties.getString("String_DistanceUnit_Centimeter");
		} else if (this == DECIMETER) {
			return CommonProperties.getString("String_DistanceUnit_Decimeter");
		} else if (this == MILE) {
			return CommonProperties.getString("String_DistanceUnit_Mile");
		} else if (this == INCH) {
			return CommonProperties.getString("String_DistanceUnit_Inch");
		} else if (this == YARD) {
			return CommonProperties.getString("String_DistanceUnit_Yard");
		} else if (this == SECOND) {
			return CommonProperties.getString("String_AngleUnit_Second");
		} else if (this == MINUTE) {
			return CommonProperties.getString("String_AngleUnit_Minute");
		} else if (this == DEGREE) {
			return CommonProperties.getString("String_AngleUnit_Degree");
		} else if (this == FOOT) {
			return CommonProperties.getString("String_DistanceUnit_Foot");
		} else if (this == MILLIMETER) {
			return CommonProperties.getString("String_DistanceUnit_Millimeter");
		}
		return this.unit.toString();
	}

	public static LengthUnit getValueOf(String name) {
		if (name.equals(CommonProperties.getString("String_DistanceUnit_Meter"))) {
			return METER;
		} else if (name.equals(CommonProperties.getString("String_DistanceUnit_Kilometer"))) {
			return KILOMETER;
		} else if (name.equals(CommonProperties.getString("String_DistanceUnit_Centimeter"))) {
			return CENTIMETER;
		} else if (name.equals(CommonProperties.getString("String_DistanceUnit_Decimeter"))) {
			return DECIMETER;
		} else if (name.equals(CommonProperties.getString("String_DistanceUnit_Mile"))) {
			return MILE;
		} else if (name.equals(CommonProperties.getString("String_DistanceUnit_Inch"))) {
			return INCH;
		} else if (name.equals(CommonProperties.getString("String_DistanceUnit_Yard"))) {
			return YARD;
		} else if (name.equals(CommonProperties.getString("String_AngleUnit_Second"))) {
			return SECOND;
		} else if (name.equals(CommonProperties.getString("String_AngleUnit_Minute"))) {
			return MINUTE;
		} else if (name.equals(CommonProperties.getString("String_AngleUnit_Degree"))) {
			return DEGREE;
		} else if (name.equals(CommonProperties.getString("String_DistanceUnit_Foot"))) {
			return FOOT;
		} else if (name.equals(CommonProperties.getString("String_DistanceUnit_Millimeter"))) {
			return MILLIMETER;
		}
		return RADIAN;
	}

	/**
	 * 距离转换
	 *
	 * @param currentUnit 转换后单位
	 * @param beforeUnit  转换前单位
	 * @param distance    当前距离
	 * @return 转换后距离
	 */
	public static double ConvertDistance(Unit beforeUnit, Unit currentUnit,
	                                     double distance) {
		double resultDistance = distance;
		try {
			if (currentUnit != beforeUnit) {
				resultDistance = distance * ((double) beforeUnit.value() / (double) currentUnit.value());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return resultDistance;
	}

	/**
	 * 以投影系统的坐标单位作为参照，进行距离单位换算，平面坐标系使用坐标单位，其他坐标系单位默认为米
	 *
	 * @param prjCoordSys
	 * @param desUnit
	 * @param distance
	 * @return
	 */
	public static double ConvertDistance(PrjCoordSys prjCoordSys, Unit
			desUnit, double distance) {
		double resultDistance = distance;
		try {
			Unit unit = Unit.METER;
			if (prjCoordSys.getType() == PrjCoordSysType.PCS_NON_EARTH) {
				unit = prjCoordSys.getCoordUnit();
			}

			resultDistance = ConvertDistance(unit, desUnit, distance);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return resultDistance;
	}
}