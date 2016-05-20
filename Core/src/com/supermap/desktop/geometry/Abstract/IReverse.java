package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.Geometry;

/**
 * 反向
 * 
 * @author highsad
 *
 */
public interface IReverse extends IGeometry {

	/**
	 * 几何对象反向，得到一个新的对象
	 * 
	 * @return
	 */
	public Geometry reverse();
}
