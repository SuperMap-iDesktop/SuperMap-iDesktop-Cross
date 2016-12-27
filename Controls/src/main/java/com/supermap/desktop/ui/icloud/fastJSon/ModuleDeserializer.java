package com.supermap.desktop.ui.icloud.fastJSon;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.supermap.desktop.ui.icloud.commontypes.Module;

import java.lang.reflect.Type;

/**
 * Created by xie on 2016/12/24.
 */
public class ModuleDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        String value = parser.parseObject(String.class);
        String[] values = value.split(",");
        Module[] modules = new Module[values.length];
        for (int i = 0; i < values.length; i++) {
            modules[i] = Module.valueOf(values[i]);
        }
        return (T) modules;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
