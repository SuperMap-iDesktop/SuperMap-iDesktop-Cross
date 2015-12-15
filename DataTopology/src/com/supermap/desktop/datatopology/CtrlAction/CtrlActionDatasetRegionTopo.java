package com.supermap.desktop.datatopology.CtrlAction;

import javax.swing.JFrame;

import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author xie
 * 拓扑构面
 */
public class CtrlActionDatasetRegionTopo extends CtrlAction {

	public CtrlActionDatasetRegionTopo(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	
	public void run(){
		JDialogTopoBuildRegions buildRegions = new JDialogTopoBuildRegions((JFrame)Application.getActiveApplication().getMainFrame(), true);
		buildRegions.setVisible(true);
	}
	public boolean enable(){
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (null != datasources && 0 < datasources.getCount()) {
			return true;
		}
		return false;
	}
}
