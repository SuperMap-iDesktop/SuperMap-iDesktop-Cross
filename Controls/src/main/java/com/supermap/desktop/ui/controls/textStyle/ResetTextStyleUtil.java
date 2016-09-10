package com.supermap.desktop.ui.controls.textStyle;

import com.supermap.data.StringAlignment;
import com.supermap.data.TextAlignment;
import com.supermap.data.TextStyle;
import com.supermap.desktop.enums.TextStyleType;

import java.awt.*;

public class ResetTextStyleUtil {

    public ResetTextStyleUtil() {
        //工具类不用构造函数
    }

    public static void resetTextStyle(TextStyleType newValue, TextStyle textStyle, Object newTextStyleValue) {
        if (null == newTextStyleValue) {
            return;
        }
        switch (newValue) {
            case FONTNAME:
                textStyle.setFontName((String) newTextStyleValue);
                break;
            case ALIGNMENT:
                textStyle.setAlignment((TextAlignment) newTextStyleValue);
                break;
            case FONTSIZE:
                textStyle.setFontHeight((double) newTextStyleValue);
                break;
            case FONTHEIGHT:
                textStyle.setFontHeight((double) newTextStyleValue);
                break;
            case FONTWIDTH:
                textStyle.setFontWidth((double) newTextStyleValue);
                break;
            case FORECOLOR:
                textStyle.setForeColor((Color) newTextStyleValue);
                break;
            case BACKCOLOR:
                textStyle.setBackColor((Color) newTextStyleValue);
                break;
            case ROTATION:
                textStyle.setRotation((double) newTextStyleValue);
                break;
            case ITALICANGLE:
                textStyle.setItalicAngle((double) newTextStyleValue);
                break;
            case BOLD:
                textStyle.setBold((boolean) newTextStyleValue);
                break;
            case STRIKOUT:
                textStyle.setStrikeout((boolean) newTextStyleValue);
                break;
            case ITALIC:
                textStyle.setItalic((boolean) newTextStyleValue);
                break;
            case UNDERLINE:
                textStyle.setUnderline((boolean) newTextStyleValue);
                break;
            case SHADOW:
                textStyle.setShadow((boolean) newTextStyleValue);
                break;
            case FIXEDSIZE:
                textStyle.setSizeFixed((boolean) newTextStyleValue);
                break;
            case BACKOPAQUE:
                textStyle.setBackOpaque((boolean) newTextStyleValue);
                break;
            case OUTLINE:
                textStyle.setOutline((boolean) newTextStyleValue);
                break;
            case OUTLINEWIDTH:
                textStyle.setOutlineWidth((int) newTextStyleValue);
                break;
            case STRINGALIGNMENT:
                textStyle.setStringAlignment((StringAlignment) newTextStyleValue);
                break;
            default:
                break;
        }
    }
}
