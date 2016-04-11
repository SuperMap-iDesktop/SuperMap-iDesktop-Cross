package com.supermap.desktop.controls.utilties;

import com.supermap.desktop.Application;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialog;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialogFill;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialogLine;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialogPoint;

/**
 * @author XiaJt
 */
public class SymbolDialogUtilties {


	private static SymbolDialogPoint symbolDialogPoint;
	private static SymbolDialogLine symbolDialogLine;
	private static SymbolDialogFill symbolDialogFill;

	private SymbolDialogUtilties() {

	}

	public static SymbolDialog getSymbolMarkerDialog() {
		if (symbolDialogPoint == null) {
			symbolDialogPoint = new SymbolDialogPoint();
		} else if (symbolDialogPoint.getCurrentRescources() != Application.getActiveApplication().getWorkspace().getResources()) {
			symbolDialogPoint.dispose();
			symbolDialogPoint = new SymbolDialogPoint();
		}
		return symbolDialogPoint;
	}

	public static SymbolDialog getSymbolLineDialog() {
		if (symbolDialogLine == null) {
			symbolDialogLine = new SymbolDialogLine();
		} else if (symbolDialogLine.getCurrentRescources() != Application.getActiveApplication().getWorkspace().getResources()) {
			symbolDialogLine.dispose();
			symbolDialogLine = new SymbolDialogLine();
		}
		return symbolDialogLine;
	}

	public static SymbolDialog getSymbolFillDialog() {
		if (symbolDialogFill == null) {
			symbolDialogFill = new SymbolDialogFill();
		} else if (symbolDialogFill.getCurrentRescources() != Application.getActiveApplication().getWorkspace().getResources()) {
			symbolDialogFill.dispose();
			symbolDialogFill = new SymbolDialogFill();
		}
		return symbolDialogFill;
	}
}
