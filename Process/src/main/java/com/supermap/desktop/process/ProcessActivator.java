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
	private static final String processTreeClassName = "com.supermap.desktop.process.core.ProcessManager";
	private static final String ParameterManagerClassName = "com.supermap.desktop.process.ParameterManager";

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
		Application.getActiveApplication().setWorkflowInitListener(new WorkflowInitListener() {
			@Override
			public IWorkflow init(Element element) {
				String name = element.getAttribute("name");
				Workflow workflow = new Workflow(name);
				workflow.serializeFrom(element.getAttribute("value"));
				return workflow;
			}
		});
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
			IFormWorkflow formProcess = showProcess(evt.getNewWindowName());
			evt.setNewWindow(formProcess);
		}
	}

	private IFormWorkflow showProcess(String newWindowName) {
		FormWorkflow formWorkflow = null;

		try {
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
			for (int i = 0; i < formManager.getCount(); i++) {
				if (formManager.get(i).getWindowType() == WindowType.WORK_FLOW && formManager.get(i).getText().equals(newWindowName)) {
					if (formManager.getActiveForm() != formManager.get(i)) {
						formManager.setActiveForm(formManager.get(i));
					}
					return null;
				}
			}

			CursorUtilities.setWaitCursor();
			ArrayList<IWorkflow> workFlows = Application.getActiveApplication().getWorkflows();
			for (IWorkflow workFlow : workFlows) {
				if (workFlow.getName().equals(newWindowName)) {
					formWorkflow = new FormWorkflow(workFlow);
					break;
				}
			}
			if (formWorkflow == null) {
				formWorkflow = new FormWorkflow(newWindowName);
			}
			formManager.showChildForm(formWorkflow);
			Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(processTreeClassName)).setVisible(true);
			Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(ParameterManagerClassName)).setVisible(true);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			CursorUtilities.setDefaultCursor();
		}

		return formWorkflow;
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
