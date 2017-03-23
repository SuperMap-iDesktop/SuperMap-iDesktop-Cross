package com.supermap.desktop.process.util;

import com.supermap.data.Enum;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.utilities.StringUtilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/3/8.
 * EnumParser:
 * EnumType is the enum class
 * EnumValues array store your enum value, which you want to translate,
 * ChNames array store the chinese name for enum values;
 */
public class EnumParser {
    private boolean isSuperMapEnum = false;
    private Class enumClass;
    private String[] chName;
    private String[] enumNames;
    private CopyOnWriteArrayList enumItems = new CopyOnWriteArrayList();

    public EnumParser() {

    }

    public EnumParser(Class enumClass, String[] enumNames, String... chName) {
        this.enumClass = enumClass;
        this.enumNames = enumNames;
        this.chName = chName;
        parse();
    }

    public void setEnumNames(String[] enumNames) {
        this.enumNames = enumNames;
    }

    public void setEnumClass(Class enumClass) {
        this.enumClass = enumClass;
    }

    public void setChName(String[] chName) {
        this.chName = chName;
    }

    /**
     * Enum class
     * Chinese name reflect to a enum value
     */
    public void parse() {
        if (null == enumClass || StringUtilities.isNullOrEmptyString(chName)) {
            return;
        }
        if (enumClass.getSuperclass() == Enum.class) {
            isSuperMapEnum = true;
        }
        if (isSuperMapEnum) {
            try {
                if (enumNames.length == chName.length) {
                    int newLength = enumNames.length;
                    Method parse = enumClass.getMethod("parse", Class.class, String.class);
                    for (int i = 0; i < newLength; i++) {
                        ParameterDataNode item = new ParameterDataNode(chName[i], parse.invoke(Enum.class, enumClass, enumNames[i]));
                        enumItems.add(item);
                    }
                }

            } catch (NoSuchMethodException e) {
                Application.getActiveApplication().getOutput().output(e);
            } catch (IllegalAccessException e) {
                Application.getActiveApplication().getOutput().output(e);
            } catch (InvocationTargetException e) {
                Application.getActiveApplication().getOutput().output(e);
            }
        } else if (enumClass.isEnum()) {
            Object[] enumConstants = enumClass.getEnumConstants();
            if (enumConstants != null && enumConstants.length > 0) {
                int size = enumConstants.length;
                if (enumNames.length == size && enumNames.length == chName.length) {
                    for (int i = 0; i < size; i++) {
                        ParameterDataNode item = new ParameterDataNode(chName[i], enumConstants[i]);
                        enumItems.add(item);
                    }
                } else if (enumNames.length == chName.length) {
                    for (int i = 0; i < size; i++) {
                        java.lang.Enum nowEnum = (java.lang.Enum) enumConstants[i];
                        int enumNameCount = enumNames.length;
                        for (int j = 0; j < enumNameCount; j++) {
                            if (enumNames[j].equalsIgnoreCase(nowEnum.name())) {
                                ParameterDataNode item = new ParameterDataNode(chName[j], nowEnum);
                                enumItems.add(item);
                            }
                        }
                    }
                }
            }
        }
    }

    public CopyOnWriteArrayList getEnumItems() {
        return enumItems;
    }

    /**
     * Set your custom arrays
     *
     * @param enumItems
     */
    public void setEnumItems(CopyOnWriteArrayList enumItems) {
        this.enumItems = enumItems;
    }

}
