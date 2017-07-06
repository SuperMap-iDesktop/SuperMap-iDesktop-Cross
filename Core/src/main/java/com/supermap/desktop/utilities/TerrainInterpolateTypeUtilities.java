package com.supermap.desktop.utilities;

import com.supermap.analyst.spatialanalyst.TerrainInterpolateType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class TerrainInterpolateTypeUtilities {

    private TerrainInterpolateTypeUtilities() {
    }

    public static String toString(TerrainInterpolateType type) {
        String result = "";
        try {
            if (type == TerrainInterpolateType.IDW) {
                result = CommonProperties.getString("String_TerrainInterpolateType_IDW");
            } else if (type == TerrainInterpolateType.KRIGING) {
                result = CommonProperties.getString("String_TerrainInterpolateType_Kriging");
            } else if (type == TerrainInterpolateType.TIN) {
                result = CommonProperties.getString("String_TerrainInterpolateType_TIN");
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        return result;
    }

    public static TerrainInterpolateType valueOf(String text) {
        TerrainInterpolateType type = null;
        try {
            if (text.equalsIgnoreCase(CommonProperties.getString("String_TerrainInterpolateType_TIN"))) {
                type = TerrainInterpolateType.TIN;
            } else if (text.equalsIgnoreCase(CommonProperties.getString("String_TerrainInterpolateType_KRIGING"))) {
                type = TerrainInterpolateType.KRIGING;
            } else if (text.equalsIgnoreCase(CommonProperties.getString("String_TerrainInterpolateType_IDW"))) {
                type = TerrainInterpolateType.IDW;
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        return type;
    }
}
