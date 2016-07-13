package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoCardinal;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IFlatFeature;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoCardinal extends AbstractGeometry implements ILineFeature, ILineConvertor, IRegionConvertor {

	private GeoCardinal geoCardinal;

	protected DGeoCardinal(GeoCardinal geoCardinal) {
		super(geoCardinal);
		this.geoCardinal = geoCardinal;
	}

	@Override
	public GeoRegion convertToRegion(int segment) {
		GeoLine geoLine = convertToLine(segment);
		return geoLine == null ? null : geoLine.convertToRegion();
	}

	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoCardinal == null ? null : this.geoCardinal.convertToLine(segment);
	}

}
