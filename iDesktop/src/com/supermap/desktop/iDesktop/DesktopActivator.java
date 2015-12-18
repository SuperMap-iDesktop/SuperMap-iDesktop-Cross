package com.supermap.desktop.iDesktop;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.ui.UICommonToolkit;

public class DesktopActivator implements BundleActivator {

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
		
		DesktopActivator.setContext(bundleContext);		
		
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
	
	public static void setContext(BundleContext context){
		DesktopActivator.CONTEXT = context;
	}

}
