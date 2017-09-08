package com.supermap.desktop.core;

import com.supermap.desktop.properties.CoreProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/9/6.
 */
public class Time {
	private Map<TimeType, Integer> timeMap = new ConcurrentHashMap<>();

	public Time(long time, TimeType type) {
		convertTime(time, type);
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
		return this.timeMap.containsKey(TimeType.MILLISECOND) ? this.timeMap.get(TimeType.MILLISECOND) : 0;
	}

	public int getSeconds() {
		return this.timeMap.containsKey(TimeType.SECOND) ? this.timeMap.get(TimeType.SECOND) : 0;
	}

	public int getMinutes() {
		return this.timeMap.containsKey(TimeType.MINUTE) ? this.timeMap.get(TimeType.MINUTE) : 0;
	}

	public int getHours() {
		return this.timeMap.containsKey(TimeType.HOUR) ? this.timeMap.get(TimeType.HOUR) : 0;
	}

	public int getDays() {
		return this.timeMap.containsKey(TimeType.DAY) ? this.timeMap.get(TimeType.DAY) : 0;
	}

	public String toString() {
		if (this.timeMap.size() == 0) {

			// 无值，输出0
			return "0" + CoreProperties.getString("String_Second");
		}

		if (this.timeMap.size() == 1) {
			Map.Entry<TimeType, Integer> entry = this.timeMap.entrySet().iterator().next();
			return getSingleTimeText(entry.getValue(), entry.getKey());
		}

		String s = "";
		TimeType[] allTypes = TimeType.getTypes();

		// 2个及以上单位有值，则忽略毫秒级数值
		for (int i = allTypes.length - 1; i >= 1; i--) {
			if (this.timeMap.containsKey(allTypes[i])) {
				s += getSingleTimeText(this.timeMap.get(allTypes[i]), allTypes[i]) + " ";
			}
		}
		return s;
	}

	private String getSingleTimeText(int timeValue, TimeType type) {
		if (type != TimeType.MILLISECOND) {

			// 非毫秒，则直接输出
			return timeValue + type.getText();
		} else if (timeValue > 100) {

			// 大于100毫秒，则转换成一位小数以秒的形式输出
			return String.format("%.1f", timeValue / 1000d) + CoreProperties.getString("String_Second");
		} else {

			// 不大于100毫秒，则输出0
			return "0" + CoreProperties.getString("String_Second");
		}
	}

	public static String toString(long timeValue, TimeType type) {
		Time time = new Time(timeValue, type);
		return time.toString();
	}

	public static void main(String[] args) {
		Time time = new Time(288888888, TimeType.MILLISECOND);
		System.out.println(time.toString());
	}
}
