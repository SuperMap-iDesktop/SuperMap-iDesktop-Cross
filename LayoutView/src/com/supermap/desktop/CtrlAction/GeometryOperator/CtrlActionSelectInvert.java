package com.supermap.desktop.CtrlAction.GeometryOperator;

import java.util.ArrayList;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.layout.LayoutElements;
import com.supermap.layout.LayoutSelection;

public class CtrlActionSelectInvert extends CtrlAction {

	public CtrlActionSelectInvert(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// 
	}

	@Override
	public void run() {
		try {
			IFormLayout formlayout = (IFormLayout) Application.getActiveApplication().getActiveForm();
			LayoutSelection selection = formlayout.getMapLayoutControl().getMapLayout().getSelection();
			ArrayList<Integer> selectedList = new ArrayList<Integer>();
			for (int i = 0; i < selection.getCount(); i++) {
				selectedList.add(selection.get(i));
			}
			formlayout.getMapLayoutControl().getMapLayout().getSelection().clear();
			LayoutElements elements = formlayout.getMapLayoutControl().getMapLayout().getElements();
			elements.moveFirst();
			while (!elements.isEOF()) {
				if (!selectedList.contains(elements.getID())) {
					formlayout.getMapLayoutControl().getMapLayout().getSelection().add(elements.getID());
				}
				elements.moveNext();
			}
			formlayout.getMapLayoutControl().getMapLayout().refresh();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
