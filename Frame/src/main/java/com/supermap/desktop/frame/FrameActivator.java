package com.supermap.desktop.frame;


import com.supermap.data.License;
import com.supermap.data.ProductType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.MainFrame;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.icloud.CloudLicenseDialog;
import com.supermap.desktop.ui.icloud.api.LicenseService;
import com.supermap.desktop.ui.icloud.commontypes.ApplyFormalLicenseResponse;
import com.supermap.desktop.ui.icloud.commontypes.ApplyTrialLicenseResponse;
import com.supermap.desktop.utilities.LogUtilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class FrameActivator implements BundleActivator {

    private boolean isError = false;
    private boolean isFrameStarted = false;
    private LicenseService service;
    private ApplyFormalLicenseResponse formLicenseResponse;
    private ApplyTrialLicenseResponse trialLicenseResponse;

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Hello SuperMap === Frame!!");
        try {
	        startUp(bundleContext);
            //本机许可是否满足java版本需求
//            if (valiteLicense()) {
//                //yes
//                startUp(bundleContext);
//            } else {
//                //no
//                //是否登录云许可
//                confirmOnlineLicense(bundleContext, CommonProperties.getString("String_LoginOnlineWhenOffLicense"));
//            }

        } catch (Exception e) {
            UICommonToolkit.showMessageDialog(CommonProperties.getString("String_PermissionCheckFailed"));
            isError = true;
        }
    }

    private boolean valiteLicense() {
        boolean valitedLicense = false;
        License license = new License();
        /**
         * JAVA开发版本中用到了许可类型，只用全部满足时才为true
         */
        ProductType[] productTypes = {ProductType.IOBJECTS_ADDRESS_MATCHING_DEVELOP, ProductType.IOBJECTS_CHART_DEVELOP,
                ProductType.IOBJECTS_CORE_DEVELOP, ProductType.IOBJECTS_LAYOUT_DEVELOP, ProductType.IOBJECTS_NETWORK_DEVELOP,
                ProductType.IOBJECTS_REALSPACE_EFFECT_DEVELOP, ProductType.IOBJECTS_REALSPACE_NETWORK_ANALYST_DEVELOP,
                ProductType.IOBJECTS_REALSPACE_SPATIAL_ANALYST_DEVELOP, ProductType.IOBJECTS_SPACE_DEVELOP,
                ProductType.IOBJECTS_SPATIAL_DEVELOP, ProductType.IOBJECTS_TOPOLOGY_DEVELOP, ProductType.IOBJECTS_TRAFFIC_ANALYST_DEVELOP};
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

    private void confirmOnlineLicense(BundleContext bundleContext, String message) {
        try {
            if (UICommonToolkit.showConfirmDialog(message) == JOptionPane.OK_OPTION) {
                //登录
                CloudLicenseDialog dialog = new CloudLicenseDialog();
                service = dialog.getLicenseService();
                if (dialog.showDialog() == DialogResult.OK) {
                    //许可申请成功
                    formLicenseResponse = dialog.getFormalLicenseResponse();
                    trialLicenseResponse = dialog.getTrialLicenseResponse();
                    if (null != formLicenseResponse) {
//                    LicenseManager.buildLicense(formLicenseResponse.license);
                        startUp(bundleContext);
                    } else if (null != trialLicenseResponse) {
//                    LicenseManager.buildLicense(trialLicenseResponse.license);
                        startUp(bundleContext);
                    } else {
                        //不登陆
                        stop(bundleContext);
                        System.exit(1);
                        return;
                    }
                }
            } else {
                //不登陆
                stop(bundleContext);
                System.exit(1);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //启动桌面
    private void startUp(BundleContext bundleContext) {
        bundleContext.addFrameworkListener(new FrameworkListener() {
            @Override
            public void frameworkEvent(FrameworkEvent frameworkEvent) {
                if (frameworkEvent.getType() == FrameworkEvent.STARTED) {
                    FrameActivator.this.isFrameStarted = true;
                }
            }
        });
        // 在插件启动的过程中去结束进程会导致 OSGI 主框架的退出，由于 OSGI 主框架需要等待框架启动完成并执行完框架的初始化方法，从而导致挂起。
        // 因此在此使用一个 timer 监视器来监测插件和框架的状态，并在合适的时候结束程序
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (FrameActivator.this.isFrameStarted) {
                    if (FrameActivator.this.isError) {
                        System.exit(1);
                    } else {
                        timer.cancel();
                    }
                }
            }
        }, 500, 500);

        Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.Frame", bundleContext.getBundle());
        LogUtilities.outPut(CoreProperties.getString("String_DesktopStartFinished"));
        MainFrame mainFrame = new MainFrame();
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (null != service && null != trialLicenseResponse) {
                    try {
                        //退出产品时，归还试用许可
                        service.deleteTrialLicense(trialLicenseResponse.returnId);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        Application.getActiveApplication().setMainFrame(mainFrame);
        mainFrame.loadUI();
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Goodbye SuperMap === Frame!!");
    }

}