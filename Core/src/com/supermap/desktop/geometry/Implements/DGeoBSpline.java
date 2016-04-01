package com.supermap.desktop.geometry.Implements;

import com.supermap.data.Geometry;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoBSpline extends AbstractGeometry implements ILineFeature, ILineConvertor, IRegionConvertor {

	protected DGeoBSpline(Geometry geometry) {
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

}
