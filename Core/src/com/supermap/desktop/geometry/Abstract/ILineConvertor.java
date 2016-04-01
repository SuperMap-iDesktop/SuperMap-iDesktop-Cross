package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.Geometry;

public interface ILineConvertor extends IGeometry{
	Geometry convertToLine(int segment);
}
