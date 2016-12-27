package com.supermap.desktop.ui.icloud.commontypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xie on 2016/12/24.
 */
public class Version {
    private final String name;
    private static final Map<String, Version> VERSIONS = new HashMap<>();
    public static final Version VERSION_8C = newVersion("8C");

    public Version(String paramName) {
        this.name = paramName;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    private static Version newVersion(String name) {
        Version result = new Version(name);
        VERSIONS.put(name, result);
        return result;
    }

    public static Version valueOf(String name) {
        return VERSIONS.containsKey(name) ? VERSIONS.get(name) : new Version(name);
    }
}
