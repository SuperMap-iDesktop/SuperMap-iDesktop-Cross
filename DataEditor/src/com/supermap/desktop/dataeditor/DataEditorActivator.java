package com.supermap.desktop.dataeditor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.MainFrame;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.LogUtilties;

public class DataEditorActivator implements BundleActivator {

	private static BundleContext CONTEXT;

	static BundleContext getContext() {
		return CONTEXT;
	}

	static void setContext(BundleContext context) {
		CONTEXT = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === DataEditor!!");
		setContext(bundleContext);
		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.DataEditor", bundleContext.getBundle());
		
//		System.out.println("Hello SuperMap === iDesktop!!");
//		LogUtilties.outPut(CoreProperties.getString("String_DesktopStartFinished"));
//
//
//		if (Application.getActiveApplication().getWorkspace() == null) {
//			System.exit(0);
//		} else {
//			MainFrame mainFrame = new MainFrame();
//			Application.getActiveApplication().setMainFrame(mainFrame);
//			mainFrame.loadUI();
//		}
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
