package com.supermap.desktop.event;

import com.supermap.data.SymbolGroup;
import com.supermap.data.SymbolType;
import com.supermap.desktop.enums.SymbolChangeType;

/**
 * @author XiaJT
 */
public class SymbolChangedEvent {
	private SymbolType symbolType;
	private int SymbolId;
	private SymbolChangeType SymbolChangeType;
	private SymbolGroup parentSymbolGroup;

	public SymbolChangedEvent(SymbolType symbolType, int symbolId, SymbolChangeType symbolChangeType, SymbolGroup parentSymbolGroup) {
		this.symbolType = symbolType;
		SymbolId = symbolId;
		SymbolChangeType = symbolChangeType;
		this.parentSymbolGroup = parentSymbolGroup;
	}

	public SymbolType getSymbolType() {
		return symbolType;
	}

	public void setSymbolType(SymbolType symbolType) {
		this.symbolType = symbolType;
	}

	public int getSymbolId() {
		return SymbolId;
	}

	public void setSymbolId(int symbolId) {
		SymbolId = symbolId;
	}

	public com.supermap.desktop.enums.SymbolChangeType getSymbolChangeType() {
		return SymbolChangeType;
	}

	public void setSymbolChangeType(com.supermap.desktop.enums.SymbolChangeType symbolChangeType) {
		SymbolChangeType = symbolChangeType;
	}

	public SymbolGroup getParentSymbolGroup() {
		return parentSymbolGroup;
	}

	public void setParentSymbolGroup(SymbolGroup parentSymbolGroup) {
		this.parentSymbolGroup = parentSymbolGroup;
	}
}
