package com.supermap.desktop.spatialanalyst;

import com.supermap.desktop.Application;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class SpatialAnalystActivator implements BundleActivator {

	private static BundleContext CONTEXT;

	static BundleContext getContext() {
		return CONTEXT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === SpatialAnalyst!!");
		
		SpatialAnalystActivator.setContext(bundleContext);

		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.SpatialAnalyst", bundleContext.getBundle());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		SpatialAnalystActivator.setContext(null);
		System.out.println("Goodbye SuperMap === SpatialAnalyst!!");
	}

	public static void setContext(BundleContext context){
		SpatialAnalystActivator.CONTEXT = context;
	}
}
