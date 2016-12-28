package com.supermap.desktop.icloud.commontypes;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xie on 2016/12/24.
 * 许可信息类
 */
public class LicenseInfo {
    public LicenseId id;
    public int days;
    public int remainDays;
    public String snId;
    public Module[] moduleCode;
    public ProductId productId;
    public ProductType productType;
    public int allocationUserId;
    public Status status;
    public long time;
    public Version version;
    public boolean permanent;
    public String lockMacAddr;
    public int unlockNumber;

    public LicenseInfo() {
    }

    public LicenseInfo(LicenseInfo licenseInfo) {
        this.id = licenseInfo.id;
        this.days = licenseInfo.days;
        this.remainDays = licenseInfo.remainDays;
        this.snId = licenseInfo.snId;
        this.moduleCode = ((Module[]) ArrayUtils.clone(licenseInfo.moduleCode));
        this.productId = licenseInfo.productId;
        this.productType = licenseInfo.productType;
        this.allocationUserId = licenseInfo.allocationUserId;
        this.permanent = licenseInfo.permanent;
        this.lockMacAddr = licenseInfo.lockMacAddr;
        this.unlockNumber = licenseInfo.unlockNumber;
        this.status = licenseInfo.status;
        this.time = licenseInfo.time;
        this.version = licenseInfo.version;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public static class Status {
        private final int code;
        private final String name;
        private final int hashCode;
        public static final Status ALLOCATED = new Status(2, "ALLOCATED");
        public static final Status NON_ALLOCATED = new Status(1, "NON_ALLOCATED");
        private static final Map<Integer, Status> ALL_BY_CODE = new HashMap<>();

        static {
            ALL_BY_CODE.put(Integer.valueOf(ALLOCATED.code), ALLOCATED);
            ALL_BY_CODE.put(Integer.valueOf(NON_ALLOCATED.code), NON_ALLOCATED);
        }

        public Status(int paramCode, String paramName) {
            this.code = paramCode;
            this.name = paramName;
            this.hashCode = new HashCodeBuilder(1612201553, 1612201555).append(this.code).append(this.name).toHashCode();
        }

        public int getCode() {
            return this.code;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (!Status.class.equals(other.getClass())) {
                return false;
            }
            Status status = (Status) other;
            return (status.code == this.code) && (StringUtils.equals(status.name, this.name));
        }

        public int hashCode() {
            return this.hashCode;
        }

        public String toString() {
            return this.name;
        }

        public static Status valueOf(int code) {
            return ALL_BY_CODE.containsKey(Integer.valueOf(code)) ? ALL_BY_CODE.get(Integer.valueOf(code)) : new Status(code, "UNKNOWN");
        }

        public static boolean isAllocated(Status status) {
            return status.code == ALLOCATED.code;
        }

        public static boolean isNonAllocated(Status status) {
            return status.code == NON_ALLOCATED.code;
        }
    }
}
