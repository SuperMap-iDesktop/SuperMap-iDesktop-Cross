package com.supermap.desktop.CtrlAction;

import com.supermap.data.GeoStyle;
import com.supermap.data.Resources;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.SymbolDialog;

public class CtrlActionSymbolFill extends CtrlAction {

	public CtrlActionSymbolFill(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			Resources resources = Application.getActiveApplication().getWorkspace().getResources();
			SymbolDialog symbolDialog = new SymbolDialog();
			symbolDialog.showDialog(resources, new GeoStyle(), SymbolType.FILL);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
