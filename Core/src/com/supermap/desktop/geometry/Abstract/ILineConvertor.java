package com.supermap.desktop.geometry.Abstract;

import com.supermap.data.GeoLine;

public interface ILineConvertor extends IGeometry {
	GeoLine convertToLine(int segment);
}
