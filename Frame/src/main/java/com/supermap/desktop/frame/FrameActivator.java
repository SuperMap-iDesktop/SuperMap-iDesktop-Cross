package com.supermap.desktop.frame;


import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.MainFrame;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.LogUtilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class FrameActivator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === Frame!!");
		try {
			Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.Frame", bundleContext.getBundle());
		} catch (Exception e) {
			UICommonToolkit.showMessageDialog(FrameProperties.getString("PermissionCheckFailed"));
			System.exit(0);
		}

		LogUtilities.outPut(CoreProperties.getString("String_DesktopStartFinished"));
		MainFrame mainFrame = new MainFrame();
		Application.getActiveApplication().setMainFrame(mainFrame);
		mainFrame.loadUI();

	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Goodbye SuperMap === Frame!!");
	}

}