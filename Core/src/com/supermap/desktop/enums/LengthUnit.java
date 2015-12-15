package com.supermap.desktop.enums;

import com.supermap.data.Unit;

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

	private LengthUnit(int value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}

	public static LengthUnit convertForm(Unit unit) {
		LengthUnit result = LengthUnit.METER;

		LengthUnit[] lengthUnits = LengthUnit.values();
		for (int i = 0; i < lengthUnits.length; i++) {
			if (lengthUnits[i].unit == unit) {
				result = lengthUnits[i];
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
		return this.unit.toString();
	}
}