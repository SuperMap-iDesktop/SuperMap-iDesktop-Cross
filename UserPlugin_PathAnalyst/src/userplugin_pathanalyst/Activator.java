package userplugin_pathanalyst;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.supermap.desktop.Application;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public static void setContext(BundleContext context) {
		Activator.context = context;
	}

	public static BundleContext getContext() {
		return Activator.context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		setContext(bundleContext);
		Application.getActiveApplication().getPluginManager().addPlugin("UserPlugin_PathAnalyst", bundleContext.getBundle());
		System.out.println("PathAnalystCtraAction Running!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		setContext(null);
	}

}
