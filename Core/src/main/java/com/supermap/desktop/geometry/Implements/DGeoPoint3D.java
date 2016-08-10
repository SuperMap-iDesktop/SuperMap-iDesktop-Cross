package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoPoint3D;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IPoint3DFeature;

/**
 * @author XiaJT
 */
public class DGeoPoint3D extends AbstractGeometry implements IPoint3DFeature {

	private GeoPoint3D geometry;

	protected DGeoPoint3D(GeoPoint3D geometry) {
		super(geometry);
		this.geometry = geometry;
	}

}
