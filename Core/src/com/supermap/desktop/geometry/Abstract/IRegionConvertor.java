package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.Geometry;

public interface IRegionConvertor extends IGeometry{
	Geometry convertToRegion(int segment);
}
