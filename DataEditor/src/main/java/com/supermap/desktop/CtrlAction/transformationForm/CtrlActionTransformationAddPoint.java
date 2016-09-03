package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormTransformation;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class CtrlActionTransformationAddPoint extends CtrlAction {
	private static final List<FormTransformation> addPointingForms = new ArrayList<>();

	public CtrlActionTransformationAddPoint(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
	}

	@Override
	public boolean enable() {
		return Application.getActiveApplication().getActiveForm().getWindowType() == WindowType.TRANSFORMATION;
	}

}
