package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoCompound;
import com.supermap.data.Geometry;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;

public class DGeoCompound extends AbstractGeometry implements IMultiPartFeature<Geometry> {

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
	public Geometry getPart(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPart(Geometry part) {
		if (this.geoCompound != null) {
			this.geoCompound.addPart(part);
		}
	}

}
