package com.supermap.desktop.geometryoperation;

import com.supermap.data.GeoStyle;

import java.awt.*;

/**
 * Created by lixiaoyao on 2017/3/2.
 */
public class RegionAndLineHighLightStyle {
    private static GeoStyle regionStyleRed = null;
    private static GeoStyle regionStyleBlue = null;
    private static GeoStyle lineStyleRed = null;
    private static GeoStyle lineStyleBlue = null;

    public static GeoStyle getRegionStyleRed() {
        if (regionStyleRed == null) {
            initRegionStyleRed();
        }
        return regionStyleRed;
    }

    public static GeoStyle getRegionStyleBlue() {
        if (regionStyleBlue == null) {
            initRegionStyleBlue();
        }
        return regionStyleBlue;
    }

    public static GeoStyle getLineStyleRed() {
        if (lineStyleRed == null) {
            initLineStyleRed();
        }
        return lineStyleRed;
    }

    public static GeoStyle getLineStyleBlue() {
        if (lineStyleBlue == null) {
            initLineStyleBlue();
        }
        return lineStyleBlue;
    }

    private static void initRegionStyleRed() {
        regionStyleRed = new GeoStyle();
        regionStyleRed.setLineColor(Color.RED);
        regionStyleRed.setFillOpaqueRate(0);
    }

    private static void initRegionStyleBlue() {
        regionStyleBlue = new GeoStyle();
        regionStyleBlue.setLineColor(Color.BLUE);
        regionStyleBlue.setFillOpaqueRate(0);
    }

    private static void initLineStyleRed() {
        lineStyleRed = new GeoStyle();
        lineStyleRed.setLineWidth(0.6);
        lineStyleRed.setLineColor(Color.RED);
    }

    private static void initLineStyleBlue() {
        lineStyleBlue = new GeoStyle();
        lineStyleBlue.setLineWidth(0.6);
        lineStyleBlue.setLineColor(Color.BLUE);
    }
}
