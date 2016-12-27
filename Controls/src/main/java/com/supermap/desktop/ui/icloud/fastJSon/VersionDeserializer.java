package com.supermap.desktop.ui.icloud.fastJSon;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.supermap.desktop.ui.icloud.commontypes.Version;

import java.lang.reflect.Type;

/**
 * Created by xie on 2016/12/24.
 */
public class VersionDeserializer implements ObjectDeserializer {
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String value = parser.parseObject(String.class);
        return (T) Version.valueOf(value);
    }

    public int getFastMatchToken() {
        return 0;
    }
}
