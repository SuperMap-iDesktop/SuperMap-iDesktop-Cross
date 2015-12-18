package com.supermap.desktop.CtrlAction;

import java.awt.Cursor;

import javax.swing.JFrame;

import com.supermap.data.GeoStyle;
import com.supermap.data.Resources;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SymbolDialog;
import com.supermap.desktop.ui.controls.SymbolLibraryDialog;

public class CtrlActionSymbolMarker extends CtrlAction {

	public CtrlActionSymbolMarker(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Resources resources = Application.getActiveApplication().getWorkspace().getResources();
			SymbolDialog symbolDialog = new SymbolDialog();
			symbolDialog.showDialog(resources, new GeoStyle(), SymbolType.MARKER);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
