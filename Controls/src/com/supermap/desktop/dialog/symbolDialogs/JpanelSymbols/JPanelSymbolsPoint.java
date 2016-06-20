package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.GeoStyle;

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

	@Override
	protected int getCurrentSymbolId() {
		return this.geoStyle.getMarkerSymbolID();
	}

	@Override
	public void setGeoStyle(GeoStyle geoStyle) {
		super.setGeoStyle(geoStyle);

	}


	@Override
	protected void changeGeoStyleId(int symbolID) {
		this.geoStyle.setMarkerSymbolID(symbolID);
	}
}
