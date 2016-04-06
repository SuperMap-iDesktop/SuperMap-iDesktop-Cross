package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoBSpline;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoBSpline extends AbstractGeometry implements ILineFeature, ILineConvertor, IRegionConvertor {

	private GeoBSpline geoBSpline;

	protected DGeoBSpline(GeoBSpline geoBSpline) {
		super(geoBSpline);
		this.geoBSpline = geoBSpline;
	}

	@Override
	public GeoRegion convertToRegion(int segment) {
		GeoLine geoLine = convertToLine(segment);
		return geoLine == null ? null : geoLine.convertToRegion();
	}

	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoBSpline == null ? null : this.geoBSpline.convertToLine(segment);
	}
}
