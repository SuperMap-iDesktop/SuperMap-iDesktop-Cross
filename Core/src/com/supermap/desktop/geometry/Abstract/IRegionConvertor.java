package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.GeoRegion;

/**
 * 面对象转换
 * 
 * @author highsad
 *
 */
public interface IRegionConvertor extends IGeometry {
	GeoRegion convertToRegion(int segment);
}
