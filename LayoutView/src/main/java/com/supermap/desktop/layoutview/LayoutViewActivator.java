package com.supermap.desktop.layoutview;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.FormLayout;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.NewWindowEvent;
import com.supermap.desktop.event.NewWindowListener;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.LayoutUtilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.swing.*;
import java.awt.*;

public class LayoutViewActivator implements BundleActivator {

	private static BundleContext CONTEXT;

	static BundleContext getContext() {
		return CONTEXT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === LayoutView!!");
		LayoutViewActivator.setContext(bundleContext);

		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.LayoutView", bundleContext.getBundle());
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
			if (type == WindowType.LAYOUT) {

				IFormLayout formLayout = showLayout(evt.getNewWindowName());
				evt.setNewWindow(formLayout);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public IFormLayout showLayout(String name) {
		String currentName = name;
		JFrame mainFrame = (JFrame) Application.getActiveApplication().getMainFrame();
		mainFrame.setCursor(Cursor.WAIT_CURSOR);
		IFormLayout formLayout = null;

		try {
			IForm form = CommonToolkit.FormWrap.getForm(currentName, WindowType.LAYOUT);
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();

			if (form == null) {
				if (currentName == null || currentName.length() == 0) {
					currentName = LayoutUtilities.getAvailableLayoutName(CoreProperties.getString("String_WorkspaceNodeCaptionLayout"), true);
				}

				formLayout = new FormLayout(currentName);
				if (formManager.getActiveForm() != null) {
					formManager.setActiveForm(formLayout);
				}
				formManager.showChildForm(formLayout);
			} else {
				formLayout = (IFormLayout) form;
				formManager.setActiveForm(formLayout);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		mainFrame.setCursor(Cursor.DEFAULT_CURSOR);

		return formLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LayoutViewActivator.setContext(null);
		System.out.println("Goodbye SuperMap === LayoutView!!");
	}

	public static void setContext(BundleContext context){
		LayoutViewActivator.CONTEXT = context;
	}
}
