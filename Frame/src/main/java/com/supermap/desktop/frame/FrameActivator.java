package com.supermap.desktop.frame;


import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.MainFrame;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.LogUtilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

import java.util.Timer;
import java.util.TimerTask;

public class FrameActivator implements BundleActivator {

    private boolean isError = false;
    private boolean isFrameStarted = false;

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Hello SuperMap === Frame!!");
        try {

            startUp(bundleContext);

        } catch (Exception e) {
            UICommonToolkit.showMessageDialog(CommonProperties.getString("String_PermissionCheckFailed"));
            isError = true;
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