package com.supermap.desktop.icloud.fastJSon;

import com.alibaba.fastjson.serializer.PropertyFilter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xie on 2016/12/24.
 */
public class ExcludesPropertyFilter implements PropertyFilter {
    private Set<String> excludes = new HashSet<>();

    public void setExcludes(String... propertyNames) {
        this.excludes.addAll(Arrays.asList(propertyNames));
    }

    public boolean apply(Object object, String name, Object value) {
        return !this.excludes.contains(name);
    }
}
