package com.supermap.desktop.process.util;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.UniversalMatrix;
import com.supermap.desktop.process.tasks.IProcessTask;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.process.tasks.TasksManagerContainer;

import java.util.ArrayList;

/**
 * Created by xie on 2017/2/28.
 */
public class TaskUtil {
	private static final String TASKMANAGER = "com.supermap.desktop.process.tasks.TasksManagerContainer";

	private static TasksManagerContainer getManagerContainer(boolean isActive) {
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

	/**
	 * 现在只考虑串行结构，且只有一个串时的情境
	 *
	 * @param universalMatrix
	 */
	public static TasksManagerContainer addTasks(UniversalMatrix universalMatrix) {
		TasksManagerContainer tasksManagerContainer = getManagerContainer(true);
		String processKey = (String) universalMatrix.getAllStartNode().get(0);
		ArrayList<String> process = new ArrayList<>();
		process.add(processKey);
		ArrayList<String> processArray = universalMatrix.getAllNextNode(processKey);
		process.addAll(processArray);
		int size = process.size();
		for (int i = 0; i < size; i++) {
			tasksManagerContainer.addItem(new ProcessTask((IProcess) universalMatrix.getNode(process.get(i))));
		}
		return tasksManagerContainer;
	}

}
