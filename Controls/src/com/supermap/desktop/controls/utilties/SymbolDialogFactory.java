package com.supermap.desktop.controls.utilties;

import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialog;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialogFill;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialogLine;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialogPoint;

import javax.swing.*;

/**
 * @author XiaJt
 */
public class SymbolDialogFactory {


	private static SymbolDialogPoint symbolDialogPoint;
	private static SymbolDialogLine symbolDialogLine;
	private static SymbolDialogFill symbolDialogFill;

	private SymbolDialogFactory() {

	}

	private static SymbolDialog getSymbolMarkerDialog() {
		if (symbolDialogPoint == null || symbolDialogPoint.isDisposed()) {
			symbolDialogPoint = new SymbolDialogPoint();
		} else if (symbolDialogPoint.getCurrentResources() != Application.getActiveApplication().getWorkspace().getResources()) {
			symbolDialogPoint.dispose();
			symbolDialogPoint = new SymbolDialogPoint();
		}
		return symbolDialogPoint;
	}

	private static SymbolDialog getSymbolLineDialog() {
		if (symbolDialogLine == null || symbolDialogLine.isDisposed()) {
			symbolDialogLine = new SymbolDialogLine();
		} else if (symbolDialogLine.getCurrentResources() != Application.getActiveApplication().getWorkspace().getResources()) {
			symbolDialogLine.dispose();
			symbolDialogLine = new SymbolDialogLine();
		}
		return symbolDialogLine;
	}

	private static SymbolDialog getSymbolFillDialog() {
		if (symbolDialogFill == null || symbolDialogFill.isDisposed()) {
			symbolDialogFill = new SymbolDialogFill();
		} else if (symbolDialogFill.getCurrentResources() != Application.getActiveApplication().getWorkspace().getResources()) {
			symbolDialogFill.dispose();
			symbolDialogFill = new SymbolDialogFill();
		}
		return symbolDialogFill;
	}

	public static SymbolDialog getSymbolDialog(SymbolType symbolType) {
		if (symbolType == SymbolType.MARKER || symbolType == SymbolType.MARKER3D) {
			return getSymbolMarkerDialog();
		} else if (symbolType == SymbolType.LINE || symbolType == SymbolType.PIPENODE) {
			return getSymbolLineDialog();
		} else if (symbolType == SymbolType.FILL) {
			return getSymbolFillDialog();
		}
		throw new UnsupportedOperationException("unSupport Symbol Type " + symbolType);
	}

	public static SymbolDialog getSymbolDialog(JDialog dialog, SymbolType symbolType) {
		if (symbolType == SymbolType.MARKER || symbolType == SymbolType.MARKER3D) {
			return new SymbolDialogPoint(dialog);
		} else if (symbolType == SymbolType.LINE || symbolType == SymbolType.PIPENODE) {
			return new SymbolDialogLine(dialog);
		} else if (symbolType == SymbolType.FILL) {
			return new SymbolDialogFill(dialog);
		}
		throw new UnsupportedOperationException("unSupport Symbol Type " + symbolType);
	}
}
