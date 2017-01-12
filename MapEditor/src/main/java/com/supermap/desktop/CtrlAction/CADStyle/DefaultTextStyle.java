package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.*;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by lixiaoyao on 2017/1/6.
 */
public class DefaultTextStyle {
    private static HashMap<String, TextDefaultStyle> mapNameAndStyle = new HashMap<>();

    public static void setRotationAngle(String tempMapName, String tempLayerName, double tempRotationAngle) {
        if (!mapNameAndStyle.containsKey(tempMapName)) {
            TextDefaultStyle textDefaultStyle = new TextDefaultStyle();
            textDefaultStyle.setRotationAngle(tempRotationAngle);
            textDefaultStyle.setPreviousLayerName(tempLayerName);
            mapNameAndStyle.put(tempMapName, textDefaultStyle);
        } else {
            TextDefaultStyle hasSave = mapNameAndStyle.get(tempMapName);
            if (!hasSave.getPreviousLayerName().equals(tempLayerName)) {
                hasSave.resetTextDefultStyle();
                hasSave.setPreviousLayerName(tempLayerName);
            }
            if (Double.compare(tempRotationAngle, hasSave.getRotationAngle()) != 0) {
                hasSave.setRotationAngle(tempRotationAngle);
            }
        }
    }

    public static double getRotationAngle(String tempMapName) {
        double tempRotationAngle = 0;
        if (!mapNameAndStyle.containsKey(tempMapName)) {
            TextDefaultStyle textDefaultStyle = new TextDefaultStyle();
            tempRotationAngle = textDefaultStyle.getRotationAngle();
        } else {
            TextDefaultStyle hasSave = mapNameAndStyle.get(tempMapName);
            tempRotationAngle = hasSave.getRotationAngle();
        }
        return tempRotationAngle;
    }

    public static void setDefaultGeoStyle(String tempMapName, String tempLayerName,TextStyle tempGeoStyle) {
        if (!mapNameAndStyle.containsKey(tempMapName)) {
            TextDefaultStyle textDefaultStyle = new TextDefaultStyle();
            textDefaultStyle.setDefaultGeoStyle(tempGeoStyle.clone());
            textDefaultStyle.setPreviousLayerName(tempLayerName);
            mapNameAndStyle.put(tempMapName, textDefaultStyle);
        } else {
            TextDefaultStyle hasSave = mapNameAndStyle.get(tempMapName);
            if (!hasSave.getPreviousLayerName().equals(tempLayerName)) {
                hasSave.resetTextDefultStyle();
                hasSave.setPreviousLayerName(tempLayerName);
            }
            if (!hasSave.getDefaultGeoStyle().equals(tempGeoStyle)) {
                hasSave.setDefaultGeoStyle(tempGeoStyle.clone());
            }
        }
    }

    public static TextStyle getDefaultGeoStyle(String tempMapName) {
        TextStyle textStyle = new TextStyle();
        if (!mapNameAndStyle.containsKey(tempMapName)) {
            TextDefaultStyle textDefaultStyle = new TextDefaultStyle();
            textStyle = textDefaultStyle.getDefaultGeoStyle().clone();
        } else {
            TextDefaultStyle hasSave = mapNameAndStyle.get(tempMapName);
            textStyle = hasSave.getDefaultGeoStyle().clone();
        }
        return textStyle;
    }

}
