package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoPoint;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IFlatFeature;
import com.supermap.desktop.geometry.Abstract.IPointFeature;

public class DGeoPoint extends AbstractGeometry implements IPointFeature {

	protected DGeoPoint(GeoPoint geoPoint) {
		super(geoPoint);
	}
}
