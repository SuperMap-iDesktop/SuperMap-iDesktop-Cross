package com.supermap.desktop.UserDefineType;

/**
 * Created by xie on 2017/3/29.
 */
public enum UserDefineFileType {
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
