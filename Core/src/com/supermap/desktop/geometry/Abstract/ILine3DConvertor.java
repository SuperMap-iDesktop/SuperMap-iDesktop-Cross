package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.GeoLine3D;

/**
 * 三维线对象转换，转换为 {@link}GeoLine3D。
 * 
 * @author highsad
 *
 */
public interface ILine3DConvertor extends IGeometry {
	GeoLine3D convertToLine3D(int segment);
}
