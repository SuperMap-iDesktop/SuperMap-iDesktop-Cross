package com.supermap.desktop.icloud;


import com.supermap.data.License;
import com.supermap.data.Toolkit;
import com.supermap.desktop.Application;
import com.supermap.desktop.icloud.api.LicenseService;
import com.supermap.desktop.icloud.commontypes.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.ComputerUtilities;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import com.supermap.desktop.utilities.PathUtilities;
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
    private static final String LIC_DIRCTORY = "../Configuration";
//    private static final String ONLINE_LICENSEFILE = ONLINE_DIRCTORY + ComputerUtilities.getComputerName() + "_8C.lic";



    /**
     * 获取指定相对路径的绝对路径
     *
     * @param pathName
     * @return
     */
    private static String getFullPathName(String pathName, boolean isFolder) {

        String result = System.getProperty("user.dir") + File.separator + "bin";
        try {
            String[] pathPrams = new String[]{result, pathName};
            result = combinePath(pathPrams, isFolder);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return result;
    }

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
     * 获取指定路径的上级路径
     *
     * @param pathName
     * @return
     */
    private  static String getParentPath(String pathName) {
        String pathNameTemp = pathName;
        String result = "";
        try {
            if (pathNameTemp != "") {
                pathNameTemp = pathNameTemp.replace("\\", "/");
                String[] splits = pathNameTemp.split("/");

                for (int i = 0; i < splits.length - 1; i++) {
                    result += splits[i];
                    result += "/";
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return result;
    }
    /**
     * 合并多个路径字符串
     *
     * @param paths    路径数组
     * @param isFolder 是否是文件夹路径
     * @return 合并后的路径
     */
    private  static String combinePath(String[] paths, boolean isFolder) {
        String result = "";
        try {
            if (paths.length > 0) {
                result = paths[0];
            }

            if (result.endsWith("/") || result.endsWith("\\")) {
                // do nothing
            } else {
                result += "/";
            }

            for (int i = 1; i < paths.length; i++) {
                if (paths[i] != null && paths[i] != "") {
                    if (paths[i].startsWith("/") || paths[i].startsWith("\\")) {
                        paths[i] = paths[i].substring(1, paths[i].length());
                    } else if (paths[i].startsWith("../") || paths[i].startsWith("..\\")) {
                        result = getParentPath(result);
                        paths[i] = paths[i].substring(3, paths[i].length());
                    }
                    result += paths[i];

                    if (result.endsWith("/") || result.endsWith("\\")) {
                        // do nothing
                    } else {
                        result += "/";
                    }
                }
            }

            if (!isFolder && (result.endsWith("/") || result.endsWith("\\"))) {
                result = result.substring(0, result.length() - 1);
            }
        } catch (Exception ex) {
            System.out.println(ex);
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
                    request.machine.macAddr = ComputerUtilities.getMACAddress();
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
            request.machine.macAddr = ComputerUtilities.getMACAddress();
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
            String fileName = getFullPathName(LIC_DIRCTORY,true)+"/"+ComputerUtilities.getComputerName() + "_8C.lic";
            File file = new File(fileName);
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
            String fileName = getFullPathName(LIC_DIRCTORY,true)+"/"+ComputerUtilities.getComputerName() + "_8C.lic";
            File file = new File(fileName);
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
        String fileName = getFullPathName(LIC_DIRCTORY,true)+"/"+ComputerUtilities.getComputerName() + "_8C.lic";
        try {
            outPutStream = new FileOutputStream(fileName);
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
        License.setSpecifyLicenseFilePath(fileName);
        return new File(fileName);
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
