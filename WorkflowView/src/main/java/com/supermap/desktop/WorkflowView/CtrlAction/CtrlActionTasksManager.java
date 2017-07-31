package com.supermap.desktop.WorkflowView.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by highsad on 2017/7/14.
 */
public class CtrlActionTasksManager extends CtrlAction {
	private final static String TASKS = "com.supermap.desktop.WorkflowView.tasks.TasksManagerContainer";

	public CtrlActionTasksManager(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(TASKS)).setVisible(true);
		} catch (ClassNotFoundException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
