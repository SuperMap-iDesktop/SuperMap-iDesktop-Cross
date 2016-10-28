package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.SymbolType;

/**
 * @author XiaJt
 */
public class JPanelSymbolsFill extends JPanelSymbols {
    private static final int systemFillCount = 8;

    @Override
    protected void changeGeoStyleId(int symbolID) {
        geoStyle.setFillSymbolID(symbolID);
    }

    @Override
    protected void initSystemPanels() {
        for (int i = 0; i < systemFillCount; i++) {
            this.add(new SymbolPanelFill(i, resources));
        }
    }

    @Override
    protected void initDefaultPanel() {
        for (int i = 0; i < symbolGroup.getCount(); i++) {
            this.add(new SymbolPanelFill(symbolGroup.get(i), resources));
        }
    }

    @Override
    public int getCurrentSymbolId() {
        return geoStyle.getFillSymbolID();
    }

	@Override
	protected SymbolType getSymbolType() {
		return SymbolType.FILL;
	}
}
