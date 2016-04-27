package com.supermap.desktop.iDesktop;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.MainFrame;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.LogUtilties;
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

	static void setContext(BundleContext context) {
		CONTEXT = context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {

		System.out.println("Hello SuperMap === iDesktop!!");
		LogUtilties.outPut(CoreProperties.getString("String_DesktopStartFinished"));
		DesktopActivator.setContext(bundleContext);


		if (Application.getActiveApplication().getWorkspace() == null) {
			UICommonToolkit.showMessageDialog(DesktopProperties.getString("PermissionCheckFailed"));
			System.exit(0);
		} else {
//			UICommonToolkit.showMessageDialog(DesktopProperties.getString("PermissionCheckFailed"));
			MainFrame mainFrame = new MainFrame();
			Application.getActiveApplication().setMainFrame(mainFrame);
			mainFrame.loadUI();
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

}
