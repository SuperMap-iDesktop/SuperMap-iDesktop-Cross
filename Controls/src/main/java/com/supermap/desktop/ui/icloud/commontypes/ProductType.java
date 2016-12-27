package com.supermap.desktop.ui.icloud.commontypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xie on 2016/12/24.
 * 产品类型
 */
public class ProductType {
    private static final Map<String, ProductType> ALL_BY_CODE = new HashMap<>();
    public static final ProductType IDESKTOP = newProductType("10000", "SuperMap iDesktop");
    public static final ProductType ISERVER = newProductType("20000", "SuperMap iServer Java");
    public static final ProductType IEXPRESS = newProductType("30000", "SuperMap iExpress");
    public static final ProductType ICLOUDMANAGER = newProductType("40000", "SuperMap iCloudManager");
    public static final ProductType IPORTAL = newProductType("50000", "SuperMap iPortal");
    public static final ProductType IOBJECT = newProductType("60000", "SuperMap iObject");
    public static final ProductType IMOBILE = newProductType("70000", "SuperMap iMobile");
    private final String code;
    private final String description;

    private static ProductType newProductType(String code, String description) {
        ProductType result = new ProductType(code, description);
        ALL_BY_CODE.put(result.code, result);
        return result;
    }

    public static ProductType valueOf(String code) {
        return ALL_BY_CODE.containsKey(code) ? ALL_BY_CODE.get(code) : new ProductType("code", "UNKNOWN");
    }

    public ProductType(String paramCode, String paramDescription) {
        this.code = paramCode;
        this.description = paramDescription;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return this.description + "_" + this.code;
    }
}
