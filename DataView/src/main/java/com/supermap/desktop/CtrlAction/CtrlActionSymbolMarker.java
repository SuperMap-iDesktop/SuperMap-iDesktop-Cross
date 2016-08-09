package com.supermap.desktop.CtrlAction;

import com.supermap.data.GeoStyle;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.utilities.SymbolDialogFactory;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialog;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionSymbolMarker extends CtrlAction {

	public CtrlActionSymbolMarker(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			SymbolDialog symbolDialog = SymbolDialogFactory.getSymbolDialog(SymbolType.MARKER);
			symbolDialog.showDialog(new GeoStyle());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
