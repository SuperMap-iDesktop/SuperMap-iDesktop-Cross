package com.supermap.desktop.core;

import com.supermap.desktop.properties.CoreProperties;

/**
 * Created by highsad on 2017/9/6.
 */
public enum TimeType {
	MILLISECOND(1, 0, 999, CoreProperties.getString("String_MilliSecond")),
	SECOND(2, 0, 59, CoreProperties.getString("String_Second")),
	MINUTE(3, 0, 59, CoreProperties.getString("String_Minute")),
	HOUR(4, 0, 23, CoreProperties.getString("String_Hour")),
	DAY(5, 0, Integer.MAX_VALUE, CoreProperties.getString("String_Day"));

	private int value;
	private int min;
	private int max;
	private String text;

	TimeType(int value, int min, int max, String text) {
		this.value = value;
		this.min = min;
		this.max = max;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public String getText() {
		return text;
	}

	public int getSpan() {
		return this.max - this.min + 1;
	}

	public static TimeType valueOf(int value) {
		switch (value) {
			case 1:
				return TimeType.MILLISECOND;
			case 2:
				return TimeType.SECOND;
			case 3:
				return TimeType.MINUTE;
			case 4:
				return TimeType.HOUR;
			case 5:
				return TimeType.DAY;
			default:
				return TimeType.MILLISECOND;
		}
	}

	public static TimeType getMaxType() {
		return TimeType.DAY;
	}

	/**
	 * ascending order
	 *
	 * @return
	 */
	public static TimeType[] getTypes() {
		return new TimeType[]{TimeType.MILLISECOND, TimeType.SECOND, TimeType.MINUTE, TimeType.HOUR, TimeType.DAY};
	}
}
