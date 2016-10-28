package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.SymbolType;

/**
 * @author XiaJt
 */
public class JPanelSymbolsLine extends JPanelSymbols {
    private static final int systemLineCount = 6;

    @Override
    protected void changeGeoStyleId(int symbolID) {
        geoStyle.setLineSymbolID(symbolID);
    }

    @Override
    protected void initSystemPanels() {
        for (int i = 0; i < systemLineCount; i++) {
            this.add(new SymbolPanelLine(i, resources));
        }
    }

    @Override
    protected void initDefaultPanel() {
        for (int i = 0; i < symbolGroup.getCount(); i++) {
            this.add(new SymbolPanelLine(symbolGroup.get(i), resources));
        }
    }

    @Override
    public int getCurrentSymbolId() {
        return geoStyle.getLineSymbolID();
    }

	@Override
	protected SymbolType getSymbolType() {
		return SymbolType.LINE;
	}
}
