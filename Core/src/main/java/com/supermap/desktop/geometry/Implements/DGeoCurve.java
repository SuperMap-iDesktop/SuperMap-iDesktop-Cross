package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoCurve;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoCurve extends AbstractGeometry implements ILineFeature, ILineConvertor, IRegionConvertor {

	private GeoCurve geoCurve;

	protected DGeoCurve(GeoCurve geoCurve) {
		super(geoCurve);
		this.geoCurve = geoCurve;
	}

	@Override
	public GeoRegion convertToRegion(int segment) {
		GeoLine geoLine = convertToLine(segment);
		return geoLine == null ? null : geoLine.convertToRegion();
	}

	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoCurve == null ? null : this.geoCurve.convertToLine(segment);
	}

}
