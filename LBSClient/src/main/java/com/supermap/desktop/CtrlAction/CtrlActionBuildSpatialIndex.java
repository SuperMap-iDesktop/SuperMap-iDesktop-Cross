package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.JDialogBuildSpatialIndex;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;

import javax.swing.*;

public class CtrlActionBuildSpatialIndex extends CtrlAction {

	String topicNameRespond = "BuildSpatialIndex_Respond";
	
	public CtrlActionBuildSpatialIndex(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			JFrame parent = (JFrame)Application.getActiveApplication().getMainFrame();
			JDialogBuildSpatialIndex dialog = new JDialogBuildSpatialIndex(parent, true);
			DialogResult result = dialog.showDialog();
			if (result == DialogResult.OK || result == DialogResult.APPLY) {
				WorkThead thread = new WorkThead();
				thread.start();
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
//				lbsResultConsumer consumer = new lbsResultConsumer();
//				consumer.doWork(topicNameRespond);
			} finally {
			}
		}
	}

	
}
