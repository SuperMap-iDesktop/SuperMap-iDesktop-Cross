package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoRoundRectangle;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IFlatFeature;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;

public class DGeoRoundRectangle extends AbstractGeometry implements IRegionFeature, ILineConvertor, IRegionConvertor {

	private GeoRoundRectangle geoRoundRectangle;

	protected DGeoRoundRectangle(GeoRoundRectangle geoRoundRectangle) {
		super(geoRoundRectangle);
		this.geoRoundRectangle = geoRoundRectangle;
	}

	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoRoundRectangle == null ? null : this.geoRoundRectangle.convertToRegion(segment);
	}

	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoRoundRectangle == null ? null : this.geoRoundRectangle.convertToLine(segment);
	}
}
