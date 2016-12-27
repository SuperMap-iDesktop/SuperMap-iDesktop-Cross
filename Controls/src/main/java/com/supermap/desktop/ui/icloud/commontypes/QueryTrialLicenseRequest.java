package com.supermap.desktop.ui.icloud.commontypes;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by xie on 2016/12/24.
 * 返回的试用许可Request类，用于查询是否有试用许可
 */
public class QueryTrialLicenseRequest {
    private ProductType productType;
    private Version version;
    Map<String, String> querStrMap = new HashMap<>();

    public QueryTrialLicenseRequest productType(ProductType value) {
        this.productType = value;
        this.querStrMap.put("productType", this.productType.getCode());
        return this;
    }

    public QueryTrialLicenseRequest version(Version value) {
        this.version = value;
        this.querStrMap.put("version", this.version.getName());
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
