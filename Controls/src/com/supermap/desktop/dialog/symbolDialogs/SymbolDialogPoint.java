package com.supermap.desktop.dialog.symbolDialogs;

import com.supermap.data.SymbolType;

import javax.swing.*;

/**
 * @author XiaJt
 */
public class SymbolDialogPoint extends SymbolDialog {
	@Override
	protected SymbolType getSymbolType() {
		return SymbolType.MARKER;
	}

	@Override
	protected void initComponentHook() {

	}

	@Override
	protected JPanel getPanelMain() {
		return new JPanel();
	}
}
