package com.supermap.desktop.icloud;


import com.supermap.data.License;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.icloud.api.LicenseService;
import com.supermap.desktop.icloud.commontypes.*;
import com.supermap.desktop.utilities.ComputerUtilities;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xie on 2016/12/23.
 * 许可管理类
 */
public class LicenseManager {
    private static final String LIC_DIRCTORY = "C:\\Program Files\\Common Files\\SuperMap\\License";
    private static final String LINUX_LIC_DIRCTORY = "/opt/License";
    private static boolean hasOffLineLicense = false;

    /**
     * 获取离线许可文件
     *
     * @return
     */
    public static File getOffLineLicense() {
        File result = null;
        File licDir;
        if (SystemPropertyUtilities.isWindows()) {
            licDir = new File(LIC_DIRCTORY);
        } else {
            //linux系统暂不处理
            licDir = new File(LINUX_LIC_DIRCTORY);
        }
        if (licDir.exists() && licDir.isDirectory()) {
            File[] files = licDir.listFiles();
            int size = files.length;
            if (size > 0 && files[0].getName().endsWith("lic")) {
                result = files[0];
                hasOffLineLicense = true;
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
        com.supermap.data.ProductType[] productTypes = {com.supermap.data.ProductType.IOBJECTS_ADDRESS_MATCHING_DEVELOP, com.supermap.data.ProductType.IOBJECTS_CHART_DEVELOP,
                com.supermap.data.ProductType.IOBJECTS_CORE_DEVELOP, com.supermap.data.ProductType.IOBJECTS_LAYOUT_DEVELOP, com.supermap.data.ProductType.IOBJECTS_NETWORK_DEVELOP,
                com.supermap.data.ProductType.IOBJECTS_REALSPACE_EFFECT_DEVELOP, com.supermap.data.ProductType.IOBJECTS_REALSPACE_NETWORK_ANALYST_DEVELOP,
                com.supermap.data.ProductType.IOBJECTS_REALSPACE_SPATIAL_ANALYST_DEVELOP, com.supermap.data.ProductType.IOBJECTS_SPACE_DEVELOP,
                com.supermap.data.ProductType.IOBJECTS_SPATIAL_DEVELOP, com.supermap.data.ProductType.IOBJECTS_TOPOLOGY_DEVELOP, com.supermap.data.ProductType.IOBJECTS_TRAFFIC_ANALYST_DEVELOP};
        int length = productTypes.length;
        int licenseCount = 0;
        for (int i = 0; i < length; i++) {
            int valite = license.connect(productTypes[i]);
            if (valite == 0) {
                licenseCount++;
            }
        }
        if (licenseCount == length) {
            valitedLicense = true;
        }
        return valitedLicense;
    }

    /**
     * 判断离线许可是否过期
     *
     * @return
     */
    public static boolean isOffLineLicenseOverdue() {
        boolean result = false;
        if (hasOffLineLicense) {
            File licFile = getOffLineLicense();
            FileInputStream stream = null;
            BufferedReader br;
            try {
                stream = new FileInputStream(licFile);
                br = new BufferedReader(new InputStreamReader(stream));
                String tempstr = "";
                while ((tempstr = br.readLine()) != null) {
                    if (tempstr.contains("<end>")) {
                        tempstr = tempstr.substring(tempstr.indexOf(">") + 1, tempstr.lastIndexOf("<"));
                        break;
                    }
                }
                if (compareDate(tempstr) == 1) {
                    result = true;
                }
            } catch (IOException e) {
                Application.getActiveApplication().getOutput().output(e);
            } finally {
                try {
                    if (null != stream) {
                        stream.close();
                    }
                } catch (IOException e) {
                    Application.getActiveApplication().getOutput().output(e);
                }
            }
        }
        return result;
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
     * 是否存在离线许可
     *
     * @return
     */
    public static boolean hasOffLineLicense() {
        return getOffLineLicense() != null;
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
