package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoChord;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoChord extends AbstractGeometry implements ILineFeature, ILineConvertor, IRegionConvertor {

	private GeoChord geoChord;

	protected DGeoChord(GeoChord geoChord) {
		super(geoChord);
		this.geoChord = geoChord;
	}

	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoChord == null ? null : convertToRegion(segment);
	}

	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoChord == null ? null : convertToLine(segment);
	}
}
