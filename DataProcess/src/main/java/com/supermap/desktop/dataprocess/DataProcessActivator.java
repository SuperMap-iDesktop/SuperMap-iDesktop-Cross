package com.supermap.desktop.dataprocess;

import com.supermap.desktop.Application;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class DataProcessActivator implements BundleActivator {

	private static BundleContext CONTEXT;

	static BundleContext getContext() {
		return CONTEXT;
	}

	static void setContext(BundleContext context) {
		CONTEXT = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === DataProcess!!");
		setContext(bundleContext);
		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.DataProcess", bundleContext.getBundle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		setContext(null);
		System.out.println("Goodbye SuperMap === DataProcess!!");
	}

}
