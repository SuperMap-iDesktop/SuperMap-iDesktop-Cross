package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionIsSelectedColumn extends CtrlAction {
	public CtrlActionIsSelectedColumn(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		GlobalParameters.setIsHeadClickedSelectedColumn(!GlobalParameters.isHeadClickedSelectedColumn());
		ToolbarUIUtilities.updataToolbarsState();
	}

	@Override
	public boolean enable() {
		return Application.getActiveApplication().getActiveForm() instanceof IFormTabular;
	}

	@Override
	public boolean check() {
		return GlobalParameters.isHeadClickedSelectedColumn();
	}
}
