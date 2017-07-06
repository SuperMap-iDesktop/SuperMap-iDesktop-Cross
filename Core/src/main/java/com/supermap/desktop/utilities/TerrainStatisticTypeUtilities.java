package com.supermap.desktop.utilities;

import com.supermap.analyst.spatialanalyst.TerrainStatisticType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class TerrainStatisticTypeUtilities {
    private TerrainStatisticTypeUtilities() {
    }

    public static String toString(TerrainStatisticType type) {
        String text = "";
        try {
            if (type == TerrainStatisticType.UNIQUE) {
                text = CommonProperties.getString("String_TerrainStatisticType_Unique");
            } else if (type == TerrainStatisticType.MAX) {
                text = CommonProperties.getString("String_StatisticsType_MAX");
            } else if (type == TerrainStatisticType.MIN) {
                text = CommonProperties.getString("String_StatisticsType_MIN");
            } else if (type == TerrainStatisticType.MAJORITY) {
                text = CommonProperties.getString("String_TerrainStatisticType_Majority");
            } else if (type == TerrainStatisticType.MEAN) {
                text = CommonProperties.getString("String_StatisticsType_MEAN");
            } else if (type == TerrainStatisticType.MEDIAN) {
                text = CommonProperties.getString("String_StatisticsType_MEDIAN");
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        return text;
    }

    public static TerrainStatisticType valueOf(String text) {
        TerrainStatisticType type = null;
        try {
            if (text.equalsIgnoreCase(CommonProperties.getString("String_TerrainStatisticType_Unique"))) {
                type = TerrainStatisticType.UNIQUE;
            } else if (text.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_MAX"))) {
                type = TerrainStatisticType.MAX;
            } else if (text.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_MIN"))) {
                type = TerrainStatisticType.MIN;
            } else if (text.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_MEAN"))) {
                type = TerrainStatisticType.MEAN;
            } else if (text.equalsIgnoreCase(CommonProperties.getString("String_StatisticsType_MEDIAN"))) {
                type = TerrainStatisticType.MEDIAN;
            } else if (text.equalsIgnoreCase(CommonProperties.getString("String_TerrainStatisticType_Unique"))) {
                type = TerrainStatisticType.UNIQUE;
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        return type;
    }
}
