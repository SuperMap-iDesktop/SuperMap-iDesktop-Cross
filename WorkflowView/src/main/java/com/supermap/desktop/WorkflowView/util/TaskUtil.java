package com.supermap.desktop.WorkflowView.util;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.WorkflowView.ParameterManager;
import com.supermap.desktop.WorkflowView.tasks.TasksManagerContainer;

/**
 * Created by xie on 2017/2/28.
 */
public class TaskUtil {
	/**
	 * Utilities class,no public construction method
	 */
	private TaskUtil() {

	}

	private static final String TASKMANAGER = "com.supermap.desktop.WorkflowView.tasks.TasksManagerContainer";
	private static final String PARAMETERMANAGER = "com.supermap.desktop.WorkflowView.ParameterManager";

	public static ParameterManager getParameterManager(boolean isActive) {
		ParameterManager parameterManager = null;
		IDockbar dockbarPropertyContainer = null;
		try {
			dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(PARAMETERMANAGER));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (dockbarPropertyContainer != null) {
			parameterManager = (ParameterManager) dockbarPropertyContainer.getInnerComponent();
		}
		if (isActive && dockbarPropertyContainer != null) {
			dockbarPropertyContainer.setVisible(true);
			dockbarPropertyContainer.active();
		}
		return parameterManager;
	}

	public static TasksManagerContainer getManagerContainer(boolean isActive) {
		TasksManagerContainer fileManagerContainer = null;
		IDockbar dockbarPropertyContainer = null;
		try {
			dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(TASKMANAGER));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (dockbarPropertyContainer != null) {
			fileManagerContainer = (TasksManagerContainer) dockbarPropertyContainer.getInnerComponent();
		}
		if (isActive && dockbarPropertyContainer != null) {
			dockbarPropertyContainer.setVisible(true);
			dockbarPropertyContainer.active();
		}
		return fileManagerContainer;
	}
}
