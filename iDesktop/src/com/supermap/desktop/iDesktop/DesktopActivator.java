package com.supermap.desktop.iDesktop;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.ui.MainFrame;
import com.supermap.desktop.ui.UICommonToolkit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class DesktopActivator implements BundleActivator {


	private static final Log log = LogFactory.getLog(DesktopActivator.class);
	
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
		System.out.println("Hello SuperMap === iDesktop!!");
		log.info("desktopActivator start!");
		DesktopActivator.setContext(bundleContext);
		MainFrame mainFrame = new MainFrame();
		Application.getActiveApplication().setMainFrame(mainFrame);

		IFormMain formMain = Application.getActiveApplication().getMainFrame();
		if (formMain == null) {
			UICommonToolkit.showMessageDialog(DesktopProperties.getString("PermissionCheckFailed"));
			System.exit(0);
		} else {
			formMain.loadUI();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		DesktopActivator.setContext(null);
	}

	public static void setContext(BundleContext context) {
		DesktopActivator.CONTEXT = context;
	}

}
