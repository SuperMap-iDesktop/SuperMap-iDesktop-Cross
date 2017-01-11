package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ITextFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

import java.awt.*;
import java.util.List;

/**
 * Created by lixiaoyao on 2017/1/6.
 */
public class DefaultTextStyle {
    private static double rotationAngle = 0;
    private static TextStyle defaultTextStyle = null;
    private static String previousLayerName="";
    private static String previousMapName="";
    private static final double defaultFontWeight=2.8035; //  默认字体大小

    public static void setRotationAngle(double tempRotationAngle) {
        rotationAngle = tempRotationAngle;
    }

    public static double getRotationAngle() {
        return rotationAngle;
    }

    public static void setDefaultGeoStyle(TextStyle tempGeoStyle) {
        defaultTextStyle = tempGeoStyle;
    }

    public static TextStyle getDefaultGeoStyle() {
        if (defaultTextStyle==null){
            getdefaultTextStyle();
        }
        return defaultTextStyle;
    }

    public static void resetDefaultTextStyle(){
        if (Double.compare(rotationAngle,0)!=0){
            rotationAngle=0;
        }
        if (defaultTextStyle!=null){
            getdefaultTextStyle();
        }
    }

    public static void isNeedReset(String tempLayerName,String tempMapName){
        if (previousLayerName!="" && !tempLayerName.equals(previousLayerName)){
            resetDefaultTextStyle();
        }
        if (tempLayerName.equals(previousLayerName) && !tempMapName.equals(previousMapName)){
            resetDefaultTextStyle();
        }
        previousLayerName=tempLayerName;
        previousMapName=tempMapName;
    }

    private static void getdefaultTextStyle(){
        TextStyle textStyle = new TextStyle();
        String fonts[]= GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        textStyle.setFontName(fonts[0]);
        textStyle.setFontHeight(defaultFontWeight);
        defaultTextStyle=textStyle;
    }
}
