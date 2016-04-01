package com.supermap.desktop.geometry.Implements;

import com.supermap.data.Geometry;
import com.supermap.data.Point2Ds;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoLineM extends AbstractGeometry implements IMultiPartFeature, ILineFeature, ILineConvertor, IRegionConvertor {

	protected DGeoLineM(Geometry geometry) {
		super(geometry);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Geometry convertToRegion(int segment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Geometry convertToLine(int segment) {
		// TODO Auto-generated method stub
		return null;
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
