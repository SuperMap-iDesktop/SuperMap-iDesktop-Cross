package com.supermap.desktop.CtrlAction;

import com.supermap.Interface.ITaskManager;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.http.TaskManagerContainer;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.task.mananger.BoundsQueryTaskManager;
import com.supermap.desktop.utilities.CommonUtilities;

public class CtrlActionBoundsQuery extends CtrlAction {

	String topicNameRespond = "SpatialQuery_Respond";

	public CtrlActionBoundsQuery(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			// JFrame parent = (JFrame)Application.getActiveApplication().getMainFrame();
			// JDialogBoundsQuery dialog = new JDialogBoundsQuery(parent, true);
			// DialogResult result = dialog.showDialog();
			// if (result == DialogResult.OK || result == DialogResult.APPLY) {
			// WorkThead thread = new WorkThead();
			// thread.start();
			// }
			ITaskManager manager = new BoundsQueryTaskManager();
			TaskManagerContainer taskManagerContainer = CommonUtilities.getTaskManagerContainer();
			if (null != taskManagerContainer) {
				taskManagerContainer.setManager(manager);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}

	class WorkThead extends Thread {

		@Override
		public void run() {
			try {
				// lbsResultConsumer consumer = new lbsResultConsumer();
				// consumer.doWork(topicNameRespond);
			} finally {
			}
		}
	}
}