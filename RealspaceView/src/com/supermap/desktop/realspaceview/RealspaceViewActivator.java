package com.supermap.desktop.realspaceview;

import java.awt.Cursor;

import javax.swing.JFrame;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormScene;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.NewWindowEvent;
import com.supermap.desktop.event.NewWindowListener;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.CommonToolkit;

public class RealspaceViewActivator implements BundleActivator {

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
		System.out.println("Hello SuperMap === RealspaceView!!");

		RealspaceViewActivator.setContext(bundleContext);

		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.RealspaceView", bundleContext.getBundle());

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

			if (type == WindowType.SCENE) {
				IFormScene formScene = showScene(evt.getNewWindowName());
				evt.setNewWindow(formScene);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public IFormScene showScene(String name) {
		String currentName = name;
		JFrame mainFrame = (JFrame) Application.getActiveApplication().getMainFrame();
		mainFrame.setCursor(Cursor.WAIT_CURSOR);
		IFormScene formScene = null;

		try {
			IForm form = CommonToolkit.FormWrap.getForm(currentName, WindowType.SCENE);
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();

			if (form == null) {
				if (currentName == null || currentName.length() == 0) {
					currentName = CommonToolkit.SceneWrap.getAvailableSceneName(CoreProperties.getString("String_WorkspaceNodeCaptionScene"), true);
				}

				formScene = new FormScene(currentName);
				if (formManager.getActiveForm() != null) {
					formManager.setActiveForm(formScene);
				}
				formManager.showChildForm(formScene);
			} else {
				formScene = (IFormScene) form;
				formManager.setActiveForm(formScene);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		mainFrame.setCursor(Cursor.DEFAULT_CURSOR);

		return formScene;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		RealspaceViewActivator.setContext(null);
	}
	
	private static void setContext(BundleContext context) {
		RealspaceViewActivator.CONTEXT = context;
	}

}
