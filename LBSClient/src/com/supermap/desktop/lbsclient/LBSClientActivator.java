package com.supermap.desktop.lbsclient;

import java.awt.Cursor;
import javax.swing.JFrame;
import org.osgi.framework.*;
import com.supermap.desktop.*;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.dialog.FormLBSControl;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.*;

public class LBSClientActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		LBSClientActivator.context = bundleContext;
		
		System.out.println("Hello SuperMap === LBSClient!!");
		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.LBSClient", bundleContext.getBundle());
		CommonToolkit.FormWrap.addNewWindowListener(new NewWindowListener() {
			@Override
			public void newWindow(NewWindowEvent evt) {
				newWindowEvent(evt);
			}
		});
	}
	private void newWindowEvent(NewWindowEvent evt) {
		try {
			WindowType type = evt.getNewWindowType();

			if (type == WindowType.LBSCONTROL) {
				IFormLBSControl formMap = showLBSControl(evt.getNewWindowName());
				evt.setNewWindow(formMap);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public IFormLBSControl showLBSControl(String name) {
		String nameTemp = name;
		JFrame mainFrame = (JFrame) Application.getActiveApplication().getMainFrame();
		mainFrame.setCursor(Cursor.WAIT_CURSOR);
		IFormLBSControl formMap = null;
		try {
			IForm form = CommonToolkit.FormWrap.getForm(nameTemp, WindowType.LBSCONTROL);
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();

			if (form == null) {
				formMap = new FormLBSControl(nameTemp);
				formManager.showChildForm(formMap);
			} else {
				formMap = (IFormLBSControl) form;
				formManager.setActiveForm(formMap);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		mainFrame.setCursor(Cursor.DEFAULT_CURSOR);

		return formMap;
	}
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		LBSClientActivator.context = null;
	}

}
