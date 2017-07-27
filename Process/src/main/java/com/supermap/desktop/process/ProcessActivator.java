package com.supermap.desktop.process;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormWorkflow;
import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.NewWindowEvent;
import com.supermap.desktop.event.NewWindowListener;
import com.supermap.desktop.event.WorkflowInitListener;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.utilities.CursorUtilities;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.w3c.dom.Element;

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
		System.out.println("Hello SuperMap === process!!");
		setContext(bundleContext);
		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.process", bundleContext.getBundle());
		ProcessApplication.init();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		setContext(null);
		System.out.println("Goodbye SuperMap === process!!");
	}
}
