package com.supermap.desktop.icloud.commontypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xie on 2016/12/24.
 */
public class QueryFormalLicenseRequest {
    private ProductType productType;
    private Version version;
    private int page = 1;
    private int pageCount;
    Map<String, String> querStrMap = new HashMap<>();

    public QueryFormalLicenseRequest productType(ProductType value) {
        this.productType = value;
        this.querStrMap.put("productType", this.productType.getCode());
        return this;
    }

    public QueryFormalLicenseRequest version(Version value) {
        this.version = value;
        this.querStrMap.put("version", this.version.getName());
        return this;
    }

    public QueryFormalLicenseRequest page(int value) {
        this.page = value;
        this.querStrMap.put("page", String.valueOf(this.page));
        return this;
    }

    public QueryFormalLicenseRequest pageCount(int value) {
        this.pageCount = value;
        this.querStrMap.put("pageCount", String.valueOf(this.pageCount));
        return this;
    }

    public String getQueryStr() {
        String queryStr = "";
        for (Map.Entry<String, String> entry : this.querStrMap.entrySet()) {
            queryStr = queryStr + "&" + entry.getKey() + "=" + entry.getValue();
        }
        return queryStr;
    }
}
