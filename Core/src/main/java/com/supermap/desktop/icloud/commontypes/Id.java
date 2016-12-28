package com.supermap.desktop.icloud.commontypes;

import org.apache.commons.lang.ClassUtils;

/**
 * Created by xie on 2016/12/24.
 */
public class Id {
    private final String SHOT_CLASS_NAME = ClassUtils.getShortClassName(getClass());
    private final String value;

    public Id(String paramValue) {
        this.value = paramValue;
    }

    public String value() {
        return this.value;
    }

    public String toString() {
        return this.SHOT_CLASS_NAME + "_" + this.value;
    }
}
