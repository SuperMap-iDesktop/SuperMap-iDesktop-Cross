package com.supermap.desktop.CtrlAction;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.utilities.LogUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SillyB on 2016/8/1.
 */
public class WorkspaceAutoSave {
	private static WorkspaceAutoSave workspaceAutoSave = new WorkspaceAutoSave();
	private Timer timer;
	private TimerTask task;

	private WorkspaceAutoSave() {
		task = new TimerTask() {
			@Override
			public void run() {
				save();
			}
		};
		timer = new Timer();
	}

	private void save() {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		synchronized (workspace) {
			if ((workspace.getType() == WorkspaceType.SMWU || workspace.getType() == WorkspaceType.SXWU) && !StringUtilities.isNullOrEmpty(workspace.getConnectionInfo().getServer())) {
				IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
				for (int i = 0; i < formManager.getCount(); i++) {
					IForm iForm = formManager.get(i);
					String title = iForm.getText();
					if (iForm instanceof IFormLayout) {
						if (!workspace.getLayouts().getAvailableLayoutName(title).equals(title)) {
							workspace.getLayouts().setLayoutXML(title, ((IFormLayout) iForm).getMapLayoutControl().getMapLayout().toXML());
						}
					} else if (iForm instanceof IFormMap) {
						if (!workspace.getMaps().getAvailableMapName(title).equals(title)) {
							workspace.getMaps().setMapXML(title, ((IFormMap) iForm).getMapControl().getMap().toXML());
						}
					} else if (iForm instanceof IFormScene) {
						if (!workspace.getScenes().getAvailableSceneName(title).equals(title)) {
							workspace.getScenes().setSceneXML(title, ((IFormScene) iForm).getSceneControl().getScene().toXML());
						}
					}
				}
			}
			try {
				workspace.save();
			} catch (Exception e) {
				LogUtilities.outPut("Workspace AutoSave Failed");
			}
		}
	}

	public void start() {
		int workspaceAutoSaveTime = GlobalParameters.getWorkspaceAutoSaveTime() * 60000;
		timer.schedule(task, workspaceAutoSaveTime, workspaceAutoSaveTime);
	}

	public static WorkspaceAutoSave getInstance() {
		return workspaceAutoSave;
	}
}
