package com.supermap.desktop.lbsclient;

//import com.supermap.desktop.Application;
//import com.supermap.desktop.CommonToolkit;
//import com.supermap.desktop.Interface.IForm;
//import com.supermap.desktop.Interface.IFormLayout;
//import com.supermap.desktop.Interface.IFormManager;
//import com.supermap.desktop.enums.WindowType;
//import com.supermap.desktop.event.NewWindowEvent;
//import com.supermap.desktop.event.NewWindowListener;
//import com.supermap.desktop.properties.CoreProperties;
//import com.supermap.desktop.utilties.LayoutUtilties;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.event.NewWindowEvent;
import com.supermap.desktop.event.NewWindowListener;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.MainFrame;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.LogUtilties;

public class LBSClientActivator implements BundleActivator {

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
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === LBSClient!!");
		setContext(bundleContext);
		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.LBSClient", bundleContext.getBundle());
		
		System.out.println("Hello SuperMap === iDesktop!!");
		LogUtilties.outPut(CoreProperties.getString("String_DesktopStartFinished"));

		// 临时代码
		if (Application.getActiveApplication().getWorkspace() == null) {
			UICommonToolkit.showMessageDialog("许可不可用");
			System.exit(0);
		} else {
			MainFrame mainFrame = new MainFrame();
			Application.getActiveApplication().setMainFrame(mainFrame);
			mainFrame.loadUI();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		setContext(null);
	}

}
