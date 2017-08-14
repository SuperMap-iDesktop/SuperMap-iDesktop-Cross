package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormTabular;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.JDialogOutputExcel;
import com.supermap.desktop.utilities.TableUtilities;

/**
 * @author XiaJT
 */
public class CtrlActionOutputExcel extends CtrlAction {
	public CtrlActionOutputExcel(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		IFormTabular tabular = (IFormTabular) Application.getActiveApplication().getActiveForm();
		JDialogOutputExcel jDialogOutputExcel = new JDialogOutputExcel(tabular);
		TableUtilities.stopEditing(tabular.getjTableTabular());
		jDialogOutputExcel.showDialog();
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormTabular) {
			return ((FormTabular) activeForm).getSelectedRow() != -1;
		}
		return super.enable();
	}
}