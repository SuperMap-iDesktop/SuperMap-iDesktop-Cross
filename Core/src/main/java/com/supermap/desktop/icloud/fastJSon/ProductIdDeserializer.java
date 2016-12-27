package com.supermap.desktop.icloud.fastJSon;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.supermap.desktop.icloud.commontypes.ProductId;

import java.lang.reflect.Type;

/**
 * Created by xie on 2016/12/24.
 */
public class ProductIdDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String value = parser.parseObject(String.class);
        return (T) ProductId.valueOf(value);
    }


    public int getFastMatchToken() {
        return 0;
    }
}
