package com.supermap.desktop.icloud.fastJSon;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.supermap.desktop.icloud.commontypes.ReturnId;

import java.lang.reflect.Type;

/**
 * Created by xie on 2016/12/24.
 */
public class ReturnIdDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        String returnId = defaultJSONParser.parseObject(String.class);
        return (T) new ReturnId(returnId);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
