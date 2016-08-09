package com.supermap.desktop.CtrlAction.GeometryOperator;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.layout.LayoutElements;

public class CtrlActionSelectAll extends CtrlAction {

	public CtrlActionSelectAll(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormLayout formlayout = (IFormLayout) Application.getActiveApplication().getActiveForm();
			formlayout.getMapLayoutControl().getMapLayout().getSelection().clear();
			LayoutElements elements = formlayout.getMapLayoutControl().getMapLayout().getElements();
			for (int i = 0; i < elements.getCount(); i++) {
				formlayout.getMapLayoutControl().getMapLayout().getSelection().add(elements.getID());
				elements.moveNext();
			}
			formlayout.getMapLayoutControl().getMapLayout().refresh();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
