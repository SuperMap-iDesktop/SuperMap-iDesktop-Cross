package com.supermap.desktop.core;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.exception.SmUncaughtExceptionHandler;
import com.supermap.desktop.icloud.CloudLicenseDialog;
import com.supermap.desktop.icloud.LicenseManager;
import com.supermap.desktop.icloud.api.LicenseService;
import com.supermap.desktop.icloud.commontypes.ApplyFormalLicenseResponse;
import com.supermap.desktop.icloud.commontypes.ApplyTrialLicenseResponse;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.LogUtilities;
import com.supermap.desktop.utilities.SplashScreenUtilities;
import org.osgi.framework.*;

import java.io.IOException;

public class CoreActivator implements BundleActivator {

    static {
        GlobalParameters.initResource();
    }

    ServiceRegistration<?> registration;
    CoreServiceTracker serviceTracker;
    private LicenseService service;
    private ApplyFormalLicenseResponse formLicenseResponse;
    private ApplyTrialLicenseResponse trialLicenseResponse;

    /*
     * (non-Javadoc)
     *
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
     */
    @Override
    public void start(final BundleContext context) throws Exception {
        //本机许可是否满足java版本需求
        if (LicenseManager.valiteLicense()) {
            //yes
            startUp(context);
        } else {
            //判断()试用许可
            //no
            if (LicenseManager.hasOffLineLicense()) {
                //是否有离线许可
                //yes
                if (LicenseManager.isOffLineLicenseOverdue()) {
                    //离线许可是否过期
                    //yes
                    loginOnlineLicense(context);
                } else {
                    //no
                    startUp(context);
                }
            } else {
                //没有离线许可，登录云许可
                //no
                loginOnlineLicense(context);
            }
        }

    }

    /**
     * 登录云许可
     *
     * @param bundleContext
     */
    private void loginOnlineLicense(BundleContext bundleContext) {
        try {
//            if (UICommonToolkit.showConfirmDialog(message) == JOptionPane.OK_OPTION) {
            //登录
            CloudLicenseDialog dialog = new CloudLicenseDialog();
            if (dialog.showDialog() == CloudLicenseDialog.DIALOGRESULT_OK) {
                service = dialog.getLicenseService();
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
//                }
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

    private void startUp(final BundleContext context) {
        // 设置没有被捕捉的异常的处理方法
        Thread.setDefaultUncaughtExceptionHandler(new SmUncaughtExceptionHandler());
        LogUtilities.outPut(LogUtilities.getSeparator());
        LogUtilities.outPut(CoreProperties.getString("String_DesktopStarting"));
        SplashScreenUtilities splashScreenUtiltiesInstance = SplashScreenUtilities.getSplashScreenUtiltiesInstance();
        if (splashScreenUtiltiesInstance != null) {
            SplashScreenUtilities.setBundleCount(context.getBundles().length);
            SplashScreenUtilities.resetCurrentCount();
            Bundle bundle = context.getBundle();
            String name = bundle.getSymbolicName();
            if (name == null) {
                name = "unname";
            }
            String info = "Loading " + name + ".jar";
            splashScreenUtiltiesInstance.update(info);
        }


        context.addBundleListener(new SynchronousBundleListener() {
            @Override
            public void bundleChanged(BundleEvent bundleEvent) {
                if (bundleEvent.getType() != BundleEvent.STARTING) {
                    return;
                }
                SplashScreenUtilities splashScreenUtiltiesInstance = SplashScreenUtilities.getSplashScreenUtiltiesInstance();
                if (splashScreenUtiltiesInstance != null) {
                    Bundle bundle = bundleEvent.getBundle();
                    String name = bundle.getSymbolicName();
                    if (name == null) {
                        name = "unname";
                    }
                    String info = "Loading " + name + ".jar";
                    splashScreenUtiltiesInstance.update(info);
                }
                if (bundleEvent.getBundle() == context.getBundles()[context.getBundles().length - 1]) {
                    context.removeBundleListener(this);
                }
            }
        });
        System.out.println("Hello SuperMap === Core!!");

        // 不知道为什么，core会被加载两次，暂时先这么处理
        if (Application.getActiveApplication() == null || Application.getActiveApplication().getPluginManager().getBundle("SuperMap.Desktop.Core") == null) {
            this.serviceTracker = new CoreServiceTracker(context);
            this.serviceTracker.open();
//			this.registration = context.registerService(Application.class.getName(), new Application(), null);

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Goodbye SuperMap === Core!!");
        if (null != service && null != trialLicenseResponse) {
            try {
                //退出产品时，归还试用许可
                service.deleteTrialLicense(trialLicenseResponse.returnId);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        this.serviceTracker.close();
//		this.registration.unregister();
    }
}
