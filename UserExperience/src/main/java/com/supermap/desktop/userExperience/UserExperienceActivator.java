package com.supermap.desktop.userExperience;

import com.supermap.desktop.Application;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author XiaJT
 */
public class UserExperienceActivator implements BundleActivator {

	private static BundleContext CONTEXT;

	public static BundleContext getContext() {
		return CONTEXT;
	}

	public static void setContext(BundleContext context) {
		UserExperienceActivator.CONTEXT = context;
	}

	/*
		 * (non-Javadoc)
		 *
		 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
		 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === UserExperience!!");

		UserExperienceActivator.setContext(bundleContext);

		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.UserExperience", bundleContext.getBundle());

		UserExperienceManager.getInstance().start();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		UserExperienceManager.getInstance().stop();
		UserExperienceActivator.setContext(null);
		System.out.println("Goodbye SuperMap === UserExperience!!");
	}


}
