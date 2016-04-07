package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.GeoRegion;

public interface IRegionConvertor extends IGeometry {
	GeoRegion convertToRegion(int segment);
}
