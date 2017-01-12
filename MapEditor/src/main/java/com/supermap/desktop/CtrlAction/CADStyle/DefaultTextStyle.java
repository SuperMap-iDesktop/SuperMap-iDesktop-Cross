package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.*;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by lixiaoyao on 2017/1/6.
 */
public class DefaultTextStyle {
    //    private static double rotationAngle = 0;
//    private static TextStyle defaultTextStyle = null;
//    private static String previousLayerName = "";
//    private static String previousMapName = "";
//    private static final double defaultFontWeight = 2.8035; //  默认字体大小10.5号字体
    private static HashMap<String, TextDefaultStyle> mapNameAndStyle = new HashMap<>();

//    public static void setStyle(String tempLayerName, String tempMapName,double tempRotationAngle,TextStyle tempTextStyle){
//        if (!mapNameAndStyle.containsKey(tempMapName)) {
//            TextDefaultStyle textDefaultStyle = new TextDefaultStyle();
//            textDefaultStyle.setRotationAngle(tempRotationAngle);
//            textDefaultStyle.setDefaultGeoStyle(tempTextStyle);
//            textDefaultStyle.setPreviousLayerName(tempLayerName);
//            mapNameAndStyle.put(tempMapName,textDefaultStyle);
//        }else{
//            TextDefaultStyle hasSave= mapNameAndStyle.get(tempMapName);
//            if (!hasSave.getPreviousLayerName().equals(tempLayerName)){
//                hasSave.resetTextDefultStyle();
//            }
//        }
//    }

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

//    public static void resetDefaultTextStyle() {
//        if (Double.compare(rotationAngle, 0) != 0) {
//            rotationAngle = 0;
//        }
//        if (defaultTextStyle != null) {
//            getdefaultTextStyle();
//        }
//    }
//
//    public static void isNeedReset(String tempLayerName, String tempMapName) {
//        if (previousLayerName != "" && !tempLayerName.equals(previousLayerName)) {
//            resetDefaultTextStyle();
//        }
//        if (tempLayerName.equals(previousLayerName) && !tempMapName.equals(previousMapName)) {
//            resetDefaultTextStyle();
//        }
//        previousLayerName = tempLayerName;
//        previousMapName = tempMapName;
//    }
//
//    private static void getdefaultTextStyle() {
//        TextStyle textStyle = new TextStyle();
//        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//        textStyle.setFontName(fonts[0]);
//        textStyle.setFontHeight(defaultFontWeight);
//        defaultTextStyle = textStyle;
//    }
}
