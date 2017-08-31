package com.supermap.desktop.implement.UserDefineType;

/**
 * Created by xie on 2017/3/29.
 */
public enum UserDefineFileType {
    EXCEL("xlsx"),
    GPX("gpx");
    private String value;

    UserDefineFileType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
