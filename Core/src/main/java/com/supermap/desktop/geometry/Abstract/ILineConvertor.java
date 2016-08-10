package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.GeoLine;

/**
 * 线对象转换，转换为 {@link}GeoLine。
 * 
 * @author highsad
 *
 */
public interface ILineConvertor extends IGeometry {
	GeoLine convertToLine(int segment);
}
