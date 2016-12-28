package com.supermap.desktop.icloud.fastJSon;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.supermap.desktop.icloud.commontypes.Version;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by xie on 2016/12/24.
 */
public class VersionSerializer implements ObjectSerializer {
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
            throws IOException {
        SerializeWriter out = serializer.getWriter();
        Version version = (Version) object;
        out.writeString(version.getName());
    }
}
