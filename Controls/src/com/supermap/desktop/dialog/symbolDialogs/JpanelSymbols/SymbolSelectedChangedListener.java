package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.Symbol;

/**
 * @author XiaJt
 */
public interface SymbolSelectedChangedListener {
	void SymbolSelectedChangedEvent(Symbol symbol);

	void SymbolSelectedDoubleClicked();
}
