package com.supermap.desktop.CtrlAction.GeometryOperator;

import java.util.ArrayList;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.layout.LayoutSelection;

public class CtrlActionSelectNone extends CtrlAction {

	public CtrlActionSelectNone(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// 
	}

	@Override
	public void run() {
		try {
			IFormLayout formlayout = (IFormLayout) Application.getActiveApplication().getActiveForm();
			formlayout.getMapLayoutControl().getMapLayout().getSelection().clear();
			formlayout.getMapLayoutControl().getMapLayout().refresh();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			IFormLayout formlayout = (IFormLayout) Application.getActiveApplication().getActiveForm();
			LayoutSelection selection = formlayout.getMapLayoutControl().getMapLayout().getSelection();
			if (selection.getCount() > 0) {
				enable = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}

}
