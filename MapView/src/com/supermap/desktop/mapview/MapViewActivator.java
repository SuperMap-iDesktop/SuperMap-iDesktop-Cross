package com.supermap.desktop.mapview;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.NewWindowEvent;
import com.supermap.desktop.event.NewWindowListener;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilties.MapUtilties;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.swing.*;
import java.awt.*;

public class MapViewActivator implements BundleActivator {

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
		System.out.println("Hello SuperMap === MapView!!");
		setContext(bundleContext);
		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.MapView", bundleContext.getBundle());

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

			if (type == WindowType.MAP) {
				IFormMap formMap = showMap(evt.getNewWindowName());
				evt.setNewWindow(formMap);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public IFormMap showMap(String name) {
		String nameTemp = name;
		JFrame mainFrame = (JFrame) Application.getActiveApplication().getMainFrame();
		mainFrame.setCursor(Cursor.WAIT_CURSOR);
		IFormMap formMap = null;
		try {
			IForm form = CommonToolkit.FormWrap.getForm(nameTemp, WindowType.MAP);
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();

			if (form == null) {
				if (nameTemp == null || nameTemp.length() == 0) {
					nameTemp = MapUtilties.getAvailableMapName(CoreProperties.getString("String_WorkspaceNodeCaptionMap"), true);
				}

				formMap = new FormMap(nameTemp);
				formManager.showChildForm(formMap);
			} else {
				formMap = (IFormMap) form;
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
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		setContext(null);
	}

}
