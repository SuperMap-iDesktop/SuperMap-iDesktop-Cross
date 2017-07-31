package com.supermap.desktop.WorkflowView.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.WorkflowView.FormWorkflow;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by highsad on 2017/2/28.
 */
public class CtrlActionRun extends CtrlAction {
	private final static String TASKS_CONTAINER = "com.supermap.desktop.WorkflowView.tasks.TasksManagerContainer";

	public CtrlActionRun(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			if (Application.getActiveApplication().getActiveForm() instanceof FormWorkflow) {
				IDockbar tasksContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(TASKS_CONTAINER));
				tasksContainer.setVisible(true);

				FormWorkflow formWorkflow = (FormWorkflow) Application.getActiveApplication().getActiveForm();
				formWorkflow.getTasksManager().run();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	@Override
	public boolean enable() {
		return Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm() instanceof FormWorkflow;
	}
}
