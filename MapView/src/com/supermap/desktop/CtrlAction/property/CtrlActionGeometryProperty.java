package com.supermap.desktop.CtrlAction.property;

import javax.swing.JDialog;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionGeometryProperty extends CtrlAction {

	public CtrlActionGeometryProperty(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();

		if (activeForm instanceof FormMap) {
			((FormMap) activeForm).setSelectedGeometryProperty();
		}
		JDialog dialogPropertyContainer = (JDialog) Application.getActiveApplication().getMainFrame().getPropertyManager();
		dialogPropertyContainer.setVisible(true);
	}
}
