package com.supermap.desktop.ui.icloud.fastJSon;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.supermap.desktop.ui.icloud.commontypes.*;

/**
 * Created by xie on 2016/12/24.
 */
public class CloudLicenseFastjsonConfig {
    private static ParserConfig parserConfig = null;

    public static synchronized ParserConfig getParserConfig() {
        if (parserConfig == null) {
            parserConfig = new ParserConfig();
            parserConfig.putDeserializer(ProductType.class, new ProductTypeDeserializer());
            parserConfig.putDeserializer(ProductId.class, new ProductIdDeserializer());
            parserConfig.putDeserializer(Module.class, new ModuleDeserializer());
            parserConfig.putDeserializer(LicenseId.class, new LicenseIdDeserializer());
            parserConfig.putDeserializer(ReturnId.class, new ReturnIdDeserializer());
            parserConfig.putDeserializer(LicenseInfo.Status.class, new StatusDeserializer());
            parserConfig.putDeserializer(Version.class, new VersionDeserializer());
        }
        return parserConfig;
    }

    private static SerializeConfig serializeConfig = null;

    public static synchronized SerializeConfig getSerializeConfig() {
        if (serializeConfig == null) {
            serializeConfig = new SerializeConfig();
            serializeConfig.put(ProductType.class, new ProductTypeSerializer());
            serializeConfig.put(Version.class, new VersionSerializer());
        }
        return serializeConfig;
    }
}
