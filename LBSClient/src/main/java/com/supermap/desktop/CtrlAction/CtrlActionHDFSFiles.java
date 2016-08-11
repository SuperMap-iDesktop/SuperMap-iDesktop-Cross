package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.JDialogTaskManager;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.ManagerXMLParser;

public class CtrlActionHDFSFiles extends CtrlAction {

	String topicNameRespond = " KernelDensity_Respond";

	public CtrlActionHDFSFiles(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			// JFrame parent = (JFrame) Application.getActiveApplication().getMainFrame();
			// JDialogHDFSFiles dialog = new JDialogHDFSFiles();
			// DialogResult result = dialog.showDialog();
			// if (result == DialogResult.OK) {
			// // WorkThead thread = new WorkThead();
			// // thread.start();
			// }
			if (CommonUtilities.isTaskManagerOpened()){
				Application.getActiveApplication().setActiveForm(CommonUtilities.getFormHDFSManager());
			}else {
				CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.LBSCONTROL);
			}
			if (ManagerXMLParser.getTotalTaskCount() > 0 && null==JDialogTaskManager.getTaskManager()) {
				CommonUtilities.recoverTask();
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