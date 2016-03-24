package com.supermap.desktop.core;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.exception.SmUncaughtExceptionHandler;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilties.LogUtilties;
import com.supermap.desktop.utilties.SplashScreenUtilties;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.SynchronousBundleListener;

public class CoreActivator implements BundleActivator {

	static {
		GlobalParameters.initResource();
	}

	ServiceRegistration<?> registration;
	CoreServiceTracker serviceTracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {

		// 设置没有被捕捉的异常的处理方法
		Thread.setDefaultUncaughtExceptionHandler(new SmUncaughtExceptionHandler());
		LogUtilties.outPut(LogUtilties.getSeparator());
		LogUtilties.outPut(CoreProperties.getString("String_DesktopStarting"));
		SplashScreenUtilties splashScreenUtiltiesInstance = SplashScreenUtilties.getSplashScreenUtiltiesInstance();
		if (splashScreenUtiltiesInstance != null) {
			SplashScreenUtilties.setBundleCount(context.getBundles().length);
			SplashScreenUtilties.resetCurrentCount();
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
				SplashScreenUtilties splashScreenUtiltiesInstance = SplashScreenUtilties.getSplashScreenUtiltiesInstance();
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
			this.registration = context.registerService(Application.class.getName(), new Application(), null);

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

		this.serviceTracker.close();
		this.registration.unregister();
	}
}
