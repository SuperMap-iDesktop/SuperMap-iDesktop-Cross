package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionMapSaveAs extends CtrlAction {

	public CtrlActionMapSaveAs(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
            IFormMap formMap = (IFormMap)Application.getActiveApplication().getActiveForm();
            formMap.saveAs(false);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getActiveForm() != null
    			&& Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
    		enable = true;
    	}
        return enable;
	}

}
