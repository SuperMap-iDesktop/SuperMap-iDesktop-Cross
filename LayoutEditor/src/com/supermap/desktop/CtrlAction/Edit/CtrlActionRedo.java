package com.supermap.desktop.CtrlAction.Edit;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionRedo extends CtrlAction {

	public CtrlActionRedo(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	
	@Override
	public void run() {
		try {
			IFormLayout form = (IFormLayout)Application.getActiveApplication().getActiveForm();
			form.getMapLayoutControl().redo();	
			form.getMapLayoutControl().getMapLayout().refresh();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			IForm form = Application.getActiveApplication().getActiveForm();
			if (form != null && form instanceof IFormLayout) {
				enable = ((IFormLayout)form).getMapLayoutControl().canRedo();	
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}
}
