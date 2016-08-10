package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoArc;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoArc extends AbstractGeometry implements ILineFeature, ILineConvertor, IRegionConvertor {

	private GeoArc geoArc;

	protected DGeoArc(GeoArc geoArc) {
		super(geoArc);
		this.geoArc = geoArc;
	}

	@Override
	public GeoRegion convertToRegion(int segment) {
		GeoLine geoLine = convertToLine(segment);
		return geoLine == null ? null : geoLine.convertToRegion();
	}

	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoArc == null ? null : this.geoArc.convertToLine(segment);
	}

}
