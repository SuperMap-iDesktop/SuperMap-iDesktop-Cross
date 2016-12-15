package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormTabular;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class CtrlActionHiddenSystemField extends CtrlAction {

	public CtrlActionHiddenSystemField(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormTabular) {
			GlobalParameters.setIsTabularHiddenSystemField(((JCheckBox) getCaller()).isSelected());
//			((IFormTabular) activeForm).setHiddenSystemField(!((IFormTabular) activeForm).getHiddenSystemField());
//			Application.getActiveApplication().getOutput().output("1");
		}
	}

	@Override
	public boolean enable() {
		return Application.getActiveApplication().getActiveForm() instanceof FormTabular;
	}
}
