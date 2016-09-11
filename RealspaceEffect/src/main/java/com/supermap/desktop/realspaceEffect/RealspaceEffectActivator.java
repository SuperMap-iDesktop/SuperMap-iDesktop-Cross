package com.supermap.desktop.realspaceEffect;

import com.supermap.desktop.Application;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RealspaceEffectActivator implements BundleActivator {

	private static BundleContext CONTEXT;

	static BundleContext getContext() {
		return CONTEXT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === RealspaceEffect!!");
		
		RealspaceEffectActivator.CONTEXT = bundleContext;

		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.RealspaceEffect", bundleContext.getBundle());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		RealspaceEffectActivator.CONTEXT = null;
		System.out.println("Goodbye SuperMap === RealspaceEffect!!");
	}

}
