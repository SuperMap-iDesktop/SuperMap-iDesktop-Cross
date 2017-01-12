package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import java.awt.*;

/**
 * Created by lixiaoyao on 2017/1/12.
 */
public class TextDefaultStyle {

    private double rotationAngle = 0;
    private TextStyle defaultTextStyle = null;
    private String previousLayerName = "";
    private static final double defaultFontWeight = 3.704375; //  默认字体大小10.5号字体

    public void setRotationAngle(double tempRotationAngle) {
        rotationAngle = tempRotationAngle;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setDefaultGeoStyle(TextStyle tempGeoStyle) {
        defaultTextStyle = tempGeoStyle;
    }

    public TextStyle getDefaultGeoStyle() {
        if (defaultTextStyle == null) {
            getdefaultTextStyle();
        }
        return defaultTextStyle;
    }

    public void setPreviousLayerName(String tempLayerName) {
        previousLayerName = tempLayerName;
    }

    public String getPreviousLayerName() {
        return previousLayerName;
    }

    public void resetTextDefultStyle() {
        if (Double.compare(rotationAngle, 0) != 0) {
            rotationAngle = 0;
        }
        if (defaultTextStyle != null) {
            getdefaultTextStyle();
        }
    }

    private void getdefaultTextStyle() {
        TextStyle textStyle = new TextStyle();
        //Font systemFont=Font.getDefault();
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        textStyle.setFontName(fonts[0]);
        textStyle.setFontHeight(defaultFontWeight);
        defaultTextStyle = textStyle;
    }
}
