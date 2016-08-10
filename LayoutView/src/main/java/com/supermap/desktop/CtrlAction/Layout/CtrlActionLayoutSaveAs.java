package com.supermap.desktop.CtrlAction.Layout;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionLayoutSaveAs extends CtrlAction {

	public CtrlActionLayoutSaveAs(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			IFormLayout formLayout = (IFormLayout)Application.getActiveApplication().getActiveForm();
			formLayout.saveAs(false);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getActiveForm() != null
    			&& Application.getActiveApplication().getActiveForm() instanceof IFormLayout) {
    		enable = true;
    	}
        return enable;
	}

}
