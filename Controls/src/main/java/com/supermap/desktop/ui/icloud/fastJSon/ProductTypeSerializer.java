package com.supermap.desktop.ui.icloud.fastJSon;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.supermap.desktop.ui.icloud.commontypes.ProductType;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by xie on 2016/12/24.
 */
public class ProductTypeSerializer implements ObjectSerializer {
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
            throws IOException {
        SerializeWriter out = serializer.getWriter();
        ProductType type = (ProductType) object;
        out.writeString(type.getCode());
    }
}
