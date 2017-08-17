package com.supermap.desktop.utilities;

import com.supermap.analyst.spatialanalyst.GridStatisticsMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created By Chens on 2017/8/15 0015
 */
public class GridStatisticsModeUtilities {
	public GridStatisticsModeUtilities() {
	}

	public static String getGridStatisticsModeName(GridStatisticsMode mode) {
		String name = "";
		try {
			if (mode == GridStatisticsMode.MAX) {
				name = CommonProperties.getString("String_GridStatisticsMode_Max");
			} else if (mode == GridStatisticsMode.MIN) {
				name = CommonProperties.getString("String_GridStatisticsMode_Min");
			} else if (mode == GridStatisticsMode.MEAN) {
				name = CommonProperties.getString("String_GridStatisticsMode_Mean");
			} else if (mode == GridStatisticsMode.MEDIAN) {
				name = CommonProperties.getString("String_GridStatisticsMode_Median");
			} else if (mode == GridStatisticsMode.SUM) {
				name = CommonProperties.getString("String_GridStatisticsMode_Sum");
			} else if (mode == GridStatisticsMode.MAJORITY) {
				name = CommonProperties.getString("String_GridStatisticsMode_Majority");
			} else if (mode == GridStatisticsMode.MINORITY) {
				name = CommonProperties.getString("String_GridStatisticsMode_Minority");
			} else if (mode == GridStatisticsMode.VARIETY) {
				name = CommonProperties.getString("String_GridStatisticsMode_Variety");
			} else if (mode == GridStatisticsMode.STDEV) {
				name = CommonProperties.getString("String_GridStatisticsMode_Stdev");
			} else if (mode == GridStatisticsMode.RANGE) {
				name = CommonProperties.getString("String_GridStatisticsMode_Range");
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return name;
	}

	public static GridStatisticsMode getGridStatisticsMode(String name) {
		GridStatisticsMode type = GridStatisticsMode.MAX;
		try {
			if (name.equalsIgnoreCase(CommonProperties.getString("String_GridStatisticsMode_Max"))) {
				type = GridStatisticsMode.MAX;
			} else if (name.equalsIgnoreCase(CommonProperties.getString("String_GridStatisticsMode_Min"))) {
				type = GridStatisticsMode.MIN;
			} else if (name.equalsIgnoreCase(CommonProperties.getString("String_GridStatisticsMode_Mean"))) {
				type = GridStatisticsMode.MEAN;
			} else if (name.equalsIgnoreCase(CommonProperties.getString("String_GridStatisticsMode_Median"))) {
				type = GridStatisticsMode.MEDIAN;
			} else if (name.equalsIgnoreCase(CommonProperties.getString("String_GridStatisticsMode_Sum"))) {
				type = GridStatisticsMode.SUM;
			} else if (name.equalsIgnoreCase(CommonProperties.getString("String_GridStatisticsMode_Majority"))) {
				type = GridStatisticsMode.MAJORITY;
			} else if (name.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_Minority"))) {
				type = GridStatisticsMode.MINORITY;
			} else if (name.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_Variety"))) {
				type = GridStatisticsMode.VARIETY;
			} else if (name.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_Stdev"))) {
				type = GridStatisticsMode.STDEV;
			} else if (name.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_Range"))) {
				type = GridStatisticsMode.RANGE;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return type;
	}
}
