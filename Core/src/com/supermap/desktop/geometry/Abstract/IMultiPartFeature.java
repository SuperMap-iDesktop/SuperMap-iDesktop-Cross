package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.Point2Ds;

/**
 * 表示多部分聚合特性
 * 
 * @author highsad
 *
 */
public interface IMultiPartFeature extends IGeometryFeature {
	public int getPartCount();

	public Point2Ds getPart(int index);
}
