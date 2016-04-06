package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoCircle;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;

public class DGeoCircle extends AbstractGeometry implements IRegionFeature, ILineConvertor, IRegionConvertor {

	private GeoCircle geoCircle;

	protected DGeoCircle(GeoCircle geoCircle) {
		super(geoCircle);
		this.geoCircle = geoCircle;
	}

	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoCircle == null ? null : this.geoCircle.convertToRegion(segment);
	}

	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoCircle == null ? null : this.geoCircle.convertToLine(segment);
	}

}
