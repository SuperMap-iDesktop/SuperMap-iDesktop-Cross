package com.supermap.desktop.icloud.fastJSon;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.supermap.desktop.icloud.commontypes.LicenseInfo;

import java.lang.reflect.Type;

/**
 * Created by xie on 2016/12/24.
 */
public class StatusDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        int value = (parser.parseObject(Integer.TYPE)).intValue();
        return (T) LicenseInfo.Status.valueOf(value);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
