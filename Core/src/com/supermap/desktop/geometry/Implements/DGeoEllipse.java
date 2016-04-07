package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoEllipse;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;

public class DGeoEllipse extends AbstractGeometry implements IRegionFeature, ILineConvertor, IRegionConvertor {

	private GeoEllipse geoEllipse;

	protected DGeoEllipse(GeoEllipse geoEllipse) {
		super(geoEllipse);
		this.geoEllipse = geoEllipse;
	}

	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoEllipse == null ? null : this.geoEllipse.convertToRegion(segment);
	}

	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoEllipse == null ? null : this.geoEllipse.convertToLine(segment);
	}

}
