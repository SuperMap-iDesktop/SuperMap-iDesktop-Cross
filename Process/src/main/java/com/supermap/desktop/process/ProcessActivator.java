package com.supermap.desktop.process;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormProcess;
import com.supermap.desktop.Interface.IWorkFlow;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.NewWindowEvent;
import com.supermap.desktop.event.NewWindowListener;
import com.supermap.desktop.utilities.CursorUtilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;

/**
 * Created by highsad on 2016/12/19.
 */
public class ProcessActivator implements BundleActivator {

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
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === Process!!");
		setContext(bundleContext);
		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.Process", bundleContext.getBundle());
		CommonToolkit.FormWrap.addNewWindowListener(new NewWindowListener() {
			@Override
			public void newWindow(NewWindowEvent evt) {
				newWindowEvent(evt);
			}
		});
	}

	private void newWindowEvent(NewWindowEvent evt) {
		WindowType type = evt.getNewWindowType();
		if (type == WindowType.WORK_FLOW) {
			IFormProcess formProcess = showProcess(evt.getNewWindowName());
			evt.setNewWindow(formProcess);
		}
	}

	private IFormProcess showProcess(String newWindowName) {
		FormProcess formProcess = null;
		CursorUtilities.setWaitCursor();
		ArrayList<IWorkFlow> workFlows = Application.getActiveApplication().getWorkFlows();
		for (IWorkFlow workFlow : workFlows) {
			if (workFlow.getName().equals(newWindowName)) {
				formProcess = new FormProcess(workFlow);
				break;
			}
		}
		if (formProcess == null) {
			formProcess = new FormProcess(newWindowName);
		}
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
		formManager.showChildForm(formProcess);
		CursorUtilities.setDefaultCursor();
		return formProcess;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		setContext(null);
		System.out.println("Goodbye SuperMap === Process!!");
	}
}
