package com.supermap.desktop.controls.utilties;

import com.supermap.desktop.dialog.symbolDialogs.SymbolDialog;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialogPoint;

/**
 * @author XiaJt
 */
public class SymbolDialogUtilties {


	private static SymbolDialogPoint symbolDialogPoint;

	//	private static SymbolDialogLine symbolDialogLine;
//	private static SymbolDialogFill symbolDialogFill;
	private SymbolDialogUtilties() {

	}

	public static SymbolDialog getSymbolMarkerDialog() {
		if (symbolDialogPoint == null) {
			symbolDialogPoint = new SymbolDialogPoint();
		}
		return symbolDialogPoint;
	}
}
