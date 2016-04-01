package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.Point2Ds;

public interface IMultiPartFeature extends IGeometry {
	public int getPartCount();

	public Point2Ds getPart(int index);
}
