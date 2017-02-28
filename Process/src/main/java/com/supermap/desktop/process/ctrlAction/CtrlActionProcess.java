package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormProcess;

/**
 * @author XiaJT
 */
public class CtrlActionProcess extends CtrlAction {
	private static final String processTreeClassName = "com.supermap.desktop.process.diagram.ui.ProcessTree";

	public CtrlActionProcess(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IDockbar iDockbar = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(processTreeClassName));
			iDockbar.setVisible(true);

			FormProcess formProcess = new FormProcess();
			Application.getActiveApplication().getMainFrame().getFormManager().add(formProcess);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}
