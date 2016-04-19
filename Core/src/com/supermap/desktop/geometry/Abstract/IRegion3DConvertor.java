package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.GeoRegion3D;

/**
 * 三维面对象转换，转换为 {@link}GeoRegion3D。
 * 
 * @author highsad
 *
 */
public interface IRegion3DConvertor extends IGeometry {
	GeoRegion3D convertToRegion3D(int segment);
}
