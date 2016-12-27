package com.supermap.desktop.ui.icloud.commontypes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by xie on 2016/12/24.
 */
public class ProductId {
    private static final Map<String, ProductId> ALL_BY_CODE = new HashMap<>();
    public static final ProductId IDESKTOP_8C_STANDARD = newProductId("1001075", ProductType.IDESKTOP, Edition.STANDARD);
    public static final ProductId IDESKTOP_8C_PROFESSIONAL = newProductId("1001076", ProductType.IDESKTOP, Edition.PROFESSIONAL);
    public static final ProductId IDESKTOP_8C_ADVANCED = newProductId("1001077", ProductType.IDESKTOP, Edition.ADVANCED);
    public static final ProductId ISERVER_8C_STANDARD = newProductId("1001081", ProductType.ISERVER, Edition.STANDARD);
    public static final ProductId ISERVER_8C_PROFESSIONAL = newProductId("1001082", ProductType.ISERVER, Edition.PROFESSIONAL);
    public static final ProductId ISERVER_8C_ADVANCED = newProductId("1001083", ProductType.ISERVER, Edition.ADVANCED);
    private final ProductType type;
    private final String code;
    private Set<Edition> editions;

    private static ProductId newProductId(String code, ProductType type, Edition edition) {
        ProductId result = new ProductId(code, type, edition);
        ALL_BY_CODE.put(result.code, result);
        return result;
    }

    public static ProductId valueOf(String code) {
        return ALL_BY_CODE.get(code);
    }

    public ProductId(String paramCode, ProductType paramType, Edition paramEdition) {
        this.type = paramType;
        this.code = paramCode;
        this.editions = new HashSet<>();
    }

    public ProductId(String paramCode, ProductType paramType) {
        this(paramCode, paramType, Edition.NOT_APPLICABLE);
    }

    public ProductType getType() {
        return this.type;
    }

    public String getCode() {
        return this.code;
    }

    public String toString() {
        return "type:" + this.type.toString() + ";code:" + this.code;
    }

//    public <T extends EditionVisitor> T acceptEditionVisitor(T visitor)
//    {
//        for (Edition edition : this.editions) {
//            edition.call(visitor);
//        }
//        return visitor;
//    }
}
