package com.supermap.desktop.icloud.fastJSon;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.supermap.desktop.icloud.commontypes.LicenseId;

import java.lang.reflect.Type;

/**
 * Created by xie on 2016/12/24.
 */
public class LicenseIdDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        String value = parser.parseObject(String.class);
        return (T) new LicenseId(value);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
