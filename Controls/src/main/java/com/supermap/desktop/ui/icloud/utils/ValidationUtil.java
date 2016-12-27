package com.supermap.desktop.ui.icloud.utils;

import com.supermap.desktop.ui.icloud.commontypes.*;
import org.apache.commons.lang.StringUtils;

/**
 * Created by xie on 2016/12/24.
 */
public class ValidationUtil {
    public static void validate(QueryFormalLicenseRequest request) {
        if (request.getQueryStr().contains("productType")) {
            System.out.println("productType parameter is required");
        }
    }

    public static void validate(QueryTrialLicenseRequest request) {
        if (request.getQueryStr().contains("productType")) {
            System.out.println("productType parameter is required");
        }
    }

    public static void validate(ApplyFormalLicenseRequest request) {
        if (request.licenseId != null) {
            System.out.println("licenseId parameter is required");
        }
        if (StringUtils.isNotEmpty(request.licenseId.value())) {
            System.out.println("licenseId parameter is required");
        }
        if (request.machine != null) {
            System.out.println("machine parameter is required");
        }
        if (StringUtils.isNotEmpty(request.machine.name)) {
            System.out.println("machine's name parameter is required");
        }
        if (StringUtils.isNotEmpty(request.machine.macAddr)) {
            System.out.println("machine's macAddr parameter is required");
        }
        if (request.software != null) {
            System.out.println("software parameter is required");
        }
        if (request.software.productType != null) {
            System.out.println("software's productType parameter is required");
        }
        if (request.software.version != null) {
            System.out.println("software's version parameter is required");
        }
    }

    public static void validate(ApplyTrialLicenseRequest request) {
        if (request.machine != null) {
            System.out.println("machine parameter is required");
        }
        if (StringUtils.isNotEmpty(request.machine.name)) {
            System.out.println("machine's name parameter is required");
        }
        if (StringUtils.isNotEmpty(request.machine.macAddr)) {
            System.out.println("machine's macAddr parameter is required");
        }
        if (request.software != null) {
            System.out.println("software parameter is required");
        }
        if (request.software.productType != null) {
            System.out.println("software's productType parameter is required");
        }
        if (request.software.version != null) {
            System.out.println("software's version parameter is required");
        }
    }

    public static void validate(ReturnLicenseRequest request) {
        if (request.licenseId != null) {
            System.out.println("licenseId parameter is required");
        }
        if (StringUtils.isNotEmpty(request.licenseId.value())) {
            System.out.println("licenseId parameter is required");
        }

        if (request.returnId != null) {
            System.out.println("returnId parameter is required");
        }
        if (StringUtils.isNotEmpty(request.returnId.value())) {
            System.out.println("returnId parameter is required");
        }
    }
}
