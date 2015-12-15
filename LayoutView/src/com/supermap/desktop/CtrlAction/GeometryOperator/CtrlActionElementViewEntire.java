package com.supermap.desktop.CtrlAction.GeometryOperator;

import java.util.ArrayList;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.FormLayout;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.layout.LayoutSelection;
import com.supermap.mapping.Layer;

public class CtrlActionElementViewEntire extends CtrlAction {

	public CtrlActionElementViewEntire(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			FormLayout formLayout = (FormLayout) Application.getActiveApplication().getActiveForm();
			formLayout.geometryViewEntire();
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
