package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoCompound;
import com.supermap.data.Point2Ds;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;

public class DGeoCompound extends AbstractGeometry implements IMultiPartFeature {

	private GeoCompound geoCompound;

	protected DGeoCompound(GeoCompound geoCompound) {
		super(geoCompound);
		this.geoCompound = geoCompound;
	}

	@Override
	public int getPartCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point2Ds getPart(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
