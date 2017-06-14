package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by highsad on 2017/6/14.
 */
public class CtrlActionParameters extends CtrlAction {
	private static final String ParameterManagerClassName = "com.supermap.desktop.process.ParameterManager";

	public CtrlActionParameters(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(ParameterManagerClassName)).setVisible(true);
		} catch (ClassNotFoundException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
