package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoEllipticArc;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoEllipticArc extends AbstractGeometry implements ILineFeature, ILineConvertor, IRegionConvertor {

	private GeoEllipticArc geoEllipticArc;

	protected DGeoEllipticArc(GeoEllipticArc geoEllipticArc) {
		super(geoEllipticArc);
		this.geoEllipticArc = geoEllipticArc;
	}

	@Override
	public GeoRegion convertToRegion(int segment) {
		GeoLine geoLine = convertToLine(segment);
		return geoLine == null ? null : geoLine.convertToRegion();
	}

	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoEllipticArc == null ? null : this.geoEllipticArc.convertToLine(segment);
	}

}
