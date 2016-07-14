package com.supermap.desktop.CtrlAction;

import com.supermap.data.Workspace;
import com.supermap.desktop.Application;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XiaJT
 */
public class WorkspaceAutoSave {

	private static WorkspaceAutoSave workspaceAutoSave;
	private Timer timer;
	private TimerTask task;
	private int period = 60000; // 1 min

	private void WorkspaceAutoSave() {
		timer = new Timer("WorkspaceSave", true);
		task = new TimerTask() {
			@Override
			public void run() {
				Workspace workspace = Application.getActiveApplication().getWorkspace();

			}
		};

		timer.schedule(task, 1000, period);

	}


	public static WorkspaceAutoSave getInstance() {
		if (workspaceAutoSave == null) {
			workspaceAutoSave = new WorkspaceAutoSave();
		}
		return workspaceAutoSave;
	}
}
