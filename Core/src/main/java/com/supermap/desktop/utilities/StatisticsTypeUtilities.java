package com.supermap.desktop.utilities;

import com.supermap.analyst.spatialstatistics.StatisticsType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created by hanyz on 2017/5/4.
 */
public class StatisticsTypeUtilities {
	private StatisticsTypeUtilities() {
	}

	public static String getStatisticsTypeName(StatisticsType type) {
		String name = "";
		try {
			if (type == StatisticsType.MAX) {
				name = CommonProperties.getString("String_StatisticsType_MAX");
			} else if (type == StatisticsType.MIN) {
				name = CommonProperties.getString("String_StatisticsType_MIN");
			} else if (type == StatisticsType.MEAN) {
				name = CommonProperties.getString("String_StatisticsType_MEAN");
			} else if (type == StatisticsType.MEDIAN) {
				name = CommonProperties.getString("String_StatisticsType_MEDIAN");
			} else if (type == StatisticsType.SUM) {
				name = CommonProperties.getString("String_StatisticsType_SUM");
			} else if (type == StatisticsType.FIRST) {
				name = CommonProperties.getString("String_StatisticsType_FIRST");
			} else if (type == StatisticsType.LAST) {
				name = CommonProperties.getString("String_StatisticsType_LAST");
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return name;
	}

	public static StatisticsType getStatisticsType(String statisticsTypeName) {
		StatisticsType type = StatisticsType.FIRST;
		try {
			if (statisticsTypeName.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_MAX"))) {
				type = StatisticsType.MAX;
			} else if (statisticsTypeName.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_MIN"))) {
				type = StatisticsType.MIN;
			} else if (statisticsTypeName.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_MEAN"))) {
				type = StatisticsType.MEAN;
			} else if (statisticsTypeName.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_MEDIAN"))) {
				type = StatisticsType.MEDIAN;
			} else if (statisticsTypeName.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_SUM"))) {
				type = StatisticsType.SUM;
			} else if (statisticsTypeName.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_FIRST"))) {
				type = StatisticsType.FIRST;
			} else if (statisticsTypeName.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_LAST"))) {
				type = StatisticsType.LAST;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return type;
	}
}

