package com.supermap.desktop.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/9/6.
 */
public class Time {
	private Map<TimeType, Integer> timeMap = new ConcurrentHashMap<>();

	public Time(long time, TimeType type) {
		initTimeMap();
		convertTime(time, type);
	}

	private void initTimeMap() {
		TimeType[] types = TimeType.getTypes();
		for (int i = 0; i < types.length; i++) {
			this.timeMap.put(types[i], 0);
		}
	}

	private void convertTime(long time, TimeType type) {
		if (time < type.getMin()) {
			throw new IllegalArgumentException();
		}

		if (time <= type.getMax() || type == TimeType.getMaxType()) {
			this.timeMap.put(type, (int) time);
		} else {
			long current = time % type.getSpan();
			this.timeMap.put(type, (int) current);

			long next = time / type.getSpan();
			TimeType nextType = TimeType.valueOf(type.getValue() + 1);
			convertTime(next, nextType);
		}
	}

	public int getMilliSeconds() {
		return this.timeMap.get(TimeType.MILLISECOND);
	}

	public int getSeconds() {
		return this.timeMap.get(TimeType.SECOND);
	}

	public int getMinutes() {
		return this.timeMap.get(TimeType.MINUTE);
	}

	public int getHours() {
		return this.timeMap.get(TimeType.HOUR);
	}

	public int getDays() {
		return this.timeMap.get(TimeType.DAY);
	}

	public String toString() {
		String s = "";

		TimeType[] timeTypes = TimeType.getTypes();
		for (int i = timeTypes.length - 1; i >= 0; i--) {
			int time = this.timeMap.get(timeTypes[i]);
			if (time == 0) {
				continue;
			}

			s += time + timeTypes[i].getText();
		}
		return s;
	}

	public static String toString(long timeValue, TimeType type) {
		Time time = new Time(timeValue, type);
		return time.toString();
	}

	public static void main(String[] args) {
		Time time = new Time(2468, TimeType.MILLISECOND);
		System.out.println(time.toString());
	}
}
