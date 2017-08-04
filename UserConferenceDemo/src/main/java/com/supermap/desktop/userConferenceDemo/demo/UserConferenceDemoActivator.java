package com.supermap.desktop.userConferenceDemo.demo;

import com.supermap.desktop.Application;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author XiaJT
 */
public class UserConferenceDemoActivator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.UserConferenceDemo", bundleContext.getBundle());
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

	}
}
