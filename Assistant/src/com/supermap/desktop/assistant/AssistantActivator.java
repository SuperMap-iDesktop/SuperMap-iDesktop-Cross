package com.supermap.desktop.assistant;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.supermap.desktop.Application;

public class AssistantActivator implements BundleActivator {

	private static BundleContext CONTEXT;

	static BundleContext getContext() {
		return CONTEXT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === Assistant!!");
		setContext(bundleContext);

		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.Assistance", bundleContext.getBundle());
	}

	public static void setContext(BundleContext context) {
		AssistantActivator.CONTEXT = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		setContext(null);
	}

}
