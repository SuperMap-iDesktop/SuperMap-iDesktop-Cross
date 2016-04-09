package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

/**
 * @author XiaJt
 */
public class JPanelSymbolsPoint extends JPanelSymbols {

	private static final int systemPointsCount = 1;

	public JPanelSymbolsPoint() {
		super();
	}

	@Override
	protected void initSystemPanels() {
		for (int i = -1; i < systemPointsCount; i++) {
			this.add(new SymbolPanelPoint(i, resources));
		}
	}

	@Override
	protected void initDefaultPanel() {
		for (int i = 0; i < symbolGroup.getCount(); i++) {
			this.add(new SymbolPanelPoint(symbolGroup.get(i), resources));
		}
	}
}
