package com.supermap.desktop.lbsclient;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.supermap.desktop.Application;

public class LBSClientActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		LBSClientActivator.context = bundleContext;
		
		System.out.println("Hello SuperMap === LBSClient!!");
		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.LBSClient", bundleContext.getBundle());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		LBSClientActivator.context = null;
	}

}
