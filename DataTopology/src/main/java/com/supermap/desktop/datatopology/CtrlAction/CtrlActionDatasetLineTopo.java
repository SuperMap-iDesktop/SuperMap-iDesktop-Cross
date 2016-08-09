package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

import javax.swing.*;

/**
 * 线拓扑处理
 * @author xie
 */
public class CtrlActionDatasetLineTopo extends CtrlAction {

	public CtrlActionDatasetLineTopo(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run() {
		JDialogDatasetLineTopo lineTopo = new JDialogDatasetLineTopo((JFrame)Application.getActiveApplication().getMainFrame(), true);
		lineTopo.setVisible(true);
	}

	public boolean enable() {
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (null != datasources && 0 < datasources.getCount()) {
			return true;
		}
		return false;
	}

}
