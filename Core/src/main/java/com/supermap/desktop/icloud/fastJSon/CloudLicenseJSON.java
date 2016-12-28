package com.supermap.desktop.icloud.fastJSon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by xie on 2016/12/24.
 */
public class CloudLicenseJSON extends JSON {
    public static <T> T parseObject(String text, TypeReference<T> type, Feature... features) {
        try {
            return parseObject(text, type.getType(), CloudLicenseFastjsonConfig.getParserConfig(), DEFAULT_PARSER_FEATURE, features);
        } catch (JSONException e) {
            throw new IllegalArgumentException("unexpected response text:" + text);
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        try {
            return parseObject(text, clazz, CloudLicenseFastjsonConfig.getParserConfig(), DEFAULT_PARSER_FEATURE, new Feature[0]);
        } catch (JSONException e) {
            throw new IllegalArgumentException("unexpected response text:" + text);
        }
    }

    public static String toJSONString(Object object) {
        return toJSONString(object, CloudLicenseFastjsonConfig.getSerializeConfig(), new SerializerFeature[0]);
    }

    public static String toJSONString(Object object, SerializeFilter filter, SerializerFeature... features) {
        return toJSONString(object, CloudLicenseFastjsonConfig.getSerializeConfig(), filter, features);
    }
}
