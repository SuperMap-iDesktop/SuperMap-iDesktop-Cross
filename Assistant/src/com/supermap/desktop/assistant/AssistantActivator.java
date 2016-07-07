package com.supermap.desktop.assistant;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.FormTransformation;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.NewWindowEvent;
import com.supermap.desktop.event.NewWindowListener;
import com.supermap.desktop.utilities.StringUtilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AssistantActivator implements BundleActivator {

	private static BundleContext CONTEXT;

	static BundleContext getContext() {
		return CONTEXT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === Assistant!!");
		setContext(bundleContext);

		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.Assistance", bundleContext.getBundle());
		CommonToolkit.FormWrap.addNewWindowListener(new NewWindowListener() {
			@Override
			public void newWindow(NewWindowEvent evt) {
				newWindowEvent(evt);
			}
		});
	}

	private void newWindowEvent(NewWindowEvent evt) {
		WindowType type = evt.getNewWindowType();
		if (type == WindowType.TRANSFORMATION) {
			evt.setNewWindow(getNewWindowTransformation(evt.getNewWindowName()));
		}
	}

	private IForm getNewWindowTransformation(String name) {
		JFrame mainFrame = (JFrame) Application.getActiveApplication().getMainFrame();
		mainFrame.setCursor(Cursor.WAIT_CURSOR);
		IFormTransformation formTransformation = null;
		try {
			IForm form = CommonToolkit.FormWrap.getForm(name, WindowType.TRANSFORMATION);
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();

			if (form == null) {
				if (StringUtilities.isNullOrEmpty(name)) {
					name = AssistantProperties.getString("String_transformationFormTitle");
					ArrayList<String> names = new ArrayList<>();
					for (int i = 0; i < formManager.getCount(); i++) {
						names.add(formManager.get(i).getText());
					}
					name = StringUtilities.getUniqueName(name, names);
				}

				formTransformation = new FormTransformation(name);
				if (formManager.getActiveForm() != null) {
					formManager.setActiveForm(formTransformation);
				}
				formManager.showChildForm(formTransformation);
			} else {
				formTransformation = (IFormTransformation) form;
				formManager.setActiveForm(formTransformation);
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		mainFrame.setCursor(Cursor.DEFAULT_CURSOR);

		return formTransformation;
	}

	public static void setContext(BundleContext context) {
		AssistantActivator.CONTEXT = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		setContext(null);
	}

}
