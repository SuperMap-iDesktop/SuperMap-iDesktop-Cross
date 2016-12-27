package com.supermap.desktop.ui.icloud.api;

import com.supermap.desktop.ui.icloud.commontypes.*;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by xie on 2016/12/24.
 */
public interface LicenseService extends Closeable {
    /**
     * 查询是否有试用许可
     *
     * @param paramQueryTrialLicenseRequest
     * @return
     * @throws IOException
     */
    ServiceResponse<QueryTrialLicenseResponse> query(QueryTrialLicenseRequest paramQueryTrialLicenseRequest)
            throws IOException;

    /**
     * 查询是否有正式许可
     *
     * @param paramQueryFormalLicenseRequest
     * @return
     * @throws IOException
     */
    ServiceResponse<QueryFormalLicenseResponse> query(QueryFormalLicenseRequest paramQueryFormalLicenseRequest)
            throws IOException;

    /**
     * 申请正式许可
     *
     * @param paramApplyFormalLicenseRequest
     * @return
     * @throws IOException
     */
    ServiceResponse<ApplyFormalLicenseResponse> apply(ApplyFormalLicenseRequest paramApplyFormalLicenseRequest)
            throws IOException;

    /**
     * 申请试用许可
     *
     * @param paramApplyTrialLicenseRequest
     * @return
     * @throws IOException
     */
    ServiceResponse<ApplyTrialLicenseResponse> apply(ApplyTrialLicenseRequest paramApplyTrialLicenseRequest)
            throws IOException;

    /**
     * 返回License信息
     *
     * @param paramReturnLicenseRequest
     * @return
     * @throws IOException
     */
    ServiceResponse<Integer> returns(ReturnLicenseRequest paramReturnLicenseRequest)
            throws IOException;

    /**
     * 归还试用许可
     *
     * @param paramReturnId
     * @return
     * @throws IOException
     */
    ServiceResponse<Void> deleteTrialLicense(ReturnId paramReturnId)
            throws IOException;
}

