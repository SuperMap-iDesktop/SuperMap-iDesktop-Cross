package com.supermap.desktop.icloud;


import com.supermap.data.License;
import com.supermap.data.Toolkit;
import com.supermap.desktop.Application;
import com.supermap.desktop.icloud.api.LicenseService;
import com.supermap.desktop.icloud.commontypes.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.ComputerUtilities;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by xie on 2016/12/23.
 * 许可管理类
 */
public class LicenseManager {
    private static final String LIC_DIRCTORY = "C:\\Program Files\\Common Files\\SuperMap\\License";
    private static final String LINUX_LIC_DIRCTORY = "/opt/License";
    private static final String ONLINE_DIRCTORY = SystemPropertyUtilities.isWindows() ? LIC_DIRCTORY + "\\online\\" : LINUX_LIC_DIRCTORY + "/online/";
    private static final String ONLINE_LICENSEFILE = ONLINE_DIRCTORY + ComputerUtilities.getComputerName() + "_8C.lic";

    /**
     * 验证试用许可是否可用
     *
     * @return
     */
    public static boolean valiteTrialLicense() {
        boolean result = false;
        ArrayList<com.supermap.data.ProductType> trialLicense = Toolkit.getTrialProducts();
        License var2 = new License();
        Iterator var3 = trialLicense.iterator();

        while (var3.hasNext()) {
            com.supermap.data.ProductType var4 = (com.supermap.data.ProductType) var3.next();
            int var1 = var2.connect(var4);
            if (var1 == 0) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 判段正式许可(此方法无法检测本机试用许可，只对java开发有用)
     *
     * @return
     */
    public static boolean valiteLicense() {
        boolean valitedLicense = false;
        License license = new License();
        /**
         * JAVA开发版本中用到了许可类型，只用全部满足时才为true
         */
        int valite = license.connect(com.supermap.data.ProductType.IOBJECTS_CORE_DEVELOP);
        if (valite == 0) {
            valitedLicense = true;
        }
        return valitedLicense;
    }

    /**
     * 判断日期大小
     *
     * @param endStr
     * @return
     */
    private static int compareDate(String endStr) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = df.format(date);
        try {
            Calendar calendar = Calendar.getInstance();
            int year = Integer.parseInt(endStr.substring(0, 4));
            int moth = Integer.parseInt(endStr.substring(4, 6));
            int day = Integer.parseInt(endStr.substring(6, 8));
            calendar.set(year, moth, day);
            Date endDate = calendar.getTime();
            Date dfDate = df.parse(dateStr);
            if (dfDate.getTime() > endDate.getTime()) {
                return 1;
            } else if (dfDate.getTime() < endDate.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    /**
     * 申请试用许可,许可可用将生成本地文件来存储试用许可以便启动桌面
     *
     * @param licenseService
     * @return
     */
    public static ApplyTrialLicenseResponse applyTrialLicense(LicenseService licenseService) {
        ServiceResponse<ApplyTrialLicenseResponse> response = null;
        try {
            ServiceResponse<QueryTrialLicenseResponse> queryTrialResult = licenseService.query(new QueryTrialLicenseRequest()
                    .productType(ProductType.ICLOUDMANAGER).version(Version.VERSION_8C));
            if (queryTrialResult.code == ServiceResponse.Code.SUCCESS) {
                //查询成功,获取许可状态
                //试用许可权限
                if (queryTrialResult.data.trial == true) {
                    //申请试用许可
                    ApplyTrialLicenseRequest request = new ApplyTrialLicenseRequest();
                    request.days = 90;
                    request.software.productType = ProductType.ICLOUDMANAGER;
                    request.software.version = Version.VERSION_8C;
                    request.machine.name = ComputerUtilities.getComputerName();
                    request.machine.macAddr = ComputerUtilities.getMac();
                    response = licenseService.apply(request);
                    //用于归还的试用许可信息
                    System.out.println(response.data.returnId);
                    System.out.println(response.data.license);
                }

            } else if (queryTrialResult.code == ServiceResponse.Code.LOGININ_OTHERPLAINT) {
                JOptionPaneUtilities.showMessageDialog(CommonProperties.getString("String_LoginOnOtherPath"));
            }
        } catch (IOException e) {
            JOptionPaneUtilities.showMessageDialog(CommonProperties.getString("String_ApplyTrialLicenseFalure"));
        }

        return response.data;
    }

    /**
     * 申请正式许可
     *
     * @param licenseService 自定义许可服务类
     * @param licenseId      查询得到的许可id
     * @return
     */
    public static ApplyFormalLicenseResponse applyFormalLicense(LicenseService licenseService, LicenseId licenseId) {
        ServiceResponse<ApplyFormalLicenseResponse> response = null;
        try {
            ApplyFormalLicenseRequest request = new ApplyFormalLicenseRequest();
            request.days = 2;
            request.licenseId = licenseId;
            request.software.productType = ProductType.ICLOUDMANAGER;
            request.software.version = Version.VERSION_8C;
            request.machine.name = ComputerUtilities.getComputerName();
            request.machine.macAddr = ComputerUtilities.getMac();
            response = licenseService.apply(request);
            System.out.println(response.data.returnId);
            System.out.println(response.data.license);
        } catch (IOException e) {
            JOptionPaneUtilities.showConfirmDialog(CommonProperties.getString("String_ApplyFormalLicenseFalure"));
        }
        return response.data;
    }

    /**
     * 归还试用许可
     *
     * @param licenseService
     * @param response
     */
    public static void returnTrialLicense(LicenseService licenseService, ApplyTrialLicenseResponse response) {
        try {
            licenseService.deleteTrialLicense(response.returnId);
            File file = new File(ONLINE_DIRCTORY);
            if (file.exists()) {
                file.delete();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * 归还正式许可
     *
     * @param licenseService
     * @param response
     */
    public static void returnFormLicense(LicenseService licenseService, ApplyFormalLicenseResponse response, LicenseId licenseId) {
        ReturnLicenseRequest request = new ReturnLicenseRequest();
        request.licenseId = licenseId;
        request.returnId = response.returnId;
        try {
            licenseService.returns(request);
            File file = new File(ONLINE_DIRCTORY);
            if (file.exists()) {
                file.delete();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * 创建许可文件
     *
     * @param licenseStr
     */
    public static File buildLicenseFile(String licenseStr) {
        FileOutputStream outPutStream = null;
        try {
            outPutStream = new FileOutputStream(ONLINE_LICENSEFILE);
            outPutStream.write(licenseStr.getBytes());
        } catch (FileNotFoundException e) {
            Application.getActiveApplication().getOutput().output(e);
        } catch (IOException e) {
            Application.getActiveApplication().getOutput().output(e);
        } finally {
            try {
                if (null != outPutStream) {
                    outPutStream.close();
                }
            } catch (IOException e) {
                Application.getActiveApplication().getOutput().output(e);
            }
        }
        return new File(ONLINE_LICENSEFILE);
    }

    /**
     * 查询许可id
     *
     * @param licenseService
     * @return
     */
    public static LicenseId getFormalLicenseId(LicenseService licenseService) {
        boolean hasFormalLicense = false;
        LicenseId licenseId = null;
        ServiceResponse<QueryFormalLicenseResponse> queryFormalResult = null;
        try {
            queryFormalResult = licenseService.query(new QueryFormalLicenseRequest()
                    .productType(ProductType.ICLOUDMANAGER).version(Version.VERSION_8C).pageCount(10));
            if (queryFormalResult.code == ServiceResponse.Code.SUCCESS) {
                //查询成功，获取许可状态
                //正式许可个数
                int licenseCount = queryFormalResult.data.licenseCount;
                if (licenseCount > 0) {
                    System.out.println(queryFormalResult.data.licenseCount);
                    int falseDays = 0;
                    for (int i = 0; i < licenseCount; i++) {
                        LicenseInfo licenseInfo = queryFormalResult.data.licenses[i];
                        System.out.println(licenseInfo.id);
                        System.out.println(licenseInfo.days);
                        int remainDays = licenseInfo.remainDays;
                        if (remainDays == 0) {
                            falseDays++;
                        }
                        System.out.println(licenseInfo.remainDays);
                        System.out.println(licenseInfo.snId);
                    }
                    if (falseDays == licenseCount) {
                        JOptionPaneUtilities.showMessageDialog(CommonProperties.getString("String_FormLicenseOverdue"));
                        hasFormalLicense = false;
                    } else {
                        hasFormalLicense = true;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPaneUtilities.showMessageDialog(CommonProperties.getString("String_PermissionCheckFailed"));
        }
        if (hasFormalLicense == true) {
            licenseId = queryFormalResult.data.licenses[0].id;
        }
        return licenseId;
    }
}
