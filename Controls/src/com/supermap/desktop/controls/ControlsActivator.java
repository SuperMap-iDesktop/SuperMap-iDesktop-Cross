package com.supermap.desktop.controls;

import com.supermap.desktop.Application;
import com.supermap.desktop.core.CoreServiceTracker;
import com.supermap.desktop.dialog.OptionPaneImpl;
import com.supermap.desktop.utilties.JOptionPaneUtilties;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ControlsActivator implements BundleActivator {
	ServiceRegistration<?> registration = null;
	CoreServiceTracker serviceTracker = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Hello SuperMap === Control!!");
		JOptionPaneUtilties.setiOptionPane(new OptionPaneImpl());
		if (Application.getActiveApplication() == null || Application.getActiveApplication().getPluginManager().getBundle("SuperMap.Desktop.Controls") == null) {
			this.serviceTracker = new CoreServiceTracker(context);
			this.serviceTracker.open();
//			this.registration = context.registerService(Application.class.getName(), new Application(), null);

			Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.Controls", context.getBundle());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Goodbye SuperMap === Control!!");
		this.serviceTracker.close();
//		this.registration.unregister();
	}

}
