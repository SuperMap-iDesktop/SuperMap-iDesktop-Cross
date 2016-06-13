package com.supermap.desktop.CtrlAction;


import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.CtrlAction.CtrlActionFindTrack.WorkThead;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.JDialogBoundsQuery;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CtrlActionBoundsQuery extends CtrlAction {

	String topicNameRespond = "SpatialQuery_Respond";
	
	public CtrlActionBoundsQuery(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			JFrame parent = (JFrame)Application.getActiveApplication().getMainFrame();
			JDialogBoundsQuery dialog = new JDialogBoundsQuery(parent, true);
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