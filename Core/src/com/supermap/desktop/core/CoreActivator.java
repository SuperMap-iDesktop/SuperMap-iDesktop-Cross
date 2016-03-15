package com.supermap.desktop.core;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.utilties.SplashScreenUtilties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	private static Log log = LogFactory.getLog(CoreActivator.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
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
		log.info("CoreActivator Started!");

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
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						System.err.println(e);
//					}
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
